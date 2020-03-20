package com.jean.database.gui.manager;

import com.jean.database.gui.factory.DatabaseThreadFactory;
import com.jean.database.gui.task.BaseTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManger {

    private static final ExecutorService executorService;

    static {
        int thread_num = Runtime.getRuntime().availableProcessors() * 2;
        executorService = Executors.newFixedThreadPool(thread_num, new DatabaseThreadFactory());
    }

    private TaskManger() {
    }


    public static void execute(BaseTask<?> task) {
        executorService.execute(task);
    }

    public static void shutdown() {
        executorService.shutdown();
    }
}
