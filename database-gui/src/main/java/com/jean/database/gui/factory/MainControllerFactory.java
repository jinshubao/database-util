package com.jean.database.gui.factory;

import com.jean.database.context.ApplicationContext;
import com.jean.database.gui.controller.MainController;
import javafx.util.Callback;

public class MainControllerFactory implements Callback<Class<?>, Object> {

    private final ApplicationContext applicationContext;

    public MainControllerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public Object call(Class<?> param) {
        return new MainController(applicationContext);
    }
}
