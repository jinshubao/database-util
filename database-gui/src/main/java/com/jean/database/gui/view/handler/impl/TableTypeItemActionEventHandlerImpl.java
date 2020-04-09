package com.jean.database.gui.view.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.TableSummaries;
import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.utils.ViewUtils;
import com.jean.database.gui.view.action.ICloseable;
import com.jean.database.gui.view.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.util.List;

public class TableTypeItemActionEventHandlerImpl implements ITableTypeItemActionEventHandler {


    private final TabPane objectTablePane;
    private final TableView<TableSummaries> objectTableView;
    private final TextArea ddlTextArea;


    public TableTypeItemActionEventHandlerImpl() {
        this.objectTablePane = ViewUtils.getInstance().getObjectTabPan();
        this.objectTableView = ViewUtils.getInstance().getObjectTableView();
        this.objectTableView.setFixedCellSize(-1);
        this.ddlTextArea = ViewUtils.getInstance().getDdlInfoTextArea();
    }

    @Override
    public void onSelected(TableTypeTreeItem tableTypeTreeItem) {
        IConnectionConfiguration connectionConfiguration = tableTypeTreeItem.getConnectionConfiguration();
        IMetadataProvider metadataProvider = tableTypeTreeItem.getMetadataProvider();
        TableTypeMetaData tableTypeMetaData = tableTypeTreeItem.getValue();
        TaskManger.execute(new RefreshTableInfoTask(connectionConfiguration, metadataProvider, tableTypeMetaData));
        this.ddlTextArea.setText(tableTypeMetaData.getTableType());
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


    private class RefreshTableInfoTask extends BaseTask<List<TableSummaries>> {

        private final IConnectionConfiguration connectionConfiguration;
        private final IMetadataProvider metadataProvider;
        private final TableTypeMetaData tableTypeMetaData;

        private RefreshTableInfoTask(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, TableTypeMetaData tableTypeMetaData) {
            this.connectionConfiguration = connectionConfiguration;
            this.metadataProvider = metadataProvider;
            this.tableTypeMetaData = tableTypeMetaData;
        }

        @Override
        protected List<TableSummaries> call() throws Exception {
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                return metadataProvider.getTableSummaries(connection, tableTypeMetaData.getTableCat(), tableTypeMetaData.getTableSchema(), null, null);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            objectTableView.getItems().clear();
            List<TableSummaries> tableSummaries = getValue();
            if (tableSummaries != null && !tableSummaries.isEmpty()) {
                objectTableView.getItems().addAll(tableSummaries);
            }
            objectTablePane.getSelectionModel().select(0);
        }
    }
}
