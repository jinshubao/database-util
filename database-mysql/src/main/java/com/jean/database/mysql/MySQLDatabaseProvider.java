package com.jean.database.mysql;

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

public class MySQLDatabaseProvider extends SQLDatabaseProvider {

    private final static String ID = "MySQL";

    private String identifier;
    private String name;
    private String icon;
    private String catalogIcon;
    private String schemaIcon;
    private String tableIcon;

    private final SQLMetadataProvider metadataProvider;

    public MySQLDatabaseProvider() {
        this.identifier = ID;
        this.name = ID;
        this.icon = "/mysql/mysql.png";
        this.catalogIcon = "/mysql/catalog.png";
        this.schemaIcon = null;
        this.tableIcon = "/mysql/table.png";
        this.metadataProvider = new MySQLMetadataProvider();
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
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getIcon() {
        return this.icon;
    }

    @Override
    public SQLConnectionConfiguration getConnectionConfiguration() {
        return this.getConfiguration(new MySQLConnectionConfiguration("mysql[mysql.jean.com]", "mysql.jean.com",
                3306, "root", "123456"));
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


    private SQLConnectionConfiguration getConfiguration(MySQLConnectionConfiguration initValue) {
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

            StringConverter<Properties> converter = new MySQLPropertiesConverter();

            String propString = converter.toString(initValue.getProperties());
            TextField propertiesFiled = (TextField) root.lookup("#properties");
            propertiesFiled.setText(propString);

            Callback<ButtonType, SQLConnectionConfiguration> callback = buttonType -> {
                if (buttonType == ButtonType.OK) {
                    String nameText = nameFiled.getText();
                    String hostText = hostFiled.getText();
                    Integer portText = Integer.valueOf(portFiled.getText());
                    String userText = userFiled.getText();
                    String passwordText = passwordFiled.getText();
                    String propertiesText = propertiesFiled.getText();
                    Properties properties = converter.fromString(propertiesText);
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

}
