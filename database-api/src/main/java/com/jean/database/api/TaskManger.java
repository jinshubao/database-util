package com.jean.database.api;


import javafx.concurrent.Task;

import java.util.concurrent.*;

public final class TaskManger {

    private TaskManger() {
    }

    private static class Holder {
        private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
        private static final TaskThreadPoolExecutor THREAD_POOL_EXECUTOR = new TaskThreadPoolExecutor(N_THREADS, N_THREADS,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
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
