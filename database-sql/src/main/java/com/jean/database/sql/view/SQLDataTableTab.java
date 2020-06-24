package com.jean.database.sql.view;


import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.factory.TableCellFactory;
import com.jean.database.sql.meta.ColumnMetaData;
import com.jean.database.sql.meta.TableMetaData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class SQLDataTableTab extends Tab implements ChangeListener<Number> {

    private static final int PAGE_SIZE = 1000;

    private SQLConnectionConfiguration connectionConfiguration;
    private SQLMetadataProvider metadataProvider;
    private TableView<Map<String, ObjectProperty>> tableView;
    private Pagination pagination;
    private TableMetaData tableMetaData;

    private List<ColumnMetaData> columnDataCache;

    public SQLDataTableTab(TableMetaData tableMetaData, SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider) {
        this.tableMetaData = tableMetaData;
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;

        tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(false);

        VBox.setVgrow(tableView, Priority.ALWAYS);

        this.pagination = new Pagination(0, 0);
        pagination.setVisible(true);
        pagination.currentPageIndexProperty().addListener(this);

        setId(tableMetaData.getFullName());
        setClosable(true);
        setText(tableMetaData.getTableName());
        setTooltip(new Tooltip(tableMetaData.getFullName()));
        setContent(new VBox(tableView, pagination));
    }

    public void refresh() {
        TaskManger.execute(new RefreshDataTableTask(pagination.getCurrentPageIndex(), PAGE_SIZE));
    }

    public void close() {
        this.connectionConfiguration = null;
        this.metadataProvider = null;
        this.tableView = null;
        this.pagination = null;
        this.tableMetaData = null;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (newValue != null) {
            TaskManger.execute(new RefreshDataTableTask(newValue.intValue(), PAGE_SIZE));
        }
    }

    @Override
    public String toString() {
        return "tab [ text: " + getText() + " ]";
    }


    private static class DataColumn extends TableColumn<Map<String, ObjectProperty>, ObjectProperty> {
        public DataColumn(ColumnMetaData columnMetaData) {
            super(columnMetaData.getColumnName());
        }
    }


    private static class RefreshDataTableResult {
        private final List<ColumnMetaData> columnList;
        private final List<Map<String, ObjectProperty>> dataList;
        private final int pageSize;
        private final int totalRecord;

        public RefreshDataTableResult(List<ColumnMetaData> columnList, List<Map<String, ObjectProperty>> dataList, int pageSize, int totalRecord) {
            this.columnList = columnList;
            this.dataList = dataList;
            this.pageSize = pageSize;
            this.totalRecord = totalRecord;
        }

        public List<ColumnMetaData> getColumnList() {
            return columnList;
        }

        public List<Map<String, ObjectProperty>> getDataList() {
            return dataList;
        }

        public int getTotalRecord() {
            return totalRecord;
        }

        public int getPageCount() {
            int page = totalRecord / pageSize;
            if (totalRecord % pageSize > 0) {
                page += 1;
            }
            return page;
        }
    }

    private class RefreshDataTableTask extends BaseTask<RefreshDataTableResult> {

        private final int page;
        private final int pageSize;

        public RefreshDataTableTask(int page, int pageSize) {
            this.page = page;
            this.pageSize = pageSize;
        }

        @Override
        protected RefreshDataTableResult call() throws Exception {
            try (Connection connection = connectionConfiguration.getConnection()) {
                List<ColumnMetaData> columnMetaData
                        = metadataProvider.getColumnMetaData(connection, tableMetaData.getTableCat(), tableMetaData.getTypeSchema(), tableMetaData.getTableName());

                int rowCount = metadataProvider.getTableRowCount(connection, tableMetaData);
                List<Map<String, ObjectProperty>> list = new ArrayList<>();
                if (rowCount > 0) {
                    List<Map<String, Object>> tableRows = metadataProvider.getTableRows(connection, tableMetaData, pageSize, page);
                    list = tableRows.stream().map(value -> {
                        Map<String, ObjectProperty> row = new LinkedHashMap<>(value.size());
                        for (Map.Entry<String, Object> entry : value.entrySet()) {
                            row.put(entry.getKey(), new SimpleObjectProperty<>(entry.getValue()));
                        }
                        return row;
                    }).collect(Collectors.toList());
                }
                return new RefreshDataTableResult(columnMetaData, list, pageSize, rowCount);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            RefreshDataTableResult value = getValue();
            List<ColumnMetaData> columnList = value.getColumnList();
            if (columnsChange(columnDataCache, columnList)) {
                tableView.getColumns().clear();
                for (ColumnMetaData columnMetaData : columnList) {
                    TableColumn<Map<String, ObjectProperty>, ObjectProperty> tableColumn = new DataColumn(columnMetaData);
                    tableColumn.setEditable(true);
                    tableColumn.setCellFactory(TableCellFactory.forTableView());
                    String columnName = columnMetaData.getColumnName();
                    tableColumn.setCellValueFactory(param -> param.getValue().get(columnName));
                    tableView.getColumns().add(tableColumn);
                }
                columnDataCache = columnList;
            }
            tableView.getItems().clear();
            tableView.getItems().addAll(value.getDataList());
            pagination.setPageCount(value.getPageCount());
        }

        /**
         * 检查列是否又变化（减少界面刷新抖动）
         *
         * @param oldList
         * @param newList
         * @return
         */
        private boolean columnsChange(List<ColumnMetaData> oldList, List<ColumnMetaData> newList) {
            if (oldList == null) {
                return true;
            }
            if (oldList.size() != newList.size()) {
                return true;
            }
            for (int i = 0; i < oldList.size(); i++) {
                ColumnMetaData oldValue = oldList.get(i);
                ColumnMetaData newValue = newList.get(i);
                if (!oldValue.equals(newValue)) {
                    return true;
                }
            }
            return false;
        }
    }

}
