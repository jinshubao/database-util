package com.jean.database.client.controller;

import com.jean.database.client.constant.CommonConstant;
import com.jean.database.client.constant.StageType;
import com.jean.database.client.factory.MetaDataProviderManager;
import com.jean.database.client.factory.TreeCellFactory;
import com.jean.database.client.utils.DialogUtil;
import com.jean.database.client.view.CustomTableView;
import com.jean.database.client.view.ISelected;
import com.jean.database.client.view.treeitem.ConnectionTreeItem;
import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.constant.DatabaseType;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.core.provider.IMetadataProvider;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author jinshubao
 */
@Controller
public class MainController extends BaseController {

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


    private ResourceBundle resources;
    private ObjectProperty<IConnectionConfiguration> currentConnectionProperty = new SimpleObjectProperty<>(this, "connectionConfig");

    @Autowired
    private ConnectionController connectionController;

    @Autowired
    private MetaDataProviderManager providerManager;

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        TreeItem rootItem = new TreeItem();
        newConnection.setOnAction(event ->
                DialogUtil.customizeDialog(resources.getString("dialog.newConnection.title"),
                        null,
                        CommonConstant.SCENES.get(StageType.CONNECTION),
                        buttonType -> {
                            if (buttonType == ButtonType.OK) {
                                return connectionController.getConnectionConfig();
                            }
                            return null;
                        }).ifPresent(
                        config -> {
                            currentConnectionProperty.set(config);
                            ConnectionTreeItem treeItem = new ConnectionTreeItem(config);
                            rootItem.getChildren().add(treeItem);
                        }));
        tableButton.setOnAction(event -> {
        });

        treeView.setCellFactory(TreeCellFactory.forTreeView());
        treeView.setShowRoot(false);
        treeView.setRoot(rootItem);
        treeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue instanceof ISelected) {
                    ISelected value = ((ISelected) newValue);
                    value.onSelected(value);
                }
            }
        }));
    }

    public void openTable(IConnectionConfiguration configuration,
                          CatalogMetaData catalogMetaData,
                          SchemaMetaData schemaMetaData,
                          String tableNamePattern) throws Exception {
        CustomTableView tableView = new CustomTableView(getSupportMetaDataProvider(configuration.getDatabaseType()),
                configuration, catalogMetaData, schemaMetaData, tableNamePattern);
        tableView.refreshData();
        Tab tab = new Tab(tableNamePattern);
        tab.setClosable(true);
        tab.setContent(tableView);
        tablePane.getSelectionModel().select(tab);
        tablePane.getTabs().add(tab);
    }

    /**
     * @param provider
     * @param catalogMetaData
     * @param schemaMetaData
     * @throws Exception
     */
    public void showAllTables(IMetadataProvider provider, CatalogMetaData catalogMetaData, SchemaMetaData schemaMetaData) throws Exception {
        objectPane.getChildren().clear();
        List<TableMetaData> tables = provider.getTables(currentConnectionProperty.get(),
                catalogMetaData.getTableCat(),
                schemaMetaData != null ? schemaMetaData.getTableSchem() : null);
        for (TableMetaData table : tables) {
            Label label = new Label(table.getTableName());
            objectPane.getChildren().add(label);
        }

    }

    /**
     * 所有使用 IMetadataProvider 的地方都调用这个方法
     *
     * @param databaseType
     * @return
     */
    public IMetadataProvider getSupportMetaDataProvider(DatabaseType databaseType) {
        return providerManager.getMetadataProvider(databaseType);
    }

    /**
     * 获取当前配置
     *
     * @return
     */
    public IConnectionConfiguration getCurrentConnectionConfiguration() {
        return currentConnectionProperty.get();
    }

    public void showTableGroups(List<TableMetaData> metaDataList) {
        ObservableList<Node> children = objectPane.getChildren();
        children.clear();
        for (TableMetaData tableMetaData : metaDataList) {
            children.add(new Label(tableMetaData.getTableName()));
        }
    }

    public void showViewGroups() {
        ObservableList<Node> children = objectPane.getChildren();
        children.clear();

    }
}
