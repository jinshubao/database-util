package com.jean.database.gui.view;


import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.factory.TableCellFactory;
import com.jean.database.gui.view.handler.IDataTableActionEventHandler;
import com.jean.database.gui.view.handler.impl.DataTableActionEventHandlerImpl;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 */
public class DataTableTab extends Tab {

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    private final TableView<Map<String, Object>> tableView;
    private final Pagination pagination;
    private final TableMetaData tableMetaData;
    private final IDataTableActionEventHandler dataTableActionEventHandler;

    public DataTableTab(TableMetaData value, IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider,
                        List<ColumnMetaData> columnMetaDataList) {
        this.tableMetaData = value;

        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;

        tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        for (ColumnMetaData columnMetaData : columnMetaDataList) {
            TableColumn<Map<String, Object>, Object> tableColumn = new DataTableTab.DataColumn(columnMetaData);
            tableColumn.setCellFactory(TableCellFactory.forTableView());
            String columnName = columnMetaData.getColumnName();
            tableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().get(columnName)));
            tableView.getColumns().add(tableColumn);
        }

        this.pagination = new Pagination(0, 0);
        pagination.setVisible(false);

        dataTableActionEventHandler = LoggerWrapper.warp(new DataTableActionEventHandlerImpl());
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> dataTableActionEventHandler.refresh(DataTableTab.this, newValue.intValue()));

        setId(value.getFullName());
        setClosable(true);
        setText(value.getTableName());
        setTooltip(new Tooltip(value.getFullName()));
        setContent(new VBox(tableView, pagination));
        dataTableActionEventHandler.refresh(this);
    }


    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }


    public IConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    public IMetadataProvider getMetadataProvider() {
        return metadataProvider;
    }

    public void updateItems(List<Map<String, Object>> items) {
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

    private static class DataColumn extends TableColumn<Map<String, Object>, Object> {
        public DataColumn(ColumnMetaData columnMetaData) {
            super(columnMetaData.getColumnName());
        }
    }
}
