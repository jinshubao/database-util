package com.jean.database.gui.handler;

import com.jean.database.gui.view.CustomTableView;

public interface IDataTableActionEventHandler extends IRefreshActionEventHandler<CustomTableView> {

    void refresh(CustomTableView customTableView, int page);
}
