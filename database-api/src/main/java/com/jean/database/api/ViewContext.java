package com.jean.database.api;

import javafx.scene.Node;
import javafx.scene.control.*;

@SuppressWarnings({"rawtypes"})
public interface ViewContext {

    public static final String MENU_ID__FILE_MENU = "database_file_menu";
    public static final String MENU_ID__VIEW_MENU = "database_view_menu";
    public static final String MENU_ID__COLLECTION_MENU = "database_collection_menu";
    public static final String MENU_ID__TOOLS_MENU = "database_tools_menu";
    public static final String MENU_ID__WINDOW_MENU = "database_window_menu";
    public static final String MENU_ID__HELP_MENU = "database_help_menu";

    void addObjectTab(Tab tab);

    void removeObjectTab(Tab tab);

    void addDatabaseItem(TreeItem treeItem);

    void addConnectionMenus(MenuItem... menu);

    void addViewMenus(MenuItem... menu);

    void updateProgress(double progress);
}
