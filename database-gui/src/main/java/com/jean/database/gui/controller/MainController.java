package com.jean.database.gui.controller;

import com.jean.database.core.*;
import com.jean.database.core.utils.DialogUtil;
import com.jean.database.core.utils.StringUtil;
import com.jean.database.gui.factory.TreeCellFactory;
import com.jean.database.gui.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.handler.IDataTableActionEventHandler;
import com.jean.database.gui.handler.IServerItemActionEventHandler;
import com.jean.database.gui.handler.ITableItemActionEventHandler;
import com.jean.database.gui.handler.impl.CatalogItemActionEventHandlerImpl;
import com.jean.database.gui.handler.impl.DataTableActionEventHandlerImpl;
import com.jean.database.gui.handler.impl.ServerItemActionEventHandlerImpl;
import com.jean.database.gui.handler.impl.TableItemActionEventHandlerImpl;
import com.jean.database.gui.manager.DatabaseTypeManager;
import com.jean.database.gui.view.treeitem.ServerTreeItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        //noinspection unchecked
        treeView.setCellFactory(TreeCellFactory.forTreeView());
        //noinspection unchecked
        treeView.setRoot(new TreeItem<>());
        treeView.setShowRoot(false);
    }

    private void initializeMenuBar() {
        List<IDatabaseTypeProvider> providers = DatabaseTypeManager.getProviders();
        newConnectionMenu.getItems().clear();
        connectionMenuButton.getItems().clear();
        for (IDatabaseTypeProvider provider : providers) {
            IDatabaseType dataBaseType = provider.getDatabaseType();
            MenuItem menuItem = new MenuItem(dataBaseType.getName());
            menuItem.setUserData(provider);

            menuItem.setOnAction(event -> newConnection(provider));
            newConnectionMenu.getItems().add(menuItem);

            MenuItem item = new MenuItem(dataBaseType.getName());
            item.setUserData(provider);
            item.setOnAction(event -> newConnection(provider));
            connectionMenuButton.getItems().add(item);

            String icon = dataBaseType.getIcon();
            if (StringUtil.isNotBlank(icon)) {
                menuItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
                item.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
            }
        }
    }

    private void newConnection(IDatabaseTypeProvider provider) {
        IDatabaseType dataBaseType = provider.getDatabaseType();
        IConnectionProvider connectionProvider = provider.getConnectionProvider();
        IMetadataProvider metadataProvider = provider.getMetadataProvider();
        IDataProvider dataProvider = provider.getDataProvider();
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
                IDataTableActionEventHandler dataTableActionEventHandler = new DataTableActionEventHandlerImpl(connection, metadataProvider, dataProvider);
                ITableItemActionEventHandler ITableItemActionEventHandler = new TableItemActionEventHandlerImpl(tablePane, dataTableActionEventHandler);
                ICatalogItemActionEventHandler catalogActionEventHandler = new CatalogItemActionEventHandlerImpl(metadataProvider, connection, ITableItemActionEventHandler);
                IServerItemActionEventHandler serverActionEventHandler = new ServerItemActionEventHandlerImpl(metadataProvider, connection, catalogActionEventHandler);
                ServerTreeItem serverTreeItem = new ServerTreeItem(configuration.getConnectionName(), serverActionEventHandler);
                String icon = dataBaseType.getIcon();
                if (StringUtil.isNotBlank(icon)) {
                    serverTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
                }
                //noinspection unchecked
                treeView.getRoot().getChildren().add(serverTreeItem);
            }
        }

    }
}
