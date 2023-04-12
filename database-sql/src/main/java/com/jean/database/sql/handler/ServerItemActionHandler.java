package com.jean.database.sql.handler;

import com.jean.database.ability.ICloseable;
import com.jean.database.action.IContextMenu;
import com.jean.database.action.IMouseAction;
import com.jean.database.ability.IRefreshable;

public interface ServerItemActionHandler extends IContextMenu, IMouseAction, IRefreshable, ICloseable {
}
