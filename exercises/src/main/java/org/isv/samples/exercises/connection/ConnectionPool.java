package org.isv.samples.exercises.connection;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;


public class ConnectionPool<C extends AutoCloseable> {
    private final Supplier<C> connectionFactory;
    private final Object lock = new Object();
    private final ArrayList<ConnectionWrapper<C>> pool = new ArrayList<>();
    private final Semaphore freeConnections;
    private boolean stopped = false;

    public ConnectionPool(Supplier<C> connectionFactory, int maxConnections) {
        Objects.requireNonNull(connectionFactory);

        if (maxConnections < 1) {
            throw new IllegalArgumentException("You should specify maxConnections at least 1");
        }

        this.connectionFactory = connectionFactory;
        this.freeConnections = new Semaphore(maxConnections);
    }


    public RemoteConnection<C> connect() {
        return getConnection();
    }

    public void shutdown() {
        synchronized (lock) {
            for (var connection : pool) {
                connection.destroy();
            }
            pool.clear();
            stopped = true;
        }
    }

    private RemoteConnection<C> getConnection() {
        try {
            freeConnections.acquire();

            synchronized (lock) {
                for (var connection : pool) {
                    if (!connection.isBusy()) {
                        connection.setBusy(true);
                        return connection;
                    }
                }
            }

            var newConnection = ConnectionWrapper.<C>builder()
                    .originalConnection(connectionFactory.get()).busy(true)
                    .lock(lock).freeConnections(freeConnections).build();

            synchronized (lock) {
                if (stopped) {
                    newConnection.destroy();
                    throw new IllegalStateException("connect to closed pool");
                }
                pool.add(newConnection);
                return newConnection;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            freeConnections.release();
            throw ex;
        }
    }

    @Data
    @Builder
    private static class ConnectionWrapper<Connection extends AutoCloseable> implements RemoteConnection<Connection> {

        private final Connection originalConnection;
        private final Object lock;
        private final Semaphore freeConnections;
        @Setter
        private boolean busy;

        @Override
        public Connection getRemoteConnection() {
            return originalConnection;
        }

        public void destroy() {
            try {
                originalConnection.close();
            } catch (Exception ex) {
                //do nothing, because of shutdown process
            }
        }

        @Override
        public void close() throws Exception {
            synchronized (lock) {
                if (busy) {
                    setBusy(false);
                    freeConnections.release();
                }
            }
        }
    }


}
