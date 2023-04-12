package com.jean.database.oracle.provider;

import com.jean.database.context.ApplicationContext;
import com.jean.database.oracle.controller.OracleConfigurationController;
import com.jean.database.oracle.config.OracleConnectionConfiguration;
import com.jean.database.provider.DefaultDatabaseProvider;
import com.jean.database.context.ViewContext;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.utils.ImageUtils;
import com.jean.database.sql.SQLConnectionConfiguration;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Properties;

public class OracleDatabaseProvider extends DefaultDatabaseProvider {

    private static final String NAME = "Oracle";
    private String identifier;
    private String name;
    private String catalogIcon;
    private String schemaIcon;
    private String tableIcon;

    private final OracleConnectionConfiguration defaultCollectConfiguration;

    public OracleDatabaseProvider() {

        this.identifier = NAME;
        this.name = NAME;
        this.catalogIcon = "/oracle/catalog.png";
        this.schemaIcon = null;
        this.tableIcon = "/oracle/table.png";
        this.defaultCollectConfiguration = new OracleConnectionConfiguration("oracle-127.0.0.1",
                "127.0.0.1", 3306, "root", "");
    }

    @Override
    public void init(ApplicationContext context) {
        super.init(context);
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/oracle/oracle.png"));
        menuItem.setOnAction(event -> {
            SQLConnectionConfiguration configuration = getConfiguration();
            if (configuration != null) {
//                SQLMetadataProvider metadataProvider = new OracleMetadataProvider(null);
//                TreeItem treeItem = new ServerTreeItem(configuration.getConnectionName(), metadataProvider);
//                getViewContext().addDatabaseItem(treeItem);
            }
        });
        context.getRootContext().addConnectionMenus(menuItem);

    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return name;
    }

    private SQLConnectionConfiguration getConfiguration() {
        try {
            ViewContext viewContext = getContext().getRootContext();
            FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("fxml/oracle-conn-cfg.fxml",
                    "message.oracle",
                    new OracleConfigurationController(getContext(), defaultCollectConfiguration));
            OracleConfigurationController controller = (OracleConfigurationController) loadFxmlResult.getController();
            Callback<ButtonType, OracleConnectionConfiguration> callback = buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return controller.getValue();
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

    @Override
    public int getOrder() {
        return 20000;
    }
}
