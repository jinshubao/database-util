package com.jean.database.redis;

import com.jean.database.api.AbstractDatabaseProvider;
import com.jean.database.api.ViewContext;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.redis.view.RedisServerItem;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

import java.util.Locale;

public class RedisDatabaseProvider extends AbstractDatabaseProvider {

    private final RedisConnectionConfiguration defaultCollectConfiguration;

    public RedisDatabaseProvider() {
        String host = "101.132.156.127";
        this.defaultCollectConfiguration = new RedisConnectionConfiguration("Redis[" + host + "]", host, 6379, "123!=-09][po");
    }

    @Override
    public void init(ViewContext context) {
        super.init(context);
        MenuBar menuBar = context.getMenuBar();
        Menu fileMenu = menuBar.getMenus().get(0);
        Menu menu = (Menu) fileMenu.getItems().get(0);
        MenuItem menuItem = new MenuItem(getName());
        menu.getItems().add(menuItem);
        menuItem.setOnAction(event -> {
            RedisConnectionConfiguration configuration = this.getConnectionConfiguration();
            if (configuration != null) {
                context.addDatabaseItem(new RedisServerItem(configuration, context));
            }
        });
    }

    @Override
    public String getIdentifier() {
        return "Redis";
    }

    @Override
    public String getName() {
        return "Redis";
    }

    private RedisConnectionConfiguration getConnectionConfiguration() {
        try {
            FxmlUtils.LoadFxmlResult fxmlResult = FxmlUtils.loadFxml("/fxml/redis-conn-cfg.fxml", null, Locale.SIMPLIFIED_CHINESE);
            RedisConnectionConfigurationController cfgController = (RedisConnectionConfigurationController) fxmlResult.getController();
            cfgController.setValue(defaultCollectConfiguration);
            Callback<ButtonType, RedisConnectionConfiguration> callback = buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return cfgController.getValue();
                }
                return null;
            };
            return DialogUtil.customizeDialog("New Redis connection", fxmlResult.getParent(), callback).orElse(null);
        } catch (Exception e) {
            DialogUtil.error(e);
        }
        return null;
    }
}
