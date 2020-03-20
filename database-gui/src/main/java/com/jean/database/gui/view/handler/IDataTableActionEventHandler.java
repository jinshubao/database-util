package com.jean.database.gui.view.handler;

import com.jean.database.gui.view.DataTableTab;

public interface IDataTableActionEventHandler extends IRefreshActionEventHandler<DataTableTab> {

    void refresh(DataTableTab dataTableView, int page);
}
