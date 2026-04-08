package com.jean.database.mysql;

import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultDatabaseProvider;
import com.jean.database.api.FxmlControllerFactory;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.mysql.view.MySQLServerTreeItem;
import com.jean.database.sql.SQLConnectionConfiguration;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;

public class MySQLDatabaseProvider extends DefaultDatabaseProvider {

    private static final Logger logger = LoggerFactory.getLogger(MySQLDatabaseProvider.class);

    private final static String NAME = "MySQL";

    private final String identifier;
    private final String name;

    private final MySQLConnectionConfiguration defaultConnectionConfiguration;

    public MySQLDatabaseProvider() {
        this.identifier = NAME;
        this.name = NAME;
        this.defaultConnectionConfiguration = new MySQLConnectionConfiguration("mysql[192.168.1.15]", "192.168.1.15",
                3306, "root", "12345678");
    }

    @Override
    public void init() {
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/mysql/mysql.png"));
        menuItem.setOnAction(event -> {
            SQLConnectionConfiguration configuration = getConnectionConfiguration();
            if (configuration != null) {
                try {
                    DataSource dataSource = DataSourceManager.createDataSource(configuration);
                    // 测试连接
                    if (DataSourceManager.testConnection(dataSource)) {
                        getViewContext().addDatabaseItem(new MySQLServerTreeItem(getViewContext(), configuration.getConnectionName(), new MySQLMetadataProvider(dataSource)));
                    } else {
                        DialogUtil.information("连接测试", "连接测试", "连接测试失败");
                    }
                } catch (Exception e) {
                    com.jean.database.mysql.exception.MySQLExceptionHandler.handleConnectionException(e);
                }
            }
        });
        getViewContext().addConnectionMenus(menuItem);
    }


    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public SQLConnectionConfiguration getConnectionConfiguration() {
        try {
            ControllerContext context = ControllerContext.builder(getViewContext(), "MySQL Configuration")
                    .attribute(MySQLConfigurationController.ATTR_DEFAULT_CONFIGURATION, this.defaultConnectionConfiguration)
                    .build();

            FxmlControllerFactory.LoadResult<MySQLConfigurationController> result =
                    FxmlControllerFactory.load("fxml/mysql-conn-cfg.fxml", "message.mysql", context, MySQLConfigurationController::new);

            Parent parent = result.getParent();
            MySQLConfigurationController controller = result.getController();
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
    public int getOrder() {
        return 10000;
    }
}
