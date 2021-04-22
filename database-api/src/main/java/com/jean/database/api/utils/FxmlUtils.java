package com.jean.database.api.utils;

import com.jean.database.api.EncodingResourceBundleControl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public final class FxmlUtils {

    public static LoadFxmlResult loadFxml(String name) throws IOException {
        Callback<Class<?>, Object> factory = param -> {
            try {
                return param.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return loadFxml(name, null, Locale.SIMPLIFIED_CHINESE,factory);
    }

    public static LoadFxmlResult loadFxml(String name, String resource) throws IOException {
        Callback<Class<?>, Object> factory = param -> {
            try {
                return param.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return loadFxml(name, resource, Locale.SIMPLIFIED_CHINESE, factory);
    }

    public static LoadFxmlResult loadFxml(String name, String resource, Object controller) throws IOException {
        return loadFxml(name, resource, Locale.SIMPLIFIED_CHINESE, param -> controller);
    }

    public static LoadFxmlResult loadFxml(String name, String resource, Locale locale, Callback<Class<?>, Object> factory) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        if (resource != null && !resource.isEmpty()) {
            loader.setResources(ResourceBundle.getBundle(resource, locale, new EncodingResourceBundleControl()));
        }
        loader.setControllerFactory(factory);
        Parent parent = loader.load(FxmlUtils.class.getResourceAsStream(name));
        Object controller = loader.getController();
        return new LoadFxmlResult(parent, controller);
    }

    public static class LoadFxmlResult {
        private final Parent parent;
        private final Object controller;

        public LoadFxmlResult(Parent parent, Object controller) {
            this.parent = parent;
            this.controller = controller;
        }

        public Parent getParent() {
            return parent;
        }

        public Object getController() {
            return controller;
        }
    }
}
