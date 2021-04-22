package com.jean.database.api;

public abstract class DefaultController implements IController {

    private ViewContext viewContext;

    public DefaultController(ViewContext viewContext) {
        this.viewContext = viewContext;
    }

    public ViewContext getViewContext() {
        return viewContext;
    }
}
