package com.jean.database.redis;

import com.jean.database.api.DefaultController;
import com.jean.database.context.ApplicationContext;
import com.jean.database.context.ViewContext;
import com.jean.database.controller.AbstractController;
import com.jean.database.utils.ImageUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RedisObjectTabController extends AbstractController implements Initializable {

    public TabPane root;
    private Tab objectTab;
    private String title;

    public RedisObjectTabController(ApplicationContext context, String title) {
        super(context);
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

    public Tab getObjectTab() {
        return objectTab;
    }

    public void select() {
        TabPane tabPane = objectTab.getTabPane();
        if (tabPane != null) {
            tabPane.getSelectionModel().select(objectTab);
        }

    }

}
