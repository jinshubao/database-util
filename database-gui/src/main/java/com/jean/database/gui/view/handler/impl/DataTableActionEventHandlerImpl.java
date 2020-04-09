package com.jean.database.gui.view.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.manager.TaskManger;
import com.jean.database.gui.task.BaseTask;
import com.jean.database.gui.view.DataTableTab;
import com.jean.database.gui.view.handler.IDataTableActionEventHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataTableActionEventHandlerImpl implements IDataTableActionEventHandler {

    private static final Integer PAGE_SIZE = 1000;


    public DataTableActionEventHandlerImpl() {
    }


    @Override
    public void onRefresh(DataTableTab dataTableView) {
        this.refresh(dataTableView, 0);
    }


    public void refresh(DataTableTab dataTableView, int page) {
        TaskManger.execute(new RefreshDataTableTask(dataTableView, page, PAGE_SIZE));
    }

    private static class RefreshDataTableTask extends BaseTask<List<Map<String, ObjectProperty>>> {
        private final WeakReference<DataTableTab> dataTableView;

        private final IMetadataProvider metadataProvider;
        private final IConnectionConfiguration connectionConfiguration;
        private final TableMetaData tableMetaData;
        private final int page;
        private final int pageSize;

        public RefreshDataTableTask(DataTableTab dataTableView, int page, int pageSize) {
            this.dataTableView = new WeakReference<>(dataTableView);
            this.metadataProvider = dataTableView.getMetadataProvider();
            this.connectionConfiguration = dataTableView.getConnectionConfiguration();
            this.tableMetaData = dataTableView.getTableMetaData();
            this.page = page;
            this.pageSize = pageSize;
        }

        @Override
        protected List<Map<String, ObjectProperty>> call() throws Exception {
            try (Connection connection = metadataProvider.getConnection(connectionConfiguration)) {
                List<Map<String, Object>> tableRows = metadataProvider.getTableRows(connection, tableMetaData, pageSize, page);
                return tableRows.stream().map(value->{
                    Map<String, ObjectProperty> row =  new LinkedHashMap<>(value.size());
                    for (Map.Entry<String, Object> entry : value.entrySet()) {
                        row.put(entry.getKey(), new SimpleObjectProperty(entry.getValue()));
                    }
                    return row;
                }).collect(Collectors.toList());
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            DataTableTab dataTableTab = dataTableView.get();
            if (dataTableTab != null) {
                dataTableTab.updateItems(getValue());
            }
        }
    }

}
