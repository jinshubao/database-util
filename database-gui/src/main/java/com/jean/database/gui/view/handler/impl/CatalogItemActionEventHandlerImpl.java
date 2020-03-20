package com.jean.database.gui.view.handler.impl;

import com.jean.database.common.utils.NodeUtils;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.*;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.view.action.ICloseable;
import com.jean.database.gui.view.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class CatalogItemActionEventHandlerImpl implements ICatalogItemActionEventHandler {

    private final TableView<TableSummaries> objectTableView;
    private final TableView<KeyValuePairData> infoTableView;
    private final TextArea ddlTextArea;

    public CatalogItemActionEventHandlerImpl(Node root) {
        this.objectTableView = NodeUtils.lookup(root, "#objectTableView");
        this.infoTableView = NodeUtils.lookup(root, "#infoTableView");
        this.ddlTextArea = NodeUtils.lookup(root, "#ddlTextArea");
    }


    @Override
    public void onOpen(CatalogTreeItem catalogTreeItem) {
        if (!catalogTreeItem.isOpen()) {
            this.refresh(catalogTreeItem);
        }
    }

    @Override
    public void onClose(CatalogTreeItem catalogTreeItem) {
        catalogTreeItem.getChildren().forEach(item -> {
            if (item instanceof ICloseable) {
                ((ICloseable) item).close();
            }
        });
        catalogTreeItem.setExpanded(false);
        catalogTreeItem.setOpen(false);
        catalogTreeItem.getChildren().clear();

        //清空对象信息
        objectTableView.getItems().clear();
        infoTableView.getItems().clear();
        ddlTextArea.clear();
    }

    @Override
    public void onDelete(CatalogTreeItem catalogTreeItem) {
        this.onClose(catalogTreeItem);
        catalogTreeItem.getParent().getChildren().remove(catalogTreeItem);
    }

    @Override
    public void refresh(CatalogTreeItem catalogTreeItem) {
        TaskManger.execute(new OpenCatalogTask(catalogTreeItem));
    }

    @Override
    public void onDoubleClick(CatalogTreeItem catalogTreeItem) {
        this.onOpen(catalogTreeItem);
    }

    @Override
    public void onSelected(CatalogTreeItem catalogTreeItem) {
        this.ddlTextArea.setText(catalogTreeItem.getValue().getTableCat());
    }

    @Override
    public void onOpenCommandLine(CatalogTreeItem serverTreeItem) {
        //TODO 打开命令行界面
    }

    @Override
    public void onExecuteSqlFile(CatalogTreeItem serverTreeItem) {

    }


    @Override
    public void onExportStructAndData(CatalogTreeItem serverTreeItem) {

    }

    @Override
    public void onExportStruct(CatalogTreeItem serverTreeItem) {

    }

    @Override
    public void onPrintDatabase(CatalogTreeItem catalogTreeItem) {

    }

    @Override
    public void onTransformData(CatalogTreeItem catalogTreeItem) {

    }

    @Override
    public void onConvertToMode(CatalogTreeItem catalogTreeItem) {

    }

    @Override
    public void onFind(CatalogTreeItem catalogTreeItem) {

    }


    private static class OpenCatalogTask extends BaseTask<List<TableMetaData>> {

        private final CatalogTreeItem catalogTreeItem;
        private List<String> tableTypes = null;

        private OpenCatalogTask(CatalogTreeItem catalogTreeItem) {
            this.catalogTreeItem = catalogTreeItem;
        }

        @Override
        protected List<TableMetaData> call() throws Exception {

            CatalogMetaData catalogMetaData = catalogTreeItem.getValue();
            IMetadataProvider metadataProvider = catalogTreeItem.getMetadataProvider();
            IConnectionConfiguration connectionConfiguration = catalogTreeItem.getConnectionConfiguration();

            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                this.tableTypes = metadataProvider.getTableTypes(connection);
                return metadataProvider.getTableMataData(connection, catalogMetaData.getTableCat(), null, null, null);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            List<TableMetaData> tableMataData = getValue();
            CatalogMetaData catalogMetaData = catalogTreeItem.getValue();
            IConnectionConfiguration connectionConfiguration = catalogTreeItem.getConnectionConfiguration();
            IMetadataProvider metadataProvider = catalogTreeItem.getMetadataProvider();
            catalogTreeItem.getChildren().clear();
            if (tableMataData != null && !tableMataData.isEmpty()) {
                for (String tableType : tableTypes) {
                    TableTypeMetaData tableTypeMetaData = new TableTypeMetaData(catalogMetaData, tableType);
                    TreeItem typeItem = new TableTypeTreeItem(tableTypeMetaData, catalogTreeItem.getRoot(), connectionConfiguration, metadataProvider);
                    List<TableTreeItem> items = tableMataData.stream()
                            .filter(metaData -> metaData.getTableType().equals(tableType))
                            .map(metaData -> new TableTreeItem(metaData, catalogTreeItem.getRoot(), connectionConfiguration, metadataProvider))
                            .collect(Collectors.toList());
                    //noinspection unchecked
                    catalogTreeItem.getChildren().add(typeItem);
                    if (!items.isEmpty()) {
                        //noinspection unchecked
                        typeItem.getChildren().addAll(items);
                        typeItem.setExpanded(true);
                    }
                }
                catalogTreeItem.setExpanded(true);
                catalogTreeItem.setOpen(true);
            }
        }
    }
}
