package org.isv.samples.exercises.threads;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadFactory;


@RequiredArgsConstructor
public class DaemonThreadFactory implements ThreadFactory {
    private final String threadName;
    @Override
    public Thread newThread(Runnable r) {
        var thread = new Thread(r);
        thread.setName(threadName);
        thread.setDaemon(true);

        return thread;
    }
}
