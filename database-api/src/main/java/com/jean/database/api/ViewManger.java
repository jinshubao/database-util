package com.jean.database.api;

public class ViewManger {

    private static ViewContext viewContext;

    private ViewManger() {
    }

    public static synchronized void init(ViewContext viewContext) {
        if (ViewManger.viewContext == null) {
            ViewManger.viewContext = viewContext;
        }
    }

    public static ViewContext getViewContext() {
        return viewContext;
    }
}
