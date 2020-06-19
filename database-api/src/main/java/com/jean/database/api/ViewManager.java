package com.jean.database.api;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;

import java.util.Iterator;

public class ViewManager {

    public static final String MENU_ID__FILE_MENU = "database_file_menu";
    public static final String MENU_ID__VIEW_MENU = "database_view_menu";
    public static final String MENU_ID__COLLECTION_MENU = "database_collection_menu";
    public static final String MENU_ID__TOOLS_MENU = "database_tools_menu";
    public static final String MENU_ID__WINDOW_MENU = "database_window_menu";
    public static final String MENU_ID__HELP_MENU = "database_help_menu";

    private static MenuBar menuBar;
    private static TreeView databaseTreeView;
    private static TabPane objectTabPan;
    private static TabPane infoTabPane;

    private static boolean init = false;


    public static synchronized void init(MenuBar menuBar, TreeView databaseTreeView, TabPane objectTabPan, TabPane infoTabPane) {
        if (!init) {
            ViewManager.menuBar = menuBar;
            ViewManager.databaseTreeView = databaseTreeView;
            ViewManager.objectTabPan = objectTabPan;
            ViewManager.infoTabPane = infoTabPane;
            init = true;
        }
    }

    public static void addMenu(String menuId, Menu... menus) {
        Menu menu1 = getMenu(menuId);
        if (menu1 != null) {
            menu1.getItems().addAll(menus);
        }
    }

    public static Menu getMenu(String menuId) {
        Iterator<Menu> iterator = menuBar.getMenus().iterator();
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            if (menu.getId().equals(menuId)) {
                return menu;
            }
        }
        return null;
    }

    public static TreeView getDatabaseTreeView() {
        return databaseTreeView;
    }

    public static TabPane getObjectTabPan() {
        return objectTabPan;
    }

    public static TabPane getInfoTabPane() {
        return infoTabPane;
    }

}
