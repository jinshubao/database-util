package com.jean.database.context;

import com.jean.database.task.BackgroundTaskManager;

import java.io.FileOutputStream;
import java.io.OutputStream;


public class DefaultApplicationContext implements ApplicationContext {

    private ViewContext rootContext;

    private BackgroundTaskManager backgroundTaskManager;

    public DefaultApplicationContext() {
    }

    public DefaultApplicationContext(ViewContext rootContext, BackgroundTaskManager backgroundTaskManager) {
        this.rootContext = rootContext;
        this.backgroundTaskManager = backgroundTaskManager;
    }

    @Override
    public ViewContext getRootContext() {
        return rootContext;
    }

    @Override
    public BackgroundTaskManager getBackgroundTaskManager() {
        return backgroundTaskManager;
    }

    public void setRootContext(ViewContext rootContext) {
        this.rootContext = rootContext;
    }

    public void setBackgroundTaskManager(BackgroundTaskManager backgroundTaskManager) {
        this.backgroundTaskManager = backgroundTaskManager;
    }
}
