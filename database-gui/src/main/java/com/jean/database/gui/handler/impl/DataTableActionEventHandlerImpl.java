package com.jean.database.gui.handler.impl;

import com.jean.database.core.IDataProvider;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.core.utils.DialogUtil;
import com.jean.database.gui.factory.TableCellFactory;
import com.jean.database.gui.handler.IDataTableActionEventHandler;
import com.jean.database.gui.view.CustomTableView;
import com.jean.database.gui.view.DataColumn;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataTableActionEventHandlerImpl implements IDataTableActionEventHandler {

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
        TableView<Map<String, Object>> tableView = customTableView.getTableView();
        TableMetaData tableMetaData = customTableView.getTableMetaData();
        try {
            List<ColumnMetaData> columnMetaDataList = metadataProvider.getColumnMetaData(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName());

            List<TableColumn<Map<String, Object>, Object>> columns = new ArrayList<>(columnMetaDataList.size());
            for (ColumnMetaData columnMetaData : columnMetaDataList) {
                TableColumn<Map<String, Object>, Object> tableColumn = new DataColumn(columnMetaData);
                tableColumn.setCellFactory(TableCellFactory.forTableView());
                String columnName = columnMetaData.getColumnName();
                tableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(columnName)));
                columns.add(tableColumn);
            }
            tableView.getColumns().addAll(columns);
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

    private int getPageCount(TableMetaData tableMetaData) throws SQLException {
        int count = dataProvider.getTableRowCount(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName());
        return count / PAGE_SIZE + ((count % PAGE_SIZE) > 0 ? 1 : 0);
    }

    public void refresh(CustomTableView customTableView, int page) {
        TableView<Map<String, Object>> tableView = customTableView.getTableView();
        tableView.getItems().clear();
        TableMetaData tableMetaData = customTableView.getTableMetaData();
        try {
            List<Map<String, Object>> tableRows = dataProvider.getTableRows(connection,
                    tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName(), PAGE_SIZE, page);
            tableView.getItems().addAll(tableRows);
        } catch (SQLException e) {
            DialogUtil.error("ERROR", e.getMessage(), e);
        }
    }

}
