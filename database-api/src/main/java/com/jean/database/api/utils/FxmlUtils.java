package com.jean.database.api.utils;

import com.jean.database.api.EncodingResourceBundleControl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public final class FxmlUtils {

    public static Parent loadFxml(String name, String resource, Locale locale) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        if (resource != null) {
            loader.setResources(ResourceBundle.getBundle(resource, locale, new EncodingResourceBundleControl()));
        }
        loader.setControllerFactory(param -> {
            try {
                return param.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return loader.load(FxmlUtils.class.getResourceAsStream(name));
    }
}
