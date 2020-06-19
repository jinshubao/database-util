package com.jean.database.redis;

import com.jean.database.api.AbstractDatabaseProvider;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.view.ViewContext;
import com.jean.database.redis.view.RedisServerItem;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class RedisDatabaseProvider extends AbstractDatabaseProvider {

    private final IRedisMetadataProvider metadataProvider;
    private final RedisConnectionConfiguration defaultCollectConfiguration;

    public RedisDatabaseProvider() {
        String host = "101.132.156.127";
        this.defaultCollectConfiguration = new RedisConnectionConfiguration("Redis[" + host + "]", host, 6379, "123!=-09][po");
        this.metadataProvider = new RedisMetadataProvider();
    }

    @Override
    public void init(ViewContext context) {
        super.init(context);
        MenuBar menuBar = context.getMenuBar();
        Menu fileMenu = menuBar.getMenus().get(0);
        Menu menu = (Menu) fileMenu.getItems().get(0);
        MenuItem menuItem = new MenuItem(getName());
        menuItem.setOnAction(event -> {
            RedisConnectionConfiguration configuration = this.getConnectionConfiguration();
            if (configuration != null) {
                IRedisMetadataProvider metadataProvider = this.getMetadataProvider();
                RedisServerItem treeItem = new RedisServerItem(context, configuration, metadataProvider);
                context.getDatabaseTreeView().getRoot().getChildren().add(treeItem);
            }
        });
        menu.getItems().add(menuItem);
    }

    @Override
    public String getIdentifier() {
        return "Redis";
    }

    @Override
    public String getName() {
        return "Redis";
    }

    public IRedisMetadataProvider getMetadataProvider() {
        return this.metadataProvider;
    }

    public RedisConnectionConfiguration getConnectionConfiguration() {

        Label nameLabel = new Label("name:");
        TextField nameFiled = new TextField(this.defaultCollectConfiguration.getConnectionName());

        Label hostLabel = new Label("host:");
        TextField hostFiled = new TextField(this.defaultCollectConfiguration.getHost());
        Label portLabel = new Label("port:");
        TextField portFiled = new TextField(this.defaultCollectConfiguration.getPort().toString());
        Label passwordLabel = new Label("password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setText(this.defaultCollectConfiguration.getPassword());

        hostFiled.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(hostFiled, Priority.ALWAYS);
        GridPane.setFillWidth(hostFiled, true);

        portFiled.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(portFiled, Priority.ALWAYS);
        GridPane.setFillWidth(portFiled, true);

        passwordField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(passwordField, Priority.ALWAYS);
        GridPane.setFillWidth(passwordField, true);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0, 10, 0, 10));
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameFiled, 1, 0);
        gridPane.add(hostLabel, 0, 1);
        gridPane.add(hostFiled, 1, 1);
        gridPane.add(portLabel, 0, 2);
        gridPane.add(portFiled, 1, 2);
        gridPane.add(passwordLabel, 0, 3);
        gridPane.add(passwordField, 1, 3);
        Platform.runLater(hostFiled::requestFocus);


        Callback<ButtonType, RedisConnectionConfiguration> callback = buttonType -> {
            if (buttonType == ButtonType.OK) {
                String nameText = nameFiled.getText();
                String hostText = hostFiled.getText();
                Integer portText = Integer.valueOf(portFiled.getText());
                String passwordText = passwordField.getText();
                return new RedisConnectionConfiguration(nameText, hostText, portText, passwordText);
            }
            return null;
        };
        return DialogUtil.customizeDialog("New Redis connection", gridPane, callback).orElse(null);
    }
}
