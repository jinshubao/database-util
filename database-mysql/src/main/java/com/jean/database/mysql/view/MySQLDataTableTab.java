package com.jean.database.mysql.view;


import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.factory.TableCellFactory;
import com.jean.database.sql.meta.ColumnMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.view.SelectableTableView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class MySQLDataTableTab extends Tab {

    private static final Logger logger = LoggerFactory.getLogger(MySQLDataTableTab.class);
    private static final int PAGE_SIZE = 1000;

    private SQLMetadataProvider metadataProvider;
    private SelectableTableView<Map<String, ObjectProperty>> tableView;
    private Pagination pagination;
    private TableMetaData tableMetaData;

    private List<ColumnMetaData> columnDataCache;
    private List<Map<String, ObjectProperty>> originalItems = new ArrayList<>();
    private List<Map<String, Object>> originalValues = new ArrayList<>();
    private List<Map<String, ObjectProperty>> addedRows = new ArrayList<>();
    private List<Map<String, ObjectProperty>> deletedRows = new ArrayList<>();
    private List<String> primaryKeyColumns = new ArrayList<>();
    private BooleanProperty dirty = new SimpleBooleanProperty(false);

    public MySQLDataTableTab(TableMetaData tableMetaData, SQLMetadataProvider metadataProvider) {
        setId(tableMetaData.getFullName());
        this.tableMetaData = tableMetaData;
        this.metadataProvider = metadataProvider;

        tableView = new SelectableTableView<>();
        tableView.setEditable(true);

        VBox.setVgrow(tableView, Priority.ALWAYS);

        ToolBar toolBar = new ToolBar();
        Button refreshButton = new Button("⟳");
        refreshButton.setTooltip(new Tooltip("刷新"));
        refreshButton.setOnAction(event -> handleRefresh());
        Button addButton = new Button("+");
        addButton.setTooltip(new Tooltip("新增一条数据"));
        addButton.setOnAction(event -> handleAdd());
        Button deleteButton = new Button("-");
        deleteButton.setTooltip(new Tooltip("删除选中的数据"));
        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.setOnAction(event -> handleDelete());
        Button commitButton = new Button("↑");
        commitButton.setTooltip(new Tooltip("提交事务"));
        commitButton.disableProperty().bind(dirty.not());
        commitButton.setOnAction(event -> TaskManger.execute(new CommitTask()));
        toolBar.getItems().addAll(refreshButton, addButton, deleteButton, commitButton);

        this.pagination = new Pagination(0, 0);
        pagination.setVisible(true);
        pagination.currentPageIndexProperty().addListener(new PageChangeListener());

        setId(tableMetaData.getFullName());
        setClosable(true);
        setText(tableMetaData.getTableName());
        setTooltip(new Tooltip(tableMetaData.getFullName()));
        setContent(new VBox(toolBar, tableView, pagination));
    }

    public void refresh() {
        TaskManger.execute(new RefreshDataTableTask(pagination.getCurrentPageIndex(), PAGE_SIZE));
    }

    public void close() {
        this.metadataProvider = null;
        this.tableView = null;
        this.pagination = null;
        this.tableMetaData = null;
        this.originalItems.clear();
        this.originalValues.clear();
        this.addedRows.clear();
        this.deletedRows.clear();
        this.primaryKeyColumns.clear();
        if (getTabPane() != null) {
            getTabPane().getTabs().remove(this);
        }
    }

    private void handleRefresh() {
        if (dirty.get()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认");
            alert.setHeaderText("有未提交的改动");
            alert.setContentText("刷新将放弃当前所有未提交的改动，是否继续？");
            alert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    refresh();
                }
            });
        } else {
            refresh();
        }
    }

    private void handleAdd() {
        Map<String, ObjectProperty> newRow = new LinkedHashMap<>();
        for (ColumnMetaData column : columnDataCache) {
            newRow.put(column.getColumnName(), new SimpleObjectProperty<>(null));
        }
        addedRows.add(newRow);
        tableView.getItems().add(newRow);
        dirty.set(true);
    }

    private void handleDelete() {
        List<Integer> selectedIndices = new ArrayList<>(tableView.getSelectionModel().getSelectedIndices());
        selectedIndices.sort(Comparator.reverseOrder());
        for (int index : selectedIndices) {
            deleteRowAt(index);
        }
    }

    private void deleteRowAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= tableView.getItems().size()) return;
        Map<String, ObjectProperty> row = tableView.getItems().get(rowIndex);
        boolean isOriginal = false;
        for (Map<String, ObjectProperty> original : originalItems) {
            if (original == row) {
                isOriginal = true;
                break;
            }
        }
        if (isOriginal) {
            deletedRows.add(row);
        } else {
            addedRows.remove(row);
        }
        tableView.getItems().remove(rowIndex);
        dirty.set(true);
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
            List<ColumnMetaData> columnMetaData
                    = metadataProvider.getColumnMetaData(tableMetaData.getTableCat(), tableMetaData.getTypeSchema(), tableMetaData.getTableName());

            int rowCount = metadataProvider.getTableRowCount(tableMetaData);
            List<Map<String, ObjectProperty>> list = new ArrayList<>();
            if (rowCount > 0) {
                List<Map<String, Object>> tableRows = metadataProvider.getTableRows(tableMetaData, pageSize, page);
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

        @Override
        protected void succeeded() {
            super.succeeded();
            RefreshDataTableResult value = getValue();
            List<ColumnMetaData> columnList = value.getColumnList();
            if (columnsChange(columnDataCache, columnList)) {
                tableView.getColumns().removeIf(col -> !tableView.isIndexColumn(col));
                for (ColumnMetaData columnMetaData : columnList) {
                    TableColumn<Map<String, ObjectProperty>, ObjectProperty> tableColumn = new DataColumn(columnMetaData);
                    tableColumn.setEditable(true);
                    tableColumn.setSortable(false);
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

            originalItems = new ArrayList<>(value.getDataList());
            originalValues = new ArrayList<>();
            for (Map<String, ObjectProperty> row : value.getDataList()) {
                Map<String, Object> snapshot = new LinkedHashMap<>();
                for (Map.Entry<String, ObjectProperty> entry : row.entrySet()) {
                    snapshot.put(entry.getKey(), entry.getValue().get());
                }
                originalValues.add(snapshot);
            }
            addedRows.clear();
            deletedRows.clear();
            dirty.set(false);

            for (Map<String, ObjectProperty> row : originalItems) {
                for (ObjectProperty<?> prop : row.values()) {
                    prop.addListener((obs, oldVal, newVal) -> {
                        if (!Objects.equals(oldVal, newVal)) {
                            dirty.set(true);
                        }
                    });
                }
            }

            try {
                primaryKeyColumns = metadataProvider.getPrimaryKeyColumns(
                        tableMetaData.getTableCat(),
                        tableMetaData.getTypeSchema(),
                        tableMetaData.getTableName()
                );
            } catch (SQLException e) {
                logger.error("Failed to get primary keys", e);
                primaryKeyColumns = new ArrayList<>();
            }
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


    private class PageChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            if (newValue != null) {
                TaskManger.execute(new RefreshDataTableTask(newValue.intValue(), PAGE_SIZE));
            }
        }
    }

    private class CommitTask extends BaseTask<Void> {

        @Override
        protected Void call() throws Exception {
            if (deletedRows.isEmpty() && addedRows.isEmpty() && !hasUpdates()) {
                return null;
            }

            try (Connection connection = metadataProvider.getDataSource().getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                String quoteString = metaData.getIdentifierQuoteString();
                String separator = metaData.getCatalogSeparator();
                String tableName = quoteString + tableMetaData.getTableCat() + quoteString + separator
                        + quoteString + tableMetaData.getTableName() + quoteString;

                connection.setAutoCommit(false);

                try {
                    for (Map<String, ObjectProperty> row : deletedRows) {
                        executeDelete(connection, tableName, quoteString, row);
                    }

                    for (Map<String, ObjectProperty> row : addedRows) {
                        executeInsert(connection, tableName, quoteString, row);
                    }

                    executeUpdates(connection, tableName, quoteString);

                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                } finally {
                    connection.setAutoCommit(true);
                }
            }
            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            deletedRows.clear();
            addedRows.clear();
            dirty.set(false);
            refresh();
        }

        private boolean hasUpdates() {
            for (int i = 0; i < originalItems.size(); i++) {
                Map<String, ObjectProperty> originalRow = originalItems.get(i);
                boolean isDeleted = false;
                for (Map<String, ObjectProperty> deleted : deletedRows) {
                    if (deleted == originalRow) {
                        isDeleted = true;
                        break;
                    }
                }
                if (isDeleted) {
                    continue;
                }

                Map<String, ObjectProperty> currentRow = null;
                for (Map<String, ObjectProperty> item : tableView.getItems()) {
                    if (item == originalRow) {
                        currentRow = item;
                        break;
                    }
                }
                if (currentRow == null) {
                    continue;
                }

                Map<String, Object> originalValue = originalValues.get(i);
                for (ColumnMetaData col : columnDataCache) {
                    String colName = col.getColumnName();
                    if (!Objects.equals(originalValue.get(colName), currentRow.get(colName).get())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private void executeDelete(Connection connection, String tableName, String quoteString,
                               Map<String, ObjectProperty> row) throws SQLException {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(tableName).append(" WHERE ");
        List<Object> values = new ArrayList<>();

        List<String> whereColumns = primaryKeyColumns.isEmpty()
                ? columnDataCache.stream().map(ColumnMetaData::getColumnName).collect(Collectors.toList())
                : primaryKeyColumns;

        for (String colName : whereColumns) {
            ObjectProperty prop = row.get(colName);
            Object value = prop != null ? prop.get() : null;
            if (value == null) {
                sql.append(quoteString).append(colName).append(quoteString).append(" IS NULL AND ");
            } else {
                sql.append(quoteString).append(colName).append(quoteString).append(" = ? AND ");
                values.add(value);
            }
        }
        sql.setLength(sql.length() - 5);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            ps.executeUpdate();
        }
    }

    private void executeInsert(Connection connection, String tableName, String quoteString,
                               Map<String, ObjectProperty> row) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        List<Object> values = new ArrayList<>();

        for (ColumnMetaData col : columnDataCache) {
            sql.append(quoteString).append(col.getColumnName()).append(quoteString).append(", ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(") VALUES (");

        for (ColumnMetaData col : columnDataCache) {
            sql.append("?, ");
            ObjectProperty prop = row.get(col.getColumnName());
            values.add(prop != null ? prop.get() : null);
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            ps.executeUpdate();
        }
    }

    private void executeUpdates(Connection connection, String tableName, String quoteString) throws SQLException {
        for (int i = 0; i < originalItems.size(); i++) {
            Map<String, ObjectProperty> originalRow = originalItems.get(i);
            boolean isDeleted = false;
            for (Map<String, ObjectProperty> deleted : deletedRows) {
                if (deleted == originalRow) {
                    isDeleted = true;
                    break;
                }
            }
            if (isDeleted) {
                continue;
            }

            Map<String, ObjectProperty> currentRow = null;
            for (Map<String, ObjectProperty> item : tableView.getItems()) {
                if (item == originalRow) {
                    currentRow = item;
                    break;
                }
            }
            if (currentRow == null) {
                continue;
            }

            Map<String, Object> originalValue = originalValues.get(i);
            List<String> changedColumns = new ArrayList<>();
            List<Object> changedValues = new ArrayList<>();

            for (ColumnMetaData col : columnDataCache) {
                String colName = col.getColumnName();
                Object oldVal = originalValue.get(colName);
                Object newVal = currentRow.get(colName) != null ? currentRow.get(colName).get() : null;
                if (!Objects.equals(oldVal, newVal)) {
                    changedColumns.add(colName);
                    changedValues.add(newVal);
                }
            }

            if (changedColumns.isEmpty()) {
                continue;
            }

            StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
            for (String colName : changedColumns) {
                sql.append(quoteString).append(colName).append(quoteString).append(" = ?, ");
            }
            sql.setLength(sql.length() - 2);
            sql.append(" WHERE ");

            List<String> whereColumns = primaryKeyColumns.isEmpty()
                    ? columnDataCache.stream().map(ColumnMetaData::getColumnName).collect(Collectors.toList())
                    : primaryKeyColumns;

            List<Object> whereValues = new ArrayList<>();
            for (String colName : whereColumns) {
                Object value = originalValue.get(colName);
                if (value == null) {
                    sql.append(quoteString).append(colName).append(quoteString).append(" IS NULL AND ");
                } else {
                    sql.append(quoteString).append(colName).append(quoteString).append(" = ? AND ");
                    whereValues.add(value);
                }
            }
            sql.setLength(sql.length() - 5);

            try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
                int paramIndex = 1;
                for (Object val : changedValues) {
                    ps.setObject(paramIndex++, val);
                }
                for (Object val : whereValues) {
                    ps.setObject(paramIndex++, val);
                }
                ps.executeUpdate();
            }
        }
    }

}
