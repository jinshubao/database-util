package com.jean.database.redis;

import com.jean.database.api.IObjectTabController;
import com.jean.database.api.utils.ImageUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class RedisObjectTabController implements IObjectTabController, Initializable {

    public TabPane root;
    private Tab objectTab;
    private String title;

    public RedisObjectTabController() {
    }

    public RedisObjectTabController(String title) {
        this.title = title;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.objectTab = new Tab(title);
        this.objectTab.setGraphic(ImageUtils.createImageView("/redis/redis.png"));
        this.objectTab.setContent(root);
        this.objectTab.setOnCloseRequest(event -> {

        });
        this.objectTab.setOnClosed(event -> {

        });
    }

    public void selectDatabaseTab(Tab tab) {
        root.getSelectionModel().select(tab);
    }

    public void addDatabaseTab(Tab tab) {
        if (!root.getTabs().contains(tab)) {
            root.getTabs().add(tab);
        }
    }

    @Override
    public Tab getObjectTab() {
        return objectTab;
    }

    @Override
    public void select() {
        TabPane tabPane = objectTab.getTabPane();
        if (tabPane != null) {
            tabPane.getSelectionModel().select(objectTab);
        }

    }

}
