package com.jean.database.context;

import com.jean.database.task.BackgroundTaskManager;

import java.io.OutputStream;

public interface ApplicationContext {

    ViewContext getRootContext();

    BackgroundTaskManager getBackgroundTaskManager();

}
