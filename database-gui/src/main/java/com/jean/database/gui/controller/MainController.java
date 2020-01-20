package com.jean.database.gui.controller;

import com.jean.database.core.*;
import com.jean.database.gui.factory.TreeCellFactory;
import com.jean.database.gui.handler.CatalogActionEventHandler;
import com.jean.database.gui.handler.DataTableActionEventHandler;
import com.jean.database.gui.handler.ServerActionEventHandler;
import com.jean.database.gui.handler.TableActionEventHandler;
import com.jean.database.gui.handler.impl.CatalogActionEventHandlerImpl;
import com.jean.database.gui.handler.impl.DataTableActionEventHandlerImpl;
import com.jean.database.gui.handler.impl.ServerActionEventHandlerImpl;
import com.jean.database.gui.handler.impl.TableActionEventHandlerImpl;
import com.jean.database.gui.manager.DatabaseTypeManager;
import com.jean.database.gui.utils.DialogUtil;
import com.jean.database.gui.view.ISelecte;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author jinshubao
 */
public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    public MenuBar menuBar;
    @FXML
    public Menu fileMenu;
    @FXML
    public Menu newConnectionMenu;
    @FXML
    public MenuItem newConnection;
    @FXML
    public ToggleButton tableButton;
    @FXML
    public TreeView treeView;
    @FXML
    public TabPane tablePane;
    @FXML
    public Tab objectTab;
    @FXML
    public FlowPane objectPane;
    @FXML
    public SplitPane splitPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.initializeMenuBar();
        treeView.setCellFactory(TreeCellFactory.forTreeView());
        treeView.setShowRoot(false);
        treeView.setRoot(new TreeItem<>());
        treeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue instanceof ISelecte) {
                    ISelecte value = ((ISelecte) newValue);
                    value.onSelected(value);
                }
            }
        }));

        tableButton.setOnAction(event -> {
        });
    }

    private void initializeMenuBar() {
        List<IDatabaseTypeProvider> providers = DatabaseTypeManager.getProviders();
        ObservableList<MenuItem> items = newConnectionMenu.getItems();
        for (IDatabaseTypeProvider provider : providers) {
            IDataBaseType dataBaseType = provider.getDataBaseType();
            IConnectionProvider connectionProvider = provider.getConnectionProvider();
            IMetadataProvider metadataProvider = provider.getMetadataProvider();
            IDataProvider dataProvider = provider.getDataProvider();
            MenuItem menuItem = new MenuItem(dataBaseType.getName());
            menuItem.setUserData(dataBaseType);
            items.add(menuItem);
            menuItem.setOnAction(event -> {
                IConnectionConfiguration configuration = provider.getConfigurationProvider().getConfiguration(dataBaseType);
                logger.debug("new configuration [{}]", configuration);
                if (configuration != null) {
                    Connection connection = null;
                    try {
                        connection = connectionProvider.getConnection(configuration);
                    } catch (SQLException e) {
                        logger.error(e.getMessage(), e);
                        DialogUtil.error("ERROR", e.getMessage(), e);
                    }
                    if (connection != null) {
                        DataTableActionEventHandler dataTableActionEventHandler = new DataTableActionEventHandlerImpl(connection, metadataProvider, dataProvider);
                        TableActionEventHandler tableActionEventHandler = new TableActionEventHandlerImpl(tablePane, dataTableActionEventHandler);
                        CatalogActionEventHandler catalogActionEventHandler = new CatalogActionEventHandlerImpl(metadataProvider, connection, tableActionEventHandler);
                        ServerActionEventHandler serverActionEventHandler = new ServerActionEventHandlerImpl(metadataProvider, connection, catalogActionEventHandler);
                        ServerTreeItem serverTreeItem = new ServerTreeItem(configuration.getConnectionName(), serverActionEventHandler);
                        treeView.getRoot().getChildren().add(serverTreeItem);
                    }
                }
            });
        }
    }
}
