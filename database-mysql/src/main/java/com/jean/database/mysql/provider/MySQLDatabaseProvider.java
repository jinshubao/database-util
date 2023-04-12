package com.jean.database.mysql.provider;

import com.alibaba.druid.pool.DruidDataSource;
import com.jean.database.context.ApplicationContext;
import com.jean.database.mysql.config.MySQLConnectionConfiguration;
import com.jean.database.mysql.controller.MySQLConfigurationController;
import com.jean.database.provider.DefaultDatabaseProvider;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.utils.ImageUtils;
import com.jean.database.mysql.view.MySQLServerTreeItem;
import com.jean.database.sql.SQLConnectionConfiguration;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

import java.io.IOException;

public class MySQLDatabaseProvider extends DefaultDatabaseProvider {

    private final static String NAME = "MySQL";

    private final String identifier;
    private final String name;

    private final MySQLConnectionConfiguration defaultConnectionConfiguration;

    public MySQLDatabaseProvider() {
        this.identifier = NAME;
        this.name = NAME;
        this.defaultConnectionConfiguration = new MySQLConnectionConfiguration("mysql[127.0.0.1]", "127.0.0.1",
                3307, "root", "123456");
    }

    @Override
    public void init(ApplicationContext context) {
        super.init(context);
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/mysql/mysql.png"));
        menuItem.setOnAction(event -> {
            SQLConnectionConfiguration configuration = getConnectionConfiguration();
            if (configuration != null) {
                DruidDataSource dataSource = new DruidDataSource();
                String url = configuration.getUrl();
                dataSource.setUrl(url);
                dataSource.setUsername(configuration.getUsername());
                dataSource.setPassword(configuration.getPassword());
                dataSource.setMaxActive(2);
                dataSource.setMinIdle(1);
                dataSource.setLoginTimeout(30);
                dataSource.setQueryTimeout(60);
                MySQLMetadataProvider metadataProvider = new MySQLMetadataProvider(dataSource);
                context.getRootContext().addDatabaseItem(new MySQLServerTreeItem(context, metadataProvider,configuration.getConnectionName()));
            }
        });
        context.getRootContext().addConnectionMenus(menuItem);
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
            FxmlUtils.LoadFxmlResult loadFxmlResult =
                    FxmlUtils.loadFxml("fxml/mysql-conn-cfg.fxml", "message.mysql", new MySQLConfigurationController(getContext()));
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
