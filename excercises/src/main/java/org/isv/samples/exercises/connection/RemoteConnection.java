package org.isv.samples.exercises.connection;

public interface RemoteConnection<C> extends AutoCloseable{
    C getRemoteConnection();
}
