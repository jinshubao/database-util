package com.jean.database.oracle;

import com.jean.database.core.IConfigurationProvider;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.utils.DialogUtil;
import com.jean.database.core.utils.FxmlUtils;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class OracleConfigurationProvider implements IConfigurationProvider {

    private final Properties defaultProperties;

    private final StringConverter<Properties> propertiesStringConverter;


    public OracleConfigurationProvider() {
        this.defaultProperties = new Properties();
        this.propertiesStringConverter = new PropertiesStringConverter();
    }

    @Override
    public IConnectionConfiguration getConfiguration(Object prams) {
        OracleConnectionConfiguration configuration =
                new OracleConnectionConfiguration("oracle-127.0.0.1", "127.0.0.1", 3306, "root", "", this.defaultProperties);
        return this.getConfiguration(prams, configuration);
    }

    private IConnectionConfiguration getConfiguration(Object prams, OracleConnectionConfiguration initValue) {
        try {
            Parent root = FxmlUtils.loadFxml("/fxml/oracle-conn-cfg.fxml", "message.oracle", Locale.SIMPLIFIED_CHINESE);

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

            Callback<ButtonType, OracleConnectionConfiguration> callback = buttonType -> {
                if (buttonType == ButtonType.OK) {
                    String nameText = nameFiled.getText();
                    String hostText = hostFiled.getText();
                    Integer portText = Integer.valueOf(portFiled.getText());
                    String userText = userFiled.getText();
                    String passwordText = passwordFiled.getText();
                    String propertiesText = propertiesFiled.getText();
                    Properties properties = propertiesStringConverter.fromString(propertiesText);
                    return new OracleConnectionConfiguration(nameText, hostText, portText, userText, passwordText, properties);
                }
                return null;
            };
            return DialogUtil.customizeDialog("New Oracle connection", root, callback).orElse(null);
        } catch (IOException e) {
            DialogUtil.error("ERROR", e.getMessage(), e);
        }
        return null;
    }


    private static class PropertiesStringConverter extends StringConverter<Properties> {
        @Override
        public String toString(Properties properties) {
            //TODO
            return null;
        }

        @Override
        public Properties fromString(String text) {
            //TODO
            return new Properties();
        }
    }
}
