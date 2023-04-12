package com.jean.database.task;

public interface BackgroundTaskManager {

    <V> void execute(BackgroundTask<V> task);

    void shutdown();
}
