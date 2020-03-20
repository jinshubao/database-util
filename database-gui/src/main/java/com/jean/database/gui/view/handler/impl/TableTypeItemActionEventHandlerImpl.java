package com.jean.database.gui.view.handler.impl;

import com.jean.database.common.utils.NodeUtils;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.TableSummaries;
import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.view.action.ICloseable;
import com.jean.database.gui.view.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.util.List;

public class TableTypeItemActionEventHandlerImpl implements ITableTypeItemActionEventHandler {


    private final TabPane tablePane;
    private final TableView<TableSummaries> objectTableView;
    private final TextArea ddlTextArea;


    public TableTypeItemActionEventHandlerImpl(Node root) {
        this.tablePane = NodeUtils.lookup(root, "#tablePane");
        this.objectTableView = NodeUtils.lookup(root, "#objectTableView");
        this.ddlTextArea = NodeUtils.lookup(root, "#ddlTextArea");
    }

    @Override
    public void onSelected(TableTypeTreeItem tableTypeTreeItem) {
        TaskManger.execute(new RefreshTableInfoTask(tableTypeTreeItem, tablePane, ddlTextArea, objectTableView));
    }

    @Override
    public void onClose(TableTypeTreeItem tableTypeTreeItem) {
        tableTypeTreeItem.getChildren().forEach(item -> {
            if (item instanceof ICloseable) {
                ((ICloseable) item).close();
            }
        });
        tableTypeTreeItem.setExpanded(false);
        tableTypeTreeItem.getChildren().clear();
    }

    private static class RefreshTableInfoTask extends BaseTask<List<TableSummaries>> {
        private final TableTypeTreeItem tableTypeTreeItem;
        private final TabPane tablePane;
        private final TextArea ddlTextArea;
        private final TableView<TableSummaries> objectTableView;

        private RefreshTableInfoTask(TableTypeTreeItem tableTypeTreeItem, TabPane tablePane, TextArea ddlTextArea, TableView<TableSummaries> objectTableView) {
            this.tableTypeTreeItem = tableTypeTreeItem;
            this.tablePane = tablePane;
            this.ddlTextArea = ddlTextArea;
            this.objectTableView = objectTableView;
        }

        @Override
        protected List<TableSummaries> call() throws Exception {
            TableTypeMetaData tableTypeMetaData = tableTypeTreeItem.getValue();
            IConnectionConfiguration connectionConfiguration = tableTypeTreeItem.getConnectionConfiguration();
            IMetadataProvider metadataProvider = tableTypeTreeItem.getMetadataProvider();
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                return metadataProvider.getTableSummaries(connection, tableTypeMetaData.getTableCat(), tableTypeMetaData.getTableSchem(), null, null);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            this.objectTableView.getItems().clear();
            List<TableSummaries> tableSummaries = getValue();
            if (tableSummaries != null && !tableSummaries.isEmpty()) {
                this.objectTableView.getItems().addAll(tableSummaries);
            }
            tablePane.getSelectionModel().select(0);
            this.ddlTextArea.setText(tableTypeTreeItem.getValue().getTableType());
        }
    }
}
