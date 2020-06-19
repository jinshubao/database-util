package com.jean.database.oracle;

import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLDatabaseProvider;
import com.jean.database.sql.SQLMetadataProvider;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class OracleDatabaseProvider extends SQLDatabaseProvider {

    private static final String ID = "Oracle";
    private String identifier;
    private String name;

    private String icon;
    private String catalogIcon;
    private String schemaIcon;
    private String tableIcon;

    private final Properties defaultProperties;

    private final StringConverter<Properties> propertiesStringConverter;

    private final SQLMetadataProvider metadataProvider;

    private final OracleConnectionConfiguration defaultCollectConfiguration;

    public OracleDatabaseProvider() {

        this.identifier = ID;
        this.name = name = ID;
        this.icon = "/oracle/oracle.png";
        this.catalogIcon = "/oracle/catalog.png";
        this.schemaIcon = null;
        this.tableIcon = "/oracle/table.png";

        this.propertiesStringConverter = new PropertiesStringConverter();

        this.defaultProperties = new Properties();

        this.defaultCollectConfiguration = new OracleConnectionConfiguration("oracle-127.0.0.1",
                "127.0.0.1", 3306, "root", "", this.defaultProperties);

        this.metadataProvider = new OracleMetadataProvider();
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
    public String getTableIcon() {
        return this.tableIcon;
    }

    @Override
    public SQLConnectionConfiguration getConnectionConfiguration() {
        return this.getConfiguration(this.defaultCollectConfiguration);
    }

    @Override
    public SQLMetadataProvider getMetadataProvider() {
        return this.metadataProvider;
    }

    @Override
    public boolean supportCatalog() {
        return true;
    }

    @Override
    public boolean supportSchema() {
        return false;
    }

    private SQLConnectionConfiguration getConfiguration(OracleConnectionConfiguration initValue) {
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
            DialogUtil.error(e);
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
