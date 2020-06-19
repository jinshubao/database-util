package com.jean.database.sql.view;


import com.jean.database.api.LoggerWrapper;
import com.jean.database.api.AbstractConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.factory.TableCellFactory;
import com.jean.database.sql.meta.ColumnMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.view.handler.IDataTableActionEventHandler;
import com.jean.database.sql.view.handler.impl.DataTableActionEventHandlerImpl;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 */
public class SQLDataTableTab extends Tab {

    private final AbstractConnectionConfiguration connectionConfiguration;
    private final SQLMetadataProvider metadataProvider;

    private final TableView<Map<String, ObjectProperty>> tableView;
    private final Pagination pagination;
    private final TableMetaData tableMetaData;
    private final IDataTableActionEventHandler dataTableActionEventHandler;

    public SQLDataTableTab(TableMetaData value, AbstractConnectionConfiguration connectionConfiguration,
                           SQLMetadataProvider metadataProvider,
                           List<ColumnMetaData> columnMetaDataList) {
        this.tableMetaData = value;

        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;

        tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(false);

        VBox.setVgrow(tableView, Priority.ALWAYS);

        for (ColumnMetaData columnMetaData : columnMetaDataList) {
            TableColumn<Map<String, ObjectProperty>, ObjectProperty> tableColumn = new DataColumn(columnMetaData);
            tableColumn.setEditable(true);
            tableColumn.setCellFactory(TableCellFactory.forTableView());
            String columnName = columnMetaData.getColumnName();
            tableColumn.setCellValueFactory(param -> param.getValue().get(columnName));
            tableView.getColumns().add(tableColumn);
        }

        this.pagination = new Pagination(0, 0);
        pagination.setVisible(false);

        dataTableActionEventHandler = LoggerWrapper.warp(new DataTableActionEventHandlerImpl());
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> dataTableActionEventHandler.refresh(SQLDataTableTab.this, newValue.intValue()));

        setId(value.getFullName());
        setClosable(true);
        setText(value.getTableName());
        setTooltip(new Tooltip(value.getFullName()));
        setContent(new VBox(tableView, pagination));
        dataTableActionEventHandler.onRefresh(this);
    }


    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public AbstractConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    public SQLMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

    public void updateItems(List<Map<String, ObjectProperty>> items) {
        tableView.getItems().clear();
        if (items != null && !items.isEmpty()) {
            tableView.getItems().addAll(items);
        }
    }

    public int getCurrentPageIndex() {
        return pagination.getCurrentPageIndex();
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
}
