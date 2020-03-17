package com.jean.database.gui.handler.impl;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.factory.TableCellFactory;
import com.jean.database.gui.handler.IDataTableActionEventHandler;
import com.jean.database.gui.view.DataTableView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataTableActionEventHandlerImpl implements IDataTableActionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataTableActionEventHandlerImpl.class);

    private static final Integer PAGE_SIZE = 1000;

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    public DataTableActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider) {
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
    }


    @Override
    public void refresh(DataTableView dataTableView) {
        TableView<Map<String, Object>> tableView = dataTableView.getTableView();
        TableMetaData tableMetaData = dataTableView.getTableMetaData();
        try (Connection connection = metadataProvider.getConnection(this.connectionConfiguration)) {
            List<ColumnMetaData> columnMetaDataList = metadataProvider.getColumnMetaData(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName());
            List<TableColumn<Map<String, Object>, Object>> columns = new ArrayList<>(columnMetaDataList.size());
            for (ColumnMetaData columnMetaData : columnMetaDataList) {
                TableColumn<Map<String, Object>, Object> tableColumn = new DataTableView.DataColumn(columnMetaData);
                tableColumn.setCellFactory(TableCellFactory.forTableView());
                String columnName = columnMetaData.getColumnName();
                tableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(columnName)));
                columns.add(tableColumn);
            }
            tableView.getColumns().addAll(columns);
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableView.getSelectionModel().setCellSelectionEnabled(true);
            int pageCount = getPageCount(tableMetaData);
            dataTableView.getPagination().setPageCount(pageCount);
            dataTableView.getPagination().setVisible(pageCount > 1);
            if (pageCount > 0) {
                this.refresh(dataTableView, 0);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            DialogUtil.error(e);
        }
    }

    private int getPageCount(TableMetaData tableMetaData) throws SQLException {
        try (Connection connection = metadataProvider.getConnection(this.connectionConfiguration)) {
            int count = metadataProvider.getTableRowCount(connection, tableMetaData);
            return count / PAGE_SIZE + ((count % PAGE_SIZE) > 0 ? 1 : 0);
        }
    }

    public void refresh(DataTableView dataTableView, int page) {
        TableView<Map<String, Object>> tableView = dataTableView.getTableView();
        tableView.getItems().clear();
        TableMetaData tableMetaData = dataTableView.getTableMetaData();
        try (Connection connection = metadataProvider.getConnection(this.connectionConfiguration)) {
            List<Map<String, Object>> tableRows = metadataProvider.getTableRows(connection, tableMetaData, PAGE_SIZE, page);
            tableView.getItems().addAll(tableRows);
        } catch (SQLException e) {
            DialogUtil.error(e);
        }
    }

}
