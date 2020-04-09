package com.jean.database.gui.view.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.KeyValuePair;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.utils.ViewUtils;
import com.jean.database.gui.view.DataTableTab;
import com.jean.database.gui.view.handler.IDataTableActionEventHandler;
import com.jean.database.gui.view.handler.ITableItemActionEventHandler;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class TableItemActionEventHandlerImpl implements ITableItemActionEventHandler {

    private final IDataTableActionEventHandler dataTableActionEventHandler;
    private final TabPane tablePane;
    private final TableView<KeyValuePair> infoTableView;
    private final TextArea ddlTextArea;


    public TableItemActionEventHandlerImpl() {
        this.dataTableActionEventHandler = LoggerWrapper.warp(new DataTableActionEventHandlerImpl());
        this.tablePane = ViewUtils.getInstance().getObjectTabPan();
        this.infoTableView = ViewUtils.getInstance().getGeneralInfoTableView();
        this.ddlTextArea = ViewUtils.getInstance().getDdlInfoTextArea();
    }

    @Override
    public void onOpen(TableTreeItem tableTreeItem) {
        TableMetaData tableMetaData = tableTreeItem.getValue();
        String fullName = tableMetaData.getFullName();
        Optional<Tab> tabOptional = this.tablePane.getTabs().stream().filter(tab -> fullName.equals(tab.getId())).findFirst();
        if (tabOptional.isPresent()) {
            Tab tab = tabOptional.get();
            this.tablePane.getSelectionModel().select(tab);
        } else {
            TaskManger.execute(new OpenTableTask(tableTreeItem));
        }
    }

    @Override
    public void onClose(TableTreeItem tableTreeItem) {
        //查找对应的tab 并关闭
        Tab tabRef = tableTreeItem.getTabRef();
        if (tabRef != null) {
            tablePane.getTabs().remove(tabRef);
        }
        tableTreeItem.getChildren().clear();
        tableTreeItem.setExpanded(false);
        tableTreeItem.setOpen(false);
    }

    @Override
    public void onDelete(TableTreeItem tableTreeItem) {
        this.onClose(tableTreeItem);
        tableTreeItem.getParent().getChildren().remove(tableTreeItem);
    }

    @Override
    public void onRefresh(TableTreeItem treeItem) {
        TableMetaData tableMetaData = treeItem.getValue();
        String fullName = tableMetaData.getFullName();
        this.tablePane.getTabs().stream().filter(tab -> fullName.equals(tab.getId())).findFirst().ifPresent(tab -> {
            DataTableTab dataTableTab = (DataTableTab) tab;
            this.dataTableActionEventHandler.refresh(dataTableTab, dataTableTab.getCurrentPageIndex());
        });
    }

    @Override
    public void onDoubleClick(TableTreeItem tableTreeItem) {
        this.onOpen(tableTreeItem);
    }

    @Override
    public void onSelected(TableTreeItem tableTreeItem) {
        IConnectionConfiguration connectionConfiguration = tableTreeItem.getConnectionConfiguration();
        IMetadataProvider metadataProvider = tableTreeItem.getMetadataProvider();
        TableMetaData tableMetaData = tableTreeItem.getValue();
        TaskManger.execute(new RefreshTableInfoTask(connectionConfiguration, metadataProvider, tableMetaData));
        ddlTextArea.setText(tableMetaData.getTableName());
    }

    @Override
    public void onCopy(TableTreeItem catalogTreeItem) {

    }

    private class OpenTableTask extends BaseTask<List<ColumnMetaData>> {

        private final WeakReference<TableTreeItem> tableTreeItem;

        private final IMetadataProvider metadataProvider;
        private final IConnectionConfiguration connectionConfiguration;
        private final TableMetaData tableMetaData;


        private OpenTableTask(TableTreeItem tableTreeItem) {
            this.tableTreeItem = new WeakReference<>(tableTreeItem);
            this.metadataProvider = tableTreeItem.getMetadataProvider();
            this.connectionConfiguration = tableTreeItem.getConnectionConfiguration();
            this.tableMetaData = tableTreeItem.getValue();
        }

        @Override
        protected List<ColumnMetaData> call() throws Exception {
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                return metadataProvider.getColumnMetaData(connection, tableMetaData.getTableCat(), tableMetaData.getTypeSchema(), tableMetaData.getTableName());
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();

            TableTreeItem tableTreeItem = this.tableTreeItem.get();
            if (tableTreeItem == null) {
                return;
            }
            List<ColumnMetaData> value = getValue();
            DataTableTab dataTableTab = new DataTableTab(tableMetaData, connectionConfiguration, metadataProvider, value);
            dataTableTab.setOnClosed(event -> tableTreeItem.setOpen(false));
            tablePane.getTabs().add(dataTableTab);
            tablePane.getSelectionModel().select(dataTableTab);
            tableTreeItem.setOpen(true);
            tableTreeItem.setTabRef(dataTableTab);

        }
    }

    private class RefreshTableInfoTask extends BaseTask<List<KeyValuePair<String, Object>>> {

        private final IConnectionConfiguration connectionConfiguration;
        private final IMetadataProvider metadataProvider;
        private final TableMetaData tableMetaData;

        private RefreshTableInfoTask(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, TableMetaData tableMetaData) {
            this.connectionConfiguration = connectionConfiguration;
            this.metadataProvider = metadataProvider;
            this.tableMetaData = tableMetaData;
        }

        @Override
        protected List<KeyValuePair<String, Object>> call() throws Exception {
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                return metadataProvider.getTableDetails(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchema(), tableMetaData.getTableName(), new String[]{tableMetaData.getTableType()});
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            infoTableView.getItems().clear();
            List<KeyValuePair<String, Object>> tableDetails = getValue();
            if (tableDetails != null && !tableDetails.isEmpty()) {
                infoTableView.getItems().addAll(tableDetails);
            }
        }
    }
}
