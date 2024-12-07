package com.jean.database.api.utils;

import com.jean.database.api.EncodingResourceBundleControl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

public final class FxmlUtils {

    public static LoadFxmlResult loadFxml(String name) throws IOException {
        return loadFxml(name, null);
    }

    public static LoadFxmlResult loadFxml(String name, String resource) throws IOException {
        Callback<Class<?>, Object> factory = param -> {
            try {
                return param.getConstructor().newInstance();
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

        InputStream stream = FXMLLoader.getDefaultClassLoader().getResourceAsStream(name);
        if (stream == null) {
            stream = FXMLLoader.getDefaultClassLoader().getResourceAsStream(File.pathSeparator + name);
        }
        if (stream == null) {
            throw new RuntimeException("资源文件[" + name + "]不存在");
        }

        Parent parent = loader.load(stream);
        Object controller = loader.getController();
        return new LoadFxmlResult(parent, controller);
    }

    public record LoadFxmlResult(Parent parent, Object controller) {
    }
}
