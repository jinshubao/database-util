package com.jean.database.gui.view.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.*;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.utils.ViewUtils;
import com.jean.database.gui.view.action.ICloseable;
import com.jean.database.gui.view.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class CatalogItemActionEventHandlerImpl implements ICatalogItemActionEventHandler {

    private final TableView<TableSummaries> objectTableView;
    private final TableView<KeyValuePair> infoTableView;
    private final TextArea ddlTextArea;

    public CatalogItemActionEventHandlerImpl() {
        this.objectTableView = ViewUtils.getInstance().getObjectTableView();
        this.infoTableView = ViewUtils.getInstance().getGeneralInfoTableView();
        this.ddlTextArea = ViewUtils.getInstance().getDdlInfoTextArea();
    }


    @Override
    public void onOpen(CatalogTreeItem catalogTreeItem) {
        if (!catalogTreeItem.isOpen()) {
            this.onRefresh(catalogTreeItem);
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
    public void onRefresh(CatalogTreeItem catalogTreeItem) {
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

        private final WeakReference<CatalogTreeItem> catalogTreeItem;
        private final CatalogMetaData catalogMetaData;
        private final IMetadataProvider metadataProvider;
        private final IConnectionConfiguration connectionConfiguration;

        private List<String> tableTypes = null;

        private OpenCatalogTask(CatalogTreeItem catalogTreeItem) {
            this.catalogTreeItem = new WeakReference<>(catalogTreeItem);
            this.catalogMetaData = catalogTreeItem.getValue();
            this.metadataProvider = catalogTreeItem.getMetadataProvider();
            this.connectionConfiguration = catalogTreeItem.getConnectionConfiguration();
        }

        @Override
        protected List<TableMetaData> call() throws Exception {
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                this.tableTypes = metadataProvider.getTableTypes(connection);
                return metadataProvider.getTableMataData(connection, catalogMetaData.getTableCat(), null, null, null);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            List<TableMetaData> tableMataData = getValue();
            CatalogTreeItem catalogTreeItem = this.catalogTreeItem.get();
            if (catalogTreeItem == null) {
                return;
            }
            catalogTreeItem.getChildren().clear();
            if (tableMataData == null || tableMataData.isEmpty()) {
                return;
            }
            for (String tableType : tableTypes) {
                TableTypeMetaData tableTypeMetaData = new TableTypeMetaData();
                tableTypeMetaData.setTableCat(catalogMetaData.getTableCat());
                tableTypeMetaData.setSeparator(catalogMetaData.getSeparator());
                tableTypeMetaData.setQuoteString(catalogMetaData.getQuoteString());
                tableTypeMetaData.setTableType(tableType);
                TreeItem typeItem = new TableTypeTreeItem(tableTypeMetaData, connectionConfiguration, metadataProvider);
                List<TableTreeItem> items = tableMataData.stream()
                        .filter(metaData -> metaData.getTableType().equals(tableType))
                        .map(metaData -> new TableTreeItem(metaData, connectionConfiguration, metadataProvider))
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
