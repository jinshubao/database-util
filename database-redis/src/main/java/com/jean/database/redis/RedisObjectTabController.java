package com.jean.database.redis;

import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultController;
import com.jean.database.api.utils.ImageUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Redis 对象标签页 Controller
 */
public class RedisObjectTabController extends DefaultController implements Initializable {

    public TabPane root;
    private Tab objectTab;

    public RedisObjectTabController(ControllerContext context) {
        super(context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.objectTab = new Tab(getTitle());
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
