package com.jean.database.api;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class EncodingResourceBundleControl extends ResourceBundle.Control {

    private final Charset encoding;

    public EncodingResourceBundleControl() {
        this(StandardCharsets.UTF_8);
    }

    public EncodingResourceBundleControl(Charset encoding) {
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

    public Charset getEncoding() {
        return encoding;
    }
}
