package com.jean.database.gui.handler.impl;

import com.jean.database.core.IDataProvider;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.factory.TableCellFactory;
import com.jean.database.gui.handler.DataTableActionEventHandler;
import com.jean.database.gui.utils.DialogUtil;
import com.jean.database.gui.view.CustomTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DataTableActionEventHandlerImpl implements DataTableActionEventHandler {

    private static final Integer PAGE_SIZE = 1000;

    private final Connection connection;
    private final IMetadataProvider metadataProvider;
    private final IDataProvider dataProvider;

    public DataTableActionEventHandlerImpl(Connection connection, IMetadataProvider metadataProvider, IDataProvider dataProvider) {
        this.connection = connection;
        this.metadataProvider = metadataProvider;
        this.dataProvider = dataProvider;
    }


    @Override
    public void refresh(CustomTableView customTableView) {
        TableView<Map<String, String>> tableView = customTableView.getTableView();
        TableMetaData tableMetaData = customTableView.getTableMetaData();
        try {
            List<ColumnMetaData> columns = metadataProvider.getColumnMetaData(connection, tableMetaData.getTableCat(),
                    tableMetaData.getTableSchem(), tableMetaData.getTableName());
            for (ColumnMetaData column : columns) {
                String columnName = column.getColumnName();
                TableColumn<Map<String, String>, String> tableColumn = new TableColumn<>(columnName);
                tableColumn.setPrefWidth(100d);
                tableColumn.setMinWidth(50d);
                tableColumn.setCellFactory(TableCellFactory.forTableView());
                tableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(columnName)));
                tableView.getColumns().add(tableColumn);
            }
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableView.getSelectionModel().setCellSelectionEnabled(true);
            int pageCount = getPageCount(tableMetaData);
            customTableView.getPagination().setPageCount(pageCount);
            customTableView.getPagination().setVisible(pageCount > 1);
            if (pageCount > 0) {
                this.refresh(customTableView, 0);
            }
        } catch (SQLException e) {
            DialogUtil.error("ERROR", e.getMessage(), e);
        }
    }

    protected int getPageCount(TableMetaData tableMetaData) throws SQLException {
        int count = dataProvider.getTableRowCount(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName());
        return count / PAGE_SIZE + ((count % PAGE_SIZE) > 0 ? 1 : 0);
    }

    public void refresh(CustomTableView customTableView, int page) {
        TableView<Map<String, String>> tableView = customTableView.getTableView();
        tableView.getItems().clear();
        TableMetaData tableMetaData = customTableView.getTableMetaData();
        try {
            List<Map<String, String>> tableRows = dataProvider.getTableRows(connection,
                    tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName(), PAGE_SIZE, page);
            tableView.getItems().addAll(tableRows);
        } catch (SQLException e) {
            DialogUtil.error("ERROR", e.getMessage(), e);
        }
    }

}
