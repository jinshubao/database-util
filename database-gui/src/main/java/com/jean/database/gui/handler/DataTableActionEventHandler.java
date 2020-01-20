package com.jean.database.gui.handler;

import com.jean.database.gui.view.CustomTableView;

public interface DataTableActionEventHandler extends IRefresh<CustomTableView> {

    void refresh(CustomTableView customTableView, int page);
}
