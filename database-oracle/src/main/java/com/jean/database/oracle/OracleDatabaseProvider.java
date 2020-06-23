package com.jean.database.oracle;

import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLDatabaseProvider;
import com.jean.database.sql.SQLMetadataProvider;
import javafx.scene.control.ButtonType;
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
            FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("/fxml/oracle-conn-cfg.fxml", "message.oracle", Locale.SIMPLIFIED_CHINESE);


            Callback<ButtonType, OracleConnectionConfiguration> callback = buttonType -> {
                if (buttonType == ButtonType.OK) {

                    return defaultCollectConfiguration;
                }
                return null;
            };
            return DialogUtil.customizeDialog("New Oracle connection", loadFxmlResult.getParent(), callback).orElse(null);
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
