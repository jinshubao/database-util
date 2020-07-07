package com.jean.database.mysql;

import com.jean.database.api.IDatabaseProvider;
import com.jean.database.api.ViewManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.mysql.view.MySQLServerTreeItem;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Locale;

public class MySQLDatabaseProvider implements IDatabaseProvider {

    private final static String NAME = "MySQL";

    private final String identifier;
    private final String name;

    private final MySQLConnectionConfiguration defaultConnectionConfiguration;

    public MySQLDatabaseProvider() {
        this.identifier = NAME;
        this.name = NAME;
        this.defaultConnectionConfiguration = new MySQLConnectionConfiguration("mysql[127.0.0.1]", "127.0.0.1",
                3306, "root", "123456");
    }

    @Override
    public void init() {
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/mysql/mysql.png"));
        menuItem.setOnAction(event -> {
            SQLConnectionConfiguration configuration = getConnectionConfiguration();
            if (configuration != null) {
                HikariDataSource dataSource = new HikariDataSource();
                String url = configuration.getUrl();
                dataSource.setJdbcUrl(url);
                dataSource.setUsername(configuration.getUsername());
                dataSource.setPassword(configuration.getPassword());
                dataSource.setMaximumPoolSize(2);
                dataSource.setMaximumPoolSize(1);
                MySQLMetadataProvider metadataProvider = new MySQLMetadataProvider(dataSource);
                MySQLServerTreeItem treeItem = new MySQLServerTreeItem(configuration.getConnectionName(), metadataProvider);
                ViewManger.getViewContext().addDatabaseItem(treeItem);
            }
        });
        ViewManger.getViewContext().addConnectionMenus(menuItem);
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
    public int getOrder() {
        return 10000;
    }
}
