package com.jean.database.api;

import javafx.scene.control.*;

@SuppressWarnings({"rawtypes"})
public interface ViewContext {

    void addObjectTab(Tab tab);

    void removeObjectTab(Tab tab);

    void addDatabaseItem(TreeItem treeItem);

    void addConnectionMenus(MenuItem... menu);

    void addViewMenus(MenuItem... menu);

    void updateProgress(double progress);
}
