package com.jean.database.task;

import java.util.function.Consumer;

public class TaskBuilder<T extends BackgroundTask<V>, V> {

    private Consumer<V> onSuccess = null;
    private Consumer<Throwable> onFailed = null;

    private TaskBuilder() {
    }

    public static <T extends BackgroundTask<V>, V> TaskBuilder<T,V> newBuilder() {
        TaskBuilder<T,V> builder = new TaskBuilder<>();
        BackgroundTask<String> task = new BackgroundTask<String>("", ""){

            @Override
            protected String call() throws Exception {
                return null;
            }
        };
        BackgroundTaskManager manager = new DefaultBackgroundTaskManager(1, 1);
        manager.execute(task);
        manager.shutdown();
        return builder;
    }

    public void setOnSuccess(Consumer<V> onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void setOnFailed(Consumer<Throwable> onFailed) {
        this.onFailed = onFailed;
    }
}
