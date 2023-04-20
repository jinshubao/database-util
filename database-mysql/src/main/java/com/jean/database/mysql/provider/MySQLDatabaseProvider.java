package com.jean.database.mysql.provider;

import com.jean.database.context.ApplicationContext;
import com.jean.database.mysql.config.MySQLConnectionConfiguration;
import com.jean.database.mysql.controller.MySQLConfigurationController;
import com.jean.database.mysql.handler.DefaultMySQLServerTreeItemActionEventHandler;
import com.jean.database.mysql.view.item.MySQLServerTreeItem;
import com.jean.database.provider.DefaultDatabaseProvider;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.utils.ImageUtils;
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
                3306, "root", "12345678");
    }

    @Override
    public void init(ApplicationContext context) {
        super.init(context);
        initMenus();
    }

    private void initMenus() {
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/mysql/mysql.png"));
        menuItem.setOnAction(event -> {
            MySQLConnectionConfiguration configuration = getConnectionConfiguration();
            if (configuration != null) {
                MySQLServerTreeItem treeItem = new MySQLServerTreeItem(configuration);
                DefaultMySQLServerTreeItemActionEventHandler eventHandler = new DefaultMySQLServerTreeItemActionEventHandler(getContext(), treeItem);
                treeItem.setItemActionHandler(eventHandler);
                getContext().addDatabaseItem(treeItem);
            }
        });
        getContext().addConnectionMenus(menuItem);
    }


    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public MySQLConnectionConfiguration getConnectionConfiguration() {
        try {
            FxmlUtils.LoadFxmlResult loadFxmlResult =
                    FxmlUtils.loadFxml("fxml/mysql-conn-cfg.fxml", "message.mysql", new MySQLConfigurationController(getContext()));
            Parent parent = loadFxmlResult.getParent();
            MySQLConfigurationController controller = (MySQLConfigurationController) loadFxmlResult.getController();
            controller.setValue(this.defaultConnectionConfiguration);
            Callback<ButtonType, MySQLConnectionConfiguration> callback = buttonType -> {
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
