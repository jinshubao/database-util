package com.jean.database.api;

public abstract class DefaultDatabaseProvider implements IDatabaseProvider {

    private ViewContext viewContext;

    @Override
    public void setViewContext(ViewContext viewContext) {
        this.viewContext = viewContext;
    }

    @Override
    public ViewContext getViewContext() {
        return viewContext;
    }
}
