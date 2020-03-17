package com.jean.database.mysql;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IDatabaseProvider;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.common.utils.DialogUtil;
import com.jean.database.common.utils.FxmlUtils;
import com.jean.database.common.utils.StringUtil;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MySQLDatabaseProvider implements IDatabaseProvider {

    private final static String ID = "MySQL";

    private String identifier;
    private String name;
    private String icon;
    private String catalogIcon;
    private String schemaIcon;
    private String tableIcon;


    private final Properties defaultProperties;
    private final StringConverter<Properties> propertiesStringConverter;


    public MySQLDatabaseProvider() {
        this.identifier = ID;
        this.name = ID;
        this.icon = "/mysql/mysql.png";
        this.catalogIcon = "/mysql/catalog.png";
        this.schemaIcon = null;
        this.tableIcon = "/mysql/table.png";

        this.defaultProperties = new Properties();
        this.defaultProperties.put("characterEncoding", "UTF-8");
        this.defaultProperties.put("allowMultiQueries", "true");
        this.defaultProperties.put("zeroDateTimeBehavior", "convertToNull");
        this.defaultProperties.put("serverTimezone", "UTC");
        this.defaultProperties.put("useSSL", "false");
        this.defaultProperties.put("remarks", "true");
        this.defaultProperties.put("useInformationSchema", "true");
        this.propertiesStringConverter = new PropertiesStringConverter();

    }


    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIcon() {
        return this.icon;
    }

    @Override
    public String getCatalogIcon() {
        return this.catalogIcon;
    }

    @Override
    public String getSchemaIcon() {
        return schemaIcon;
    }

    @Override
    public String getTableIcon() {
        return this.tableIcon;
    }


    @Override
    public IConnectionConfiguration getConfiguration() {
        MySQLConnectionConfiguration configuration =
                new MySQLConnectionConfiguration("mysql[mysql.jean.com]", "mysql.jean.com", 33060, "root", "123456", this.defaultProperties);
        return this.getConfiguration( configuration);
    }

    @Override
    public IMetadataProvider getMetadataProvider() {
        return new MySQLMetadataProvider();
    }


    @Override
    public boolean supportCatalog() {
        return true;
    }

    @Override
    public boolean supportSchema() {
        return false;
    }


    private IConnectionConfiguration getConfiguration(MySQLConnectionConfiguration initValue) {
        try {
            Parent root = FxmlUtils.loadFxml("/fxml/mysql-conn-cfg.fxml", "message.mysql", Locale.SIMPLIFIED_CHINESE);

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

            Callback<ButtonType, MySQLConnectionConfiguration> callback = buttonType -> {
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
            };
            return DialogUtil.customizeDialog("New MySQL connection", root, callback).orElse(null);
        } catch (IOException e) {
            DialogUtil.error(e);
        }
        return null;
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
