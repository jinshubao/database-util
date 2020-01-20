package com.jean.database.gui.view;


import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.handler.DataTableActionEventHandler;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * @author jinshubao
 */
public class CustomTableView extends VBox {

    private final TableView<Map<String, String>> tableView;

    private final Pagination pagination;

    private final TableMetaData tableMetaData;

    public CustomTableView(TableMetaData tableMetaData, DataTableActionEventHandler eventHandler) {
        this.tableMetaData = tableMetaData;
        tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        this.pagination = new Pagination(0, 0);
        pagination.setVisible(false);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> eventHandler.refresh(CustomTableView.this, newValue.intValue()));

        getChildren().addAll(tableView, pagination);
        eventHandler.refresh(this);
    }


    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public TableView<Map<String, String>> getTableView() {
        return tableView;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
