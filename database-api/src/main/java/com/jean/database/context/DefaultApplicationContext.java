package com.jean.database.context;

import com.jean.database.task.BackgroundTask;
import com.jean.database.task.BackgroundTaskManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultApplicationContext implements ApplicationContext {

    private final Logger logger = LoggerFactory.getLogger(DefaultApplicationContext.class);

    private ViewContext rootContext;

    private BackgroundTaskManager backgroundTaskManager;

    public DefaultApplicationContext() {
    }

    public DefaultApplicationContext(ViewContext rootContext, BackgroundTaskManager backgroundTaskManager) {
        this.rootContext = rootContext;
        this.backgroundTaskManager = backgroundTaskManager;
    }

    protected BackgroundTaskManager getBackgroundTaskManager() {
        return backgroundTaskManager;
    }

    public void setRootContext(ViewContext rootContext) {
        this.rootContext = rootContext;
    }

    public void setBackgroundTaskManager(BackgroundTaskManager backgroundTaskManager) {
        this.backgroundTaskManager = backgroundTaskManager;
    }


    public <V> void execute(BackgroundTask<V> task) {
        task.progressProperty().addListener(new TaskProgressListener(this));
        task.messageProperty().addListener(new TaskMessageListener(this));
        getBackgroundTaskManager().execute(task);
    }

    @Override
    public void close() {
        try {
            if (getBackgroundTaskManager() != null) {
                getBackgroundTaskManager().shutdown();
            }
        } catch (Exception e) {
            logger.error("close background task error", e);
        }
    }

    @Override
    public void addObjectTab(Tab tab) {
        rootContext.addObjectTab(tab);
    }

    @Override
    public void removeObjectTab(Tab tab) {
        rootContext.removeObjectTab(tab);
    }

    @Override
    public void addDatabaseItem(TreeItem treeItem) {
        rootContext.addDatabaseItem(treeItem);
    }

    @Override
    public void setSelected(TreeItem treeItem) {
        rootContext.setSelected(treeItem);
    }

    @Override
    public void removeDatabaseItem(TreeItem treeItem) {
        rootContext.removeDatabaseItem(treeItem);
    }

    @Override
    public void addFileMenus(MenuItem... menu) {
        rootContext.addFileMenus(menu);
    }

    @Override
    public void addConnectionMenus(MenuItem... menu) {
        rootContext.addConnectionMenus(menu);
    }

    @Override
    public void addCollectionMenus(MenuItem... menu) {
        rootContext.addCollectionMenus(menu);
    }

    @Override
    public void addViewMenus(MenuItem... menu) {
        rootContext.addViewMenus(menu);
    }

    @Override
    public void addToolsMenus(MenuItem... menu) {
        rootContext.addToolsMenus(menu);
    }

    @Override
    public void addWindowMenus(MenuItem... menu) {
        rootContext.addWindowMenus(menu);
    }

    @Override
    public void addHelpMenus(MenuItem... menu) {
        rootContext.addHelpMenus(menu);
    }

    @Override
    public void updateProgress(double progress) {
        rootContext.updateProgress(progress);
    }

    @Override
    public void updateMessage(String message) {
        rootContext.updateMessage(message);
    }

    private static class TaskProgressListener implements ChangeListener<Number> {

        private final ApplicationContext context;

        private TaskProgressListener(ApplicationContext context) {
            this.context = context;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            context.updateProgress(t1.doubleValue());
        }

    }

    private static class TaskMessageListener implements ChangeListener<String> {
        private final ApplicationContext context;

        private TaskMessageListener(ApplicationContext context) {
            this.context = context;
        }

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            context.updateMessage(t1);
        }
    }
}
