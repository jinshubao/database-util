package com.jean.database.api;


import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TaskManger {

    private static ExecutorService executorService;

    private TaskManger() {
    }

    public static synchronized void init(int threadNumber) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(threadNumber, new TaskThreadFactory());
        }
    }

    public static void execute(Task<?> task) {
        executorService.execute(task);
    }

    public static void shutdown() {
        executorService.shutdown();
    }


}
