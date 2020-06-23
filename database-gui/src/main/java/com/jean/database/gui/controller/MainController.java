package com.jean.database.gui.controller;

import com.jean.database.api.IDatabaseProvider;
import com.jean.database.api.TreeCellFactory;
import com.jean.database.api.ViewContext;
import com.jean.database.api.view.action.IMouseAction;
import com.jean.database.gui.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MainController implements ViewContext, Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private BorderPane root;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TreeView<?> databaseTreeView;
    @FXML
    private TabPane objectTabPan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize [location: {}, resources: {}]", location, resources);
        this.initMenuBar();
        databaseTreeView.setCellFactory(TreeCellFactory.forTreeView());
        databaseTreeView.setRoot(new TreeItem<>());
        databaseTreeView.setShowRoot(false);
        databaseTreeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeItem<?> treeItem = databaseTreeView.getTreeItem(newValue.intValue());
                if (treeItem instanceof IMouseAction) {
                    ((IMouseAction) treeItem).select();
                }
            }
        });

        List<IDatabaseProvider> providers = DatabaseManager.getProviders();
        for (IDatabaseProvider provider : providers) {
            provider.init(this);
        }
    }

    private void initMenuBar() {
        Menu fileMenu = new Menu("文件");
        fileMenu.getItems().add(new Menu("新建链接"));
        Menu viewMenu = new Menu("查看");
        Menu collectionMenu = new Menu("收藏");
        Menu toolsMenu = new Menu("工具");
        Menu windowMenu = new Menu("窗口");
        Menu helpMenu = new Menu("帮助");
        this.menuBar.getMenus().addAll(fileMenu, viewMenu, collectionMenu, toolsMenu, windowMenu, helpMenu);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public MenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public TreeView getDatabaseTreeView() {
        return databaseTreeView;
    }

    @Override
    public TabPane getObjectTabPan() {
        return objectTabPan;
    }

    @Override
    public void addObjectTab(Tab tab) {
        objectTabPan.getTabs().add(tab);
    }

    @Override
    public void removeObjectTab(Tab tab) {
        objectTabPan.getTabs().remove(tab);
    }

    @Override
    public void addInfoTab(Tab tab) {
        objectTabPan.getTabs().add(tab);
    }

    @Override
    public void removeInfoTab(Tab tab) {
        objectTabPan.getTabs().remove(tab);
    }

    @Override

    public void addDatabaseItem(TreeItem treeItem) {
        databaseTreeView.getRoot().getChildren().add(treeItem);
    }
}
