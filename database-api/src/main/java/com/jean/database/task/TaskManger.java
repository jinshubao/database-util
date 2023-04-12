package com.jean.database.task;


import javafx.concurrent.Task;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class TaskManger {

    private TaskManger() {
    }

    private static class Holder {
        private static final int MIN_THREADS = Runtime.getRuntime().availableProcessors();
        private static final int MAX_THREADS = MIN_THREADS * 2;
        private static final TaskThreadPoolExecutor THREAD_POOL_EXECUTOR = new TaskThreadPoolExecutor(MIN_THREADS, MAX_THREADS,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(MAX_THREADS + 1),
                new TaskThreadFactory());
    }

    public static void execute(Task<?> task) {
        Holder.THREAD_POOL_EXECUTOR.execute(task);
    }

    public static void shutdown() {
        Holder.THREAD_POOL_EXECUTOR.shutdown();
    }

    public static Executor getExecutor() {
        return Holder.THREAD_POOL_EXECUTOR;
    }


}
