package com.jean.database.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public final class FxmlUtils {

    public static Parent loadFxml(String name, String resource, Locale locale) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        if (resource != null) {
            loader.setResources(ResourceBundle.getBundle(resource, locale, new EncodingResourceBundleControl(StandardCharsets.UTF_8)));
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


    /**
     * ResourceBundle 编码
     */
    private static class EncodingResourceBundleControl extends ResourceBundle.Control {

        private final Charset encoding;

        private EncodingResourceBundleControl(Charset encoding) {
            this.encoding = encoding;
        }

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            URL resourceURL = loader.getResource(resourceName);
            if (resourceURL != null) {
                return new PropertyResourceBundle(new InputStreamReader(resourceURL.openStream(), encoding));
            }
            return super.newBundle(baseName, locale, format, loader, reload);
        }
    }
}
