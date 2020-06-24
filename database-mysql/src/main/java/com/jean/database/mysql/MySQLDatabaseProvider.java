package com.jean.database.mysql;

import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLDatabaseProvider;
import com.jean.database.sql.SQLMetadataProvider;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Locale;

public class MySQLDatabaseProvider extends SQLDatabaseProvider {

    private final static String ID = "MySQL";

    private final String identifier;
    private final String name;
    private final String icon;
    private final String catalogIcon;
    private final String schemaIcon;
    private final String tableIcon;

    private final MySQLConnectionConfiguration defaultConnectionConfiguration;

    private final SQLMetadataProvider metadataProvider;

    public MySQLDatabaseProvider() {
        this.identifier = ID;
        this.name = ID;
        this.icon = "/mysql/mysql.png";
        this.catalogIcon = "/mysql/catalog.png";
        this.schemaIcon = null;
        this.tableIcon = "/mysql/table.png";
        this.metadataProvider = new MySQLMetadataProvider();
        this.defaultConnectionConfiguration = new MySQLConnectionConfiguration("mysql[mysql.jean.com]", "mysql.jean.com",
                3306, "root", "123456");
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
        try {
            Callback<Class<?>, Object> factory = MySQLConfigurationController.getFactory();
            FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("/fxml/mysql-conn-cfg.fxml", "message.mysql", Locale.SIMPLIFIED_CHINESE, factory);
            Parent parent = loadFxmlResult.getParent();
            MySQLConfigurationController controller = (MySQLConfigurationController) loadFxmlResult.getController();
            controller.setValue(this.defaultConnectionConfiguration);
            Callback<ButtonType, SQLConnectionConfiguration> callback = buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return controller.getValue();
                }
                return null;
            };
            return DialogUtil.customizeDialog("New MySQL connection", parent, callback).orElse(null);
        } catch (IOException e) {
            DialogUtil.error(e);
        }
        return null;
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

    @Override
    public int getOrder() {
        return 10000;
    }
}
