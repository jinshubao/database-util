package com.jean.database.mysql;

import com.jean.database.api.ViewContext;
import javafx.util.Callback;

public class MySQLConfigurationControllerFactory implements Callback<Class<?>, Object> {

    private final ViewContext viewContext;

    public MySQLConfigurationControllerFactory(ViewContext viewContext) {
        this.viewContext = viewContext;
    }

    @Override
    public Object call(Class<?> param) {
        return new MySQLConfigurationController(viewContext);
    }
}
