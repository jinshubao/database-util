package com.jean.database.api;


import javafx.concurrent.Task;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TaskManger {

    private TaskManger() {
    }

    private static class Holder {
        private static final ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new TaskThreadFactory());
    }

    public static void execute(Task<?> task) {
        Holder.executor.execute(task);
    }

    public static void shutdown() {
        Holder.executor.shutdown();
    }

    public static Executor getExecutor(){
        return Holder.executor;
    }


}
