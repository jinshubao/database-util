package com.jean.database.gui.controller;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IDatabaseProvider;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.KeyValuePair;
import com.jean.database.core.meta.TableSummaries;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.factory.TreeCellFactory;
import com.jean.database.gui.manager.DatabaseTypeManager;
import com.jean.database.gui.utils.ViewUtils;
import com.jean.database.gui.view.action.IMouseAction;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import com.jean.database.utils.StringUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Tab objectTab;
    @FXML
    private TableView<TableSummaries> objectTableView;
    @FXML
    private TabPane infoTabPane;
    @FXML
    private Tab generalInfoTab;
    @FXML
    private TableView<KeyValuePair> generalInfoTableView;
    @FXML
    private Tab ddlInfoTab;
    @FXML
    private TextArea ddlInfoTextArea;


    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize [location: {}, resources: {}]", location, resources);

        ViewUtils.init(this);

        List<IDatabaseProvider> providers = DatabaseTypeManager.getProviders();

        Menu connectionMenu = new Menu("新建连接");
        for (IDatabaseProvider provider : providers) {
            MenuItem menuItem = new MenuItem(provider.getName());
            menuItem.setUserData(provider);

            menuItem.setOnAction(event -> newConnection(provider));
            connectionMenu.getItems().add(menuItem);
            connectionMenu.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.ADD_IMAGE))));

            String icon = provider.getIcon();
            if (StringUtil.isNotBlank(icon)) {
                menuItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
            }
        }

        Menu fileMenu = new Menu("文件");
        Menu viewMenu = new Menu("查看");
        Menu collectionMenu = new Menu("收藏");
        Menu toolsMenu = new Menu("工具");
        Menu windowMenu = new Menu("窗口");
        Menu helpMenu = new Menu("帮助");

        fileMenu.getItems().addAll(connectionMenu);

        this.menuBar.getMenus().addAll(fileMenu, viewMenu, collectionMenu, toolsMenu, windowMenu, helpMenu);

        //noinspection unchecked
        databaseTreeView.setCellFactory(TreeCellFactory.forTreeView());
        //noinspection unchecked
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

        ((TableColumn<TableSummaries, String>) objectTableView.getColumns().get(0)).setCellValueFactory(param -> param.getValue().tableNameProperty());
        ((TableColumn<TableSummaries, String>) objectTableView.getColumns().get(1)).setCellValueFactory(param -> param.getValue().autoIncrementProperty());
        ((TableColumn<TableSummaries, String>) objectTableView.getColumns().get(2)).setCellValueFactory(param -> param.getValue().modifyTimeProperty());
        ((TableColumn<TableSummaries, String>) objectTableView.getColumns().get(3)).setCellValueFactory(param -> param.getValue().dataLengthProperty());
        ((TableColumn<TableSummaries, String>) objectTableView.getColumns().get(4)).setCellValueFactory(param -> param.getValue().tableTypeProperty());
        ((TableColumn<TableSummaries, String>) objectTableView.getColumns().get(5)).setCellValueFactory(param -> param.getValue().tableRowsProperty());
        ((TableColumn<TableSummaries, String>) objectTableView.getColumns().get(6)).setCellValueFactory(param -> param.getValue().commentsProperty());
        objectTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        generalInfoTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        generalInfoTableView.getColumns().get(0).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getKey()));
        generalInfoTableView.getColumns().get(1).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getValue()));
    }


    @SuppressWarnings("unchecked")
    public void newConnection(IDatabaseProvider provider) {
        IConnectionConfiguration configuration = provider.getConfiguration();
        IMetadataProvider metadataProvider = provider.getMetadataProvider();
        if (configuration != null) {
            ServerTreeItem serverTreeItem = new ServerTreeItem(configuration.getConnectionName(), configuration, metadataProvider);
            String icon = provider.getIcon();
            if (StringUtil.isNotBlank(icon)) {
                serverTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
            }
            databaseTreeView.getRoot().getChildren().add(serverTreeItem);
        }
    }

    public TreeView getDatabaseTreeView() {
        return databaseTreeView;
    }

    public Tab getObjectTab() {
        return objectTab;
    }

    public TableView<TableSummaries> getObjectTableView() {
        return objectTableView;
    }

    public Tab getGeneralInfoTab() {
        return generalInfoTab;
    }

    public TableView<KeyValuePair> getGeneralInfoTableView() {
        return generalInfoTableView;
    }

    public Tab getDdlInfoTab() {
        return ddlInfoTab;
    }

    public TextArea getDdlInfoTextArea() {
        return ddlInfoTextArea;
    }

    public TabPane getObjectTabPan() {
        return objectTabPan;
    }

    public TabPane getInfoTabPane() {
        return infoTabPane;
    }

    public BorderPane getRoot() {
        return root;
    }

}
