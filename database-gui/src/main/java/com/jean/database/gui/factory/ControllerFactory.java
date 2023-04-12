package com.jean.database.gui.factory;

import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;

public class ControllerFactory implements Callback<Class<?>, Object> {

    @Override
    public Object call(Class<?> param) {
        try {
            return param.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
