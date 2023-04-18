package com.jean.database.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DefaultBackgroundTaskManager implements BackgroundTaskManager {

    private final ExecutorService executor;

    public DefaultBackgroundTaskManager(int minThreads, int maxThreads) {
        this.executor = new TaskThreadPoolExecutor(minThreads, maxThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(maxThreads + 5),
                new DefaultTaskThreadFactory());
    }

    @Override
    public <V> void execute(BackgroundTask<V> task) {
        executor.execute(task);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

}
