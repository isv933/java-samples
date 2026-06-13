package org.isv.samples.exercises.threads;

public class PromiseWait<T> implements Promise<T> {
    private final Object lock = new Object();
    private T result;
    private Throwable exception;
    private boolean finished;

    @Override
    public void complete(T result) {
        this.result = result;
        complete();
    }

    @Override
    public void competeException(Throwable ex){
        this.exception = ex;
        complete();
    }

    @Override
    public T get() {
        try {
            synchronized (lock) {
                if (!finished) {
                    lock.wait();
                }
                if (result != null) {
                    return result;
                }
            }
            throw new IllegalStateException(exception);
        }
        catch(InterruptedException ex){
            throw new IllegalStateException(ex);
        }
    }

    private void complete(){
        synchronized (lock){
            lock.notifyAll();
            finished = true;
        }
    }
}
