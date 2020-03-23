package com.jean.database.gui.controller;

import com.jean.database.common.utils.StringUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IDatabaseProvider;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.KeyValuePairData;
import com.jean.database.core.meta.TableSummaries;
import com.jean.database.gui.factory.TreeCellFactory;
import com.jean.database.gui.manager.DatabaseTypeManager;
import com.jean.database.gui.utils.ViewUtils;
import com.jean.database.gui.view.action.IMouseAction;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import javafx.beans.property.SimpleObjectProperty;
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

    public BorderPane root;
    public MenuBar menuBar;
    public Menu fileMenu;
    public Menu newConnectionMenu;
    public MenuItem closeMenuItem;
    public MenuItem exitMenuItem;
    public MenuButton connectionMenuButton;
    public TreeView databaseTreeView;
    public TabPane objectTabPan;
    public Tab objectTab;
    public TableView<TableSummaries> objectTableView;
    public TabPane infoTabPane;
    public Tab generalInfoTab;
    public TableView<KeyValuePairData> generalInfoTableView;
    public Tab ddlInfoTab;
    public TextArea ddlInfoTextArea;

    public MainController() {
        ViewUtils.init(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize [location: {}, resources: {}]", location, resources);

        List<IDatabaseProvider> providers = DatabaseTypeManager.getProviders();
        newConnectionMenu.getItems().clear();
        connectionMenuButton.getItems().clear();
        for (IDatabaseProvider provider : providers) {
            MenuItem menuItem = new MenuItem(provider.getName());
            menuItem.setUserData(provider);

            menuItem.setOnAction(event -> newConnection(provider));
            newConnectionMenu.getItems().add(menuItem);

            MenuItem item = new MenuItem(provider.getName());
            item.setUserData(provider);
            item.setOnAction(event -> newConnection(provider));
            connectionMenuButton.getItems().add(item);

            String icon = provider.getIcon();
            if (StringUtil.isNotBlank(icon)) {
                menuItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
                item.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
            }
        }


        //noinspection unchecked
        databaseTreeView.setCellFactory(TreeCellFactory.forTreeView());
        //noinspection unchecked
        databaseTreeView.setRoot(new TreeItem<>());
        databaseTreeView.setShowRoot(false);
        databaseTreeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeItem treeItem = databaseTreeView.getTreeItem(newValue.intValue());
                if (IMouseAction.class.isInstance(treeItem)) {
                    ((IMouseAction) treeItem).select();
                }
            }
        });

        objectTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        objectTableView.getColumns().get(0).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getTableName()));
        objectTableView.getColumns().get(1).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getAutoIncrement()));
        objectTableView.getColumns().get(2).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getModifyTime()));
        objectTableView.getColumns().get(3).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getDataLength()));
        objectTableView.getColumns().get(4).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getTableType()));
        objectTableView.getColumns().get(5).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getTableRows()));
        objectTableView.getColumns().get(6).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getComments()));

        generalInfoTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        generalInfoTableView.getColumns().get(0).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getName()));
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

    public TableView<KeyValuePairData> getGeneralInfoTableView() {
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

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Menu getFileMenu() {
        return fileMenu;
    }

    public Menu getNewConnectionMenu() {
        return newConnectionMenu;
    }

    public MenuItem getCloseMenuItem() {
        return closeMenuItem;
    }

    public MenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public MenuButton getConnectionMenuButton() {
        return connectionMenuButton;
    }
}
