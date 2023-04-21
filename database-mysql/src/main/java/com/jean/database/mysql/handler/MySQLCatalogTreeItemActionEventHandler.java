package com.jean.database.mysql.handler;

import com.jean.database.ability.IRefreshable;
import com.jean.database.action.IMouseAction;
import javafx.beans.property.BooleanProperty;

public interface MySQLCatalogTreeItemActionEventHandler extends IMouseAction, IRefreshable {


    void newQuery();

    void open();

    void close();

    void newDatabase();

    void delete();

    void properties();

    void commandLine();

    void executeSqlFile();

    void exportStructAndData();

    void exportStruct();

    void printDatabase();

    void dataTransform();

    void convertDatabaseToMode();

    void findInDatabase();

    BooleanProperty openMenuDisableProperty();

    BooleanProperty queryMenuDisableProperty();

    BooleanProperty createMenuDisableProperty();

    BooleanProperty deleteMenuDisableProperty();

    BooleanProperty propertiesMenuDisableProperty();

    BooleanProperty commandLineMenuDisableProperty();

    BooleanProperty executeSqlFileDisableProperty();

    BooleanProperty exportStructAndDataMenuDisableProperty();

    BooleanProperty exportStructMenuDisableProperty();

    BooleanProperty printDatabaseMenuDisableProperty();

    BooleanProperty dataTransformMenuDisableProperty();

    BooleanProperty convertDatabaseToModeMenuDisableProperty();

    BooleanProperty findInDatabaseMenuDisableProperty();

    BooleanProperty refreshMenuDisableProperty();
}
