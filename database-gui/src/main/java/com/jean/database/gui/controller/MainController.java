package com.jean.database.gui.controller;

import com.jean.database.common.utils.StringUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IDatabaseProvider;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.KeyValuePairData;
import com.jean.database.core.meta.TableSummaries;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.factory.TreeCellFactory;
import com.jean.database.gui.listener.WeakChangeListener;
import com.jean.database.gui.manager.DatabaseTypeManager;
import com.jean.database.gui.view.action.IMouseAction;
import com.jean.database.gui.view.handler.IServerItemActionEventHandler;
import com.jean.database.gui.view.handler.impl.ServerItemActionEventHandlerImpl;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
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

/**
 * @author jinshubao
 */
public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    public BorderPane root;
    @FXML
    public MenuBar menuBar;
    @FXML
    public Menu fileMenu;
    @FXML
    public Menu newConnectionMenu;
    @FXML
    public MenuButton connectionMenuButton;
    @FXML
    public ToggleButton tableToggleButton;
    @FXML
    public ToggleButton viewToggleButton;
    @FXML
    public ToggleButton functionToggleButton;
    @FXML
    public ToggleButton eventToggleButton;
    @FXML
    public ToggleButton queryToggleButton;
    @FXML
    public ToggleButton reportToggleButton;
    @FXML
    public ToggleButton backupToggleButton;
    @FXML
    public ToggleButton plainToggleButton;
    @FXML
    public ToggleButton modelToggleButton;
    @FXML
    public TreeView treeView;
    @FXML
    public TableView<TableSummaries> objectTableView;
    @FXML
    public TableView<KeyValuePairData> infoTableView;

    private ChangeListener<Number> treeViewItemSelectedIndexChangeListener;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {

        this.treeViewItemSelectedIndexChangeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeItem treeItem = treeView.getTreeItem(newValue.intValue());
                if (treeItem instanceof IMouseAction) {
                    ((IMouseAction) treeItem).select();
                }
            }
        };

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
        treeView.setCellFactory(TreeCellFactory.forTreeView());
        //noinspection unchecked
        treeView.setRoot(new TreeItem<>());
        treeView.setShowRoot(false);
        treeView.getSelectionModel().selectedIndexProperty().addListener(new WeakChangeListener<>(treeViewItemSelectedIndexChangeListener));

        objectTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        objectTableView.getColumns().get(0).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getTableName()));
        objectTableView.getColumns().get(1).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getAutoIncrement()));
        objectTableView.getColumns().get(2).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getModifyTime()));
        objectTableView.getColumns().get(3).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getDataLength()));
        objectTableView.getColumns().get(4).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getTableType()));
        objectTableView.getColumns().get(5).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getTableRows()));
        objectTableView.getColumns().get(6).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getComments()));

        infoTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        infoTableView.getColumns().get(0).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getName()));
        infoTableView.getColumns().get(1).setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getValue()));
    }

    private void newConnection(IDatabaseProvider provider) {
        IConnectionConfiguration configuration = provider.getConfiguration();
        IMetadataProvider metadataProvider = provider.getMetadataProvider();
        logger.debug("new configuration [{}]", configuration);
        if (configuration != null) {
            ServerTreeItem serverTreeItem = new ServerTreeItem(configuration.getConnectionName(), root, configuration, metadataProvider);
            String icon = provider.getIcon();
            if (StringUtil.isNotBlank(icon)) {
                serverTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
            }
            //noinspection unchecked
            treeView.getRoot().getChildren().add(serverTreeItem);
        }

    }
}
