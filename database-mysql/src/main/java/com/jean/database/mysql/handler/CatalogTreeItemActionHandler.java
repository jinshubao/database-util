package com.jean.database.mysql.handler;

import com.jean.database.ability.IRefreshable;
import com.jean.database.context.ApplicationContext;
import com.jean.database.handler.AbstractActionHandler;

public class CatalogTreeItemActionHandler extends AbstractActionHandler implements IRefreshable {
    public CatalogTreeItemActionHandler(ApplicationContext context) {
        super(context);
    }

    @Override
    public void refresh() {

    }
}
