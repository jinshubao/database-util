package com.jean.database.client.view;


import com.jean.database.client.factory.TableCellFactory;
import com.jean.database.core.IDataProvider;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 */
public class CustomTableView extends VBox implements IRefresh {

    private static final Integer PAGE_SIZE = 1000;

    private Connection connection;
    private IMetadataProvider metadataProvider;
    private IDataProvider dataProvider;

    private CatalogMetaData catalogMetaData;
    private SchemaMetaData schemaMetaData;
    private String tableNamePattern;
    private TableView<Map<String, String>> tableView;
    private Pagination pagination;

    public CustomTableView(Connection connection, IMetadataProvider metadataProvider, IDataProvider dataProvider, CatalogMetaData catalogMetaData, SchemaMetaData schemaMetaData, String tableNamePattern) throws Exception {
        this.connection = connection;
        this.metadataProvider = metadataProvider;
        this.dataProvider = dataProvider;
        this.catalogMetaData = catalogMetaData;
        this.schemaMetaData = schemaMetaData;

        tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        List<ColumnMetaData> columns = metadataProvider.getColumnMetaData(connection, catalogMetaData.getTableCat(), schemaMetaData != null ? schemaMetaData.getTableSchem() : null, tableNamePattern);
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
        this.tableNamePattern = tableNamePattern;
        getChildren().add(tableView);
        int pageCount = getPageCount(PAGE_SIZE);
        if (pageCount > 0) {
            pagination = new Pagination(pageCount, 0);
            pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
                refreshData(PAGE_SIZE, newValue.intValue());
            });
            getChildren().add(pagination);
        }
    }

    @Override
    public void refresh() {
        refreshData(PAGE_SIZE, 0);
    }

    public void refreshData(int pageSize, int pageIndex) {
        try {
            tableView.getItems().clear();
            List<Map<String, String>> tableRows = dataProvider.getTableRows(connection,
                    catalogMetaData.getTableCat(),
                    schemaMetaData != null ? schemaMetaData.getTableSchem() : null,
                    tableNamePattern,
                    pageSize, pageIndex);
            tableView.getItems().addAll(tableRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getPageCount(int pageSize) {
        try {
            int count = dataProvider.getTableRowCount(connection,
                    catalogMetaData.getTableCat(),
                    schemaMetaData != null ? schemaMetaData.getTableSchem() : null,
                    tableNamePattern);
            return count / pageSize + ((count % pageSize) > 0 ? 1 : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
