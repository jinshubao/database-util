package com.jean.database.mysql.handler;

import com.jean.database.ability.IRefreshable;
import com.jean.database.action.IMouseAction;
import javafx.beans.property.BooleanProperty;

public interface MySQLTableTreeItemActionEventHandler extends IMouseAction, IRefreshable {
    void open();

    void copy();

    void delete();
    void close();

    BooleanProperty openMenuDisableProperty();

    BooleanProperty copyMenuDisableProperty();

    BooleanProperty deleteMenuDisableProperty();

    BooleanProperty refreshMenuDisableProperty();

}
