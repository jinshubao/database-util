package com.jean.database.api;

import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ControllerFactory {

    private static final Logger logger = LoggerFactory.getLogger(ControllerFactory.class);

    public static Callback<Class<?>, Object> getFactory(Class<?> controllerClass, Object... initargs) {
        if (initargs == null) {
            return param -> {
                try {
                    return controllerClass.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e.getMessage(), e);
                }
            };
        }
        Class<?>[] types = new Class[initargs.length];
        for (int i = 0; i < initargs.length; i++) {
            types[i] = initargs[i].getClass();
        }
        return param -> {
            try {
                Constructor<?> constructor = controllerClass.getConstructor(types);
                return constructor.newInstance(initargs);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        };
    }
}
