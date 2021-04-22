package com.jean.database.api;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

@SuppressWarnings({"rawtypes"})
public interface ViewContext {

    BorderPane getRoot();

    MenuBar getMenuBar();

    TreeView<?> getTreeView();

    TabPane getObjectTabPan();

    void addObjectTab(Tab tab);

    void removeObjectTab(Tab tab);

    void addDatabaseItem(TreeItem treeItem);

    void addFileMenus(MenuItem... menu);

    void addConnectionMenus(MenuItem... menu);

    void addCollectionMenus(MenuItem... menu);

    void addViewMenus(MenuItem... menu);

    void addToolsMenus(MenuItem... menu);

    void addWindowMenus(MenuItem... menu);

    void addHelpMenus(MenuItem... menu);

    void updateProgress(double progress);
}
