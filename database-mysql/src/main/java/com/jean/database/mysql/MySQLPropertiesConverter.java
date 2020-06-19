package com.jean.database.mysql;

import com.jean.database.api.utils.StringUtils;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MySQLPropertiesConverter extends StringConverter<Properties> {

    @Override
    public String toString(Properties properties) {
        if (properties != null && properties.size() > 0) {
            List<String> props = new ArrayList<>(properties.size());
            properties.forEach((key, value) -> props.add(key + "=" + value));
            return StringUtils.join(props, "&");
        }
        return null;
    }

    @Override
    public Properties fromString(String text) {
        Properties properties = new Properties();
        if (StringUtils.isNotBlank(text)) {
            String[] split = text.split("&");
            for (String str : split) {
                if (str.contains("=")) {
                    String[] kv = str.split("=");
                    if (kv.length == 2) {
                        properties.put(kv[0], kv[1]);
                    }
                }
            }
        }
        return properties;
    }
}
