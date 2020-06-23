package com.jean.database.api;


import javafx.util.Callback;

public abstract class AbstractControllerFactory implements Callback<Class<?>, Object> {

    private final ViewContext viewContext;

    protected AbstractControllerFactory(ViewContext viewContext) {
        this.viewContext = viewContext;
    }

    public ViewContext getViewContext() {
        return viewContext;
    }
}
