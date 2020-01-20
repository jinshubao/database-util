package com.jean.database.core.mysql;

import com.jean.database.core.IConfigurationProvider;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.utils.FxmlUtils;
import com.jean.database.core.utils.StringUtil;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MySQLConfigurationProvider implements IConfigurationProvider {

    private final Properties defaultProperties;

    private final StringConverter<Properties> propertiesStringConverter;


    public MySQLConfigurationProvider() {
        this.defaultProperties = new Properties();
        this.defaultProperties.put("characterEncoding", "UTF-8");
        this.defaultProperties.put("allowMultiQueries", "true");
        this.defaultProperties.put("zeroDateTimeBehavior", "convertToNull");
        this.defaultProperties.put("serverTimezone", "UTC");
        this.defaultProperties.put("useSSL", "false");
        this.propertiesStringConverter = new PropertiesStringConverter();
    }

    @Override
    public IConnectionConfiguration getConfiguration(Object prams) {
        MySQLConnectionConfiguration configuration =
                new MySQLConnectionConfiguration("mysql-127.0.0.1", "127.0.0.1", 3306, "root", "", this.defaultProperties);
        return this.getConfiguration(prams, configuration);
    }

    public IConnectionConfiguration getConfiguration(Object prams, MySQLConnectionConfiguration initValue) {
        final Parent root;
        try {
            root = FxmlUtils.loadFxml("/fxml/mysql-conn-cfg.fxml", "message.connection", Locale.SIMPLIFIED_CHINESE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Dialog<MySQLConnectionConfiguration> dialog = new Dialog<>();
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(root);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nameFiled = (TextField) root.lookup("#name");
        nameFiled.setText(initValue.getConnectionName());

        TextField hostFiled = (TextField) root.lookup("#host");
        hostFiled.setText(initValue.getHost());

        TextField portFiled = (TextField) root.lookup("#port");
        portFiled.setText(String.valueOf(initValue.getPort()));

        TextField userFiled = (TextField) root.lookup("#user");
        userFiled.setText(initValue.getUser());

        TextField passwordFiled = (TextField) root.lookup("#password");
        passwordFiled.setText(initValue.getPassword());

        String propString = propertiesStringConverter.toString(initValue.getProperties());
        TextField propertiesFiled = (TextField) root.lookup("#properties");
        propertiesFiled.setText(propString);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String nameText = nameFiled.getText();
                String hostText = hostFiled.getText();
                Integer portText = Integer.valueOf(portFiled.getText());
                String userText = userFiled.getText();
                String passwordText = passwordFiled.getText();
                String propertiesText = propertiesFiled.getText();
                Properties properties = propertiesStringConverter.fromString(propertiesText);
                return new MySQLConnectionConfiguration(nameText, hostText, portText, userText, passwordText, properties);
            }
            return null;
        });
        return dialog.showAndWait().orElse(null);
    }


    private static class PropertiesStringConverter extends StringConverter<Properties> {
        @Override
        public String toString(Properties properties) {
            if (properties != null && properties.size() > 0) {
                List<String> props = new ArrayList<>(properties.size());
                properties.forEach((key, value) -> props.add(key + "=" + value));
                return StringUtil.join(props, "&");
            }
            return null;
        }

        @Override
        public Properties fromString(String text) {
            Properties properties = new Properties();
            if (StringUtil.isNotBlank(text)) {
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
}
