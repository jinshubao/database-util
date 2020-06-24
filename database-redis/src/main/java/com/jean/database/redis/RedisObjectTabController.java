package com.jean.database.redis;

import com.jean.database.api.ViewManger;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class RedisObjectTabController implements Initializable {

    public TabPane root;

    private Tab objectTab;

    public RedisObjectTabController() {
    }

    public RedisObjectTabController(String title) {
        this.objectTab = new Tab(title);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.objectTab.setContent(root);
        this.objectTab.setOnCloseRequest(event -> {

        });
        ViewManger.getViewContext().getObjectTabPan().getTabs().add(objectTab);
        selected();
    }

    public static Callback<Class<?>, Object> getFactory(String title) {
        return param -> new RedisObjectTabController(title);
    }

    public void selected() {
        ViewManger.getViewContext().getObjectTabPan().getSelectionModel().select(objectTab);
    }

    public void selectDatabaseTab(Tab tab) {
        root.getSelectionModel().select(tab);
    }

    public void addDatabaseTab(Tab tab) {
        root.getTabs().add(tab);
    }

}
