package com.jean.database.mysql.handler;

import com.jean.database.action.IMouseAction;
import com.jean.database.context.ApplicationContext;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TreeItem;

public interface MySQLServerTreeItemActionEventHandler extends IMouseAction {

    ApplicationContext getContext();

    TreeItem getTreeItem();

    void open();

    void close();

    void create();

    void copy();

    void delete();

    void properties();

    void commandLine();

    void executeSqlFile();

    void dataTransform();

    void refresh();

    BooleanProperty openMenuDisableProperty();
    BooleanProperty closeMenuDisableProperty();
    BooleanProperty createMenuDisableProperty();
    BooleanProperty copyMenuDisableProperty();
    BooleanProperty deleteMenuDisableProperty();
    BooleanProperty propertiesMenuDisableProperty();
    BooleanProperty commandLineMenuDisableProperty();
    BooleanProperty executeSqlFileMenuDisableProperty();
    BooleanProperty dataTransformMenuDisableProperty();
    BooleanProperty refreshMenuDisableProperty();


}
