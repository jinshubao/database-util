package com.jean.database.gui.view.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.view.DataTableTab;
import com.jean.database.gui.view.handler.IDataTableActionEventHandler;
import javafx.scene.Node;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class DataTableActionEventHandlerImpl implements IDataTableActionEventHandler {

    private static final Integer PAGE_SIZE = 1000;


    public DataTableActionEventHandlerImpl(Node root) {
    }


    @Override
    public void refresh(DataTableTab dataTableView) {
        this.refresh(dataTableView, 0);
    }


    public void refresh(DataTableTab dataTableView, int page) {
        TaskManger.execute(new RefreshDataTable(dataTableView, page, PAGE_SIZE));
    }

    private static class RefreshDataTable extends BaseTask<List<Map<String, Object>>> {

        private final DataTableTab dataTableView;
        private final int page;
        private final int pageSize;

        public RefreshDataTable(DataTableTab dataTableView, int page, int pageSize) {
            this.dataTableView = dataTableView;
            this.page = page;
            this.pageSize = pageSize;
        }

        @Override
        protected List<Map<String, Object>> call() throws Exception {
            IMetadataProvider metadataProvider = dataTableView.getMetadataProvider();
            IConnectionConfiguration connectionConfiguration = dataTableView.getConnectionConfiguration();
            TableMetaData tableMetaData = dataTableView.getTableMetaData();
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                return metadataProvider.getTableRows(connection, tableMetaData, pageSize, page);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            dataTableView.getItems().clear();
            List<Map<String, Object>> value = getValue();
            if (value != null && !value.isEmpty()) {
                dataTableView.getItems().addAll(value);
            }
        }
    }

}
