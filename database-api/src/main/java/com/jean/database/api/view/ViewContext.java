package com.jean.database.api.view;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;

public class ViewContext {

    private final MenuBar menuBar;
    private final TreeView databaseTreeView;
    private final TabPane objectTabPan;
    private final TabPane infoPan;

    public ViewContext(MenuBar menuBar, TreeView databaseTreeView, TabPane objectTabPan, TabPane infoPan) {
        this.menuBar = menuBar;
        this.databaseTreeView = databaseTreeView;
        this.objectTabPan = objectTabPan;
        this.infoPan = infoPan;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public TreeView getDatabaseTreeView() {
        return databaseTreeView;
    }

    public TabPane getObjectTabPan() {
        return objectTabPan;
    }

    public TabPane getInfoPan() {
        return infoPan;
    }


    public void addObjectTab(Tab tab) {
        objectTabPan.getTabs().add(tab);
    }

    public void removeObjectTab(Tab tab) {
        objectTabPan.getTabs().remove(tab);
    }

    public void addInfoTab(Tab tab) {
        objectTabPan.getTabs().add(tab);
    }

    public void removeInfoTab(Tab tab) {
        objectTabPan.getTabs().remove(tab);
    }

}
