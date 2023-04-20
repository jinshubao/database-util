package com.jean.database.mysql.view.tab;


import com.jean.database.context.ApplicationContext;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.factory.TableCellFactory;
import com.jean.database.sql.meta.ColumnMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.task.BackgroundTask;
import com.jean.database.view.AbstractTab;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 */
public class MySQLDataTableTab extends AbstractTab implements ChangeListener<Number> {

    private static final int PAGE_SIZE = 1000;

    private SQLMetadataProvider metadataProvider;
    private TableView<Map<String, Object>> tableView;
    private Pagination pagination;
    private TableMetaData tableMetaData;

    private List<ColumnMetaData> columnDataCache;

    public MySQLDataTableTab(ApplicationContext context, TableMetaData tableMetaData, SQLMetadataProvider metadataProvider) {
        super(context, tableMetaData.getTableName());

        this.tableMetaData = tableMetaData;
        this.metadataProvider = metadataProvider;

        this.setId(tableMetaData.getFullName());
        this.setClosable(true);
        this.setTooltip(new Tooltip(tableMetaData.getFullName()));

        tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        VBox.setVgrow(tableView, Priority.ALWAYS);
        this.pagination = new Pagination(0, 0);
        pagination.setVisible(true);
        pagination.currentPageIndexProperty().addListener(this);

        this.setContent(new VBox(tableView, pagination));
    }

    public void refresh() {
        RefreshDataTableTask task = new RefreshDataTableTask(pagination.getCurrentPageIndex(), PAGE_SIZE);
        task.setOnSucceeded(event -> {
            RefreshDataTableResult value = (RefreshDataTableResult) event.getSource().getValue();
            refreshData(value);
        });
        getContext().execute(task);
    }

    public void close() {
        this.metadataProvider = null;
        this.tableView = null;
        this.pagination = null;
        this.tableMetaData = null;
        if (getTabPane() != null) {
            getTabPane().getTabs().remove(this);
        }
    }


    private void refreshData(RefreshDataTableResult value) {
        List<ColumnMetaData> columnList = value.getColumnList();
        if (columnsChange(columnDataCache, columnList)) {
            tableView.getColumns().clear();
            for (ColumnMetaData columnMetaData : columnList) {
                TableColumn<Map<String, Object>, Object> tableColumn = new DataColumn(columnMetaData);
                tableColumn.setEditable(true);
                tableColumn.setCellFactory(TableCellFactory.forTableView());
                String columnName = columnMetaData.getColumnName();
                tableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(columnName)));
                tableView.getColumns().add(tableColumn);
            }
            columnDataCache = columnList;
        }
        tableView.getItems().clear();
        tableView.getItems().addAll(value.getDataList());
        pagination.setPageCount(value.getPageCount());
    }


    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (newValue != null) {
            RefreshDataTableTask task = new RefreshDataTableTask(newValue.intValue(), PAGE_SIZE);
            task.setOnSucceeded(event -> {
                refreshData((RefreshDataTableResult) event.getSource().getValue());
            });
            getContext().execute(task);
        }
    }


    /**
     * 检查列是否又变化（减少界面刷新抖动）
     *
     * @param oldList 老的列表
     * @param newList 新列表
     * @return 是否变化
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

    @Override
    public String toString() {
        return "tab [ text: " + getText() + " ]";
    }


    private static class DataColumn extends TableColumn<Map<String, Object>, Object> {
        public DataColumn(ColumnMetaData columnMetaData) {
            super(columnMetaData.getColumnName());
        }
    }


    private static class RefreshDataTableResult {
        private final List<ColumnMetaData> columnList;
        private final List<Map<String, Object>> dataList;
        private final int pageSize;
        private final int totalRecord;

        public RefreshDataTableResult(List<ColumnMetaData> columnList, List<Map<String, Object>> dataList, int pageSize, int totalRecord) {
            this.columnList = columnList;
            this.dataList = dataList;
            this.pageSize = pageSize;
            this.totalRecord = totalRecord;
        }

        public List<ColumnMetaData> getColumnList() {
            return columnList;
        }

        public List<Map<String, Object>> getDataList() {
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

    private class RefreshDataTableTask extends BackgroundTask<RefreshDataTableResult> {

        private final int page;
        private final int pageSize;

        public RefreshDataTableTask(int page, int pageSize) {
            super("刷新表数据");
            this.page = page;
            this.pageSize = pageSize;
        }

        @Override
        protected RefreshDataTableResult doBackground() throws Exception {
            List<ColumnMetaData> columnMetaData
                    = metadataProvider.getColumnMetaData(tableMetaData.getTableCat(), tableMetaData.getTypeSchema(), tableMetaData.getTableName());

            int rowCount = metadataProvider.getTableRowCount(tableMetaData);
            List<Map<String, Object>> list = new ArrayList<>();
            if (rowCount > 0) {
                list = metadataProvider.getTableRows(tableMetaData, pageSize, page);
            }
            return new RefreshDataTableResult(columnMetaData, list, pageSize, rowCount);
        }
    }


}
