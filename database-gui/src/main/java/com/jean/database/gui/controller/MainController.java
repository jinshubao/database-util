package com.jean.database.gui.controller;

import com.jean.database.api.IDatabaseProvider;
import com.jean.database.api.TaskManger;
import com.jean.database.api.TreeCellFactory;
import com.jean.database.api.ViewManager;
import com.jean.database.api.view.ViewContext;
import com.jean.database.api.view.action.IMouseAction;
import com.jean.database.gui.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private BorderPane root;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TreeView databaseTreeView;
    @FXML
    private TabPane objectTabPan;
    @FXML
    private TabPane infoTabPane;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize [location: {}, resources: {}]", location, resources);
        this.initMenuBar();
        databaseTreeView.setCellFactory(TreeCellFactory.forTreeView());
        databaseTreeView.setRoot(new TreeItem<>());
        databaseTreeView.setShowRoot(false);
        databaseTreeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeItem treeItem = databaseTreeView.getTreeItem(newValue.intValue());
                if (treeItem instanceof IMouseAction) {
                    ((IMouseAction) treeItem).select();
                }
            }
        });
        objectTabPan.getTabs().clear();
        infoTabPane.getTabs().clear();
        ViewManager.init(menuBar, databaseTreeView, objectTabPan, infoTabPane);
        TaskManger.init(Runtime.getRuntime().availableProcessors() * 2);

        DatabaseManager.init();
        List<IDatabaseProvider> providers = DatabaseManager.getProviders();

        ViewContext viewContext = new ViewContext(menuBar, databaseTreeView, objectTabPan, infoTabPane);
        for (IDatabaseProvider provider : providers) {
            provider.init(viewContext);
        }
    }

    private void initMenuBar() {
        Menu fileMenu = new Menu("文件");
        fileMenu.setId(ViewManager.MENU_ID__FILE_MENU);
        fileMenu.getItems().add(new Menu("新建链接"));
        Menu viewMenu = new Menu("查看");
        viewMenu.setId(ViewManager.MENU_ID__VIEW_MENU);
        Menu collectionMenu = new Menu("收藏");
        collectionMenu.setId(ViewManager.MENU_ID__COLLECTION_MENU);
        Menu toolsMenu = new Menu("工具");
        toolsMenu.setId(ViewManager.MENU_ID__TOOLS_MENU);
        Menu windowMenu = new Menu("窗口");
        windowMenu.setId(ViewManager.MENU_ID__WINDOW_MENU);
        Menu helpMenu = new Menu("帮助");
        helpMenu.setId(ViewManager.MENU_ID__HELP_MENU);
        this.menuBar.getMenus().addAll(fileMenu, viewMenu, collectionMenu, toolsMenu, windowMenu, helpMenu);
    }


}
