package com.jean.database.gui.view;


import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.view.handler.IDataTableActionEventHandler;
import javafx.css.StyleableProperty;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * @author jinshubao
 */
public class DataTableView extends VBox {

    private final TableView<Map<String, Object>> tableView;

    private final Pagination pagination;

    private final TableMetaData tableMetaData;

    public DataTableView(TableMetaData tableMetaData, IDataTableActionEventHandler eventHandler) {
        this.tableMetaData = tableMetaData;
        this.setFocusTraversable(false);
        tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        this.pagination = new Pagination(0, 0);
        pagination.setVisible(false);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> eventHandler.refresh(DataTableView.this, newValue.intValue()));

        getChildren().addAll(tableView, pagination);
        eventHandler.refresh(this);
    }


    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public TableView<Map<String, Object>> getTableView() {
        return tableView;
    }

    public Pagination getPagination() {
        return pagination;
    }


    public static class DataColumn extends TableColumn<Map<String, Object>, Object> {

        private final ColumnMetaData columnMetaData;

        public DataColumn(ColumnMetaData columnMetaData) {
            super(columnMetaData.getColumnName());
            this.columnMetaData = columnMetaData;
        }
    }
}
