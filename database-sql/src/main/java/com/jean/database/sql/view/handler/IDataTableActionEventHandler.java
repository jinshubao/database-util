package com.jean.database.sql.view.handler;

import com.jean.database.api.view.handler.IRefreshActionEventHandler;
import com.jean.database.sql.view.SQLDataTableTab;

public interface IDataTableActionEventHandler extends IRefreshActionEventHandler<SQLDataTableTab> {

    void refresh(SQLDataTableTab dataTableView, int page);
}
