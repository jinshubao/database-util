package com.jean.database.gui.view.handler.impl;

import com.jean.database.common.utils.NodeUtils;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.KeyValuePairData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.view.DataTableTab;
import com.jean.database.gui.view.handler.IDataTableActionEventHandler;
import com.jean.database.gui.view.handler.ITableItemActionEventHandler;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class TableItemActionEventHandlerImpl implements ITableItemActionEventHandler {

    private final IDataTableActionEventHandler dataTableActionEventHandler;
    private final TabPane tablePane;
    private final TableView<KeyValuePairData> infoTableView;
    private final TextArea ddlTextArea;


    public TableItemActionEventHandlerImpl(Node root) {
        this.dataTableActionEventHandler = LoggerWrapper.warp(new DataTableActionEventHandlerImpl(root));
        this.tablePane = NodeUtils.lookup(root, "#tablePane");
        this.infoTableView = NodeUtils.lookup(root, "#infoTableView");
        this.ddlTextArea = NodeUtils.lookup(root, "#ddlTextArea");
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
            TaskManger.execute(new OpenTableTask(tableTreeItem, tablePane));
        }
    }

    @Override
    public void onClose(TableTreeItem tableTreeItem) {
        //查找对应的tab 并关闭
        Tab tabRef = tableTreeItem.getTabRef();
        if (tabRef != null) {
            tabRef.getTabPane().getTabs().remove(tabRef);
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
    public void refresh(TableTreeItem treeItem) {
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
        TaskManger.execute(new RefreshTableInfoTask(tableTreeItem, infoTableView, ddlTextArea));
    }

    private static class OpenTableTask extends BaseTask<List<ColumnMetaData>> {

        private final TableTreeItem tableTreeItem;
        private final TabPane tablePane;

        private OpenTableTask(TableTreeItem tableTreeItem, TabPane tablePane) {
            this.tableTreeItem = tableTreeItem;
            this.tablePane = tablePane;
        }

        @Override
        protected List<ColumnMetaData> call() throws Exception {
            TableMetaData tableMetaData = tableTreeItem.getValue();
            IMetadataProvider metadataProvider = tableTreeItem.getMetadataProvider();
            IConnectionConfiguration connectionConfiguration = tableTreeItem.getConnectionConfiguration();
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                return metadataProvider.getColumnMetaData(connection, tableMetaData.getTableCat(), tableMetaData.getTypeSchema(), tableMetaData.getTableName());
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            TableMetaData tableMetaData = tableTreeItem.getValue();
            IMetadataProvider metadataProvider = tableTreeItem.getMetadataProvider();
            IConnectionConfiguration connectionConfiguration = tableTreeItem.getConnectionConfiguration();
            List<ColumnMetaData> value = getValue();

            DataTableTab dataTableTab = new DataTableTab(tableMetaData, tableTreeItem.getRoot(), connectionConfiguration, metadataProvider, value);
            dataTableTab.setOnClosed(event -> tableTreeItem.setOpen(false));
            tablePane.getTabs().add(dataTableTab);
            tablePane.getSelectionModel().select(dataTableTab);
            tableTreeItem.setOpen(true);
            tableTreeItem.setTabRef(dataTableTab);

        }
    }

    private static class RefreshTableInfoTask extends BaseTask<List<KeyValuePairData>> {

        private final TableTreeItem tableTreeItem;
        private final TableView<KeyValuePairData> infoTableView;
        private final TextArea ddlTextArea;

        private RefreshTableInfoTask(TableTreeItem tableTreeItem, TableView<KeyValuePairData> infoTableView, TextArea ddlTextArea) {
            this.tableTreeItem = tableTreeItem;
            this.infoTableView = infoTableView;
            this.ddlTextArea = ddlTextArea;
        }

        @Override
        protected List<KeyValuePairData> call() throws Exception {
            TableMetaData tableMetaData = tableTreeItem.getValue();
            IMetadataProvider metadataProvider = tableTreeItem.getMetadataProvider();
            IConnectionConfiguration connectionConfiguration = tableTreeItem.getConnectionConfiguration();
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                return metadataProvider.getTableDetails(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName(), new String[]{tableMetaData.getTableType()});
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            this.infoTableView.getItems().clear();
            this.ddlTextArea.clear();
            List<KeyValuePairData> tableDetails = getValue();
            if (tableDetails != null && !tableDetails.isEmpty()) {
                this.infoTableView.getItems().addAll(tableDetails);
            }
            this.ddlTextArea.setText(tableTreeItem.getValue().getTableName());
        }
    }
}
