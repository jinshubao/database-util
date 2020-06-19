package com.jean.database.sql.view;

import com.jean.database.api.view.TableTab;
import com.jean.database.sql.meta.TableSummaries;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SQLObjectTab extends TableTab<TableSummaries> {

    public SQLObjectTab(String text) {
        super(text);
    }

    @Override
    protected TableView<TableSummaries> createTableView() {
        TableView<TableSummaries> tableView = new TableView<>();

        TableColumn<TableSummaries, String> tableName = new TableColumn<>("表名");
        tableName.setCellValueFactory(param -> param.getValue().tableNameProperty());

        TableColumn<TableSummaries, String> autoIncrement = new TableColumn<>("自动递增值");
        autoIncrement.setCellValueFactory(param -> param.getValue().autoIncrementProperty());

        TableColumn<TableSummaries, String> modifyTime = new TableColumn<>("自动递增值");
        modifyTime.setCellValueFactory(param -> param.getValue().modifyTimeProperty());

        TableColumn<TableSummaries, String> dataLength = new TableColumn<>("数据长度");
        dataLength.setCellValueFactory(param -> param.getValue().dataLengthProperty());

        TableColumn<TableSummaries, String> tableType = new TableColumn<>("数据长度");
        tableType.setCellValueFactory(param -> param.getValue().tableTypeProperty());

        TableColumn<TableSummaries, String> tableRows = new TableColumn<>("数据长度");
        tableRows.setCellValueFactory(param -> param.getValue().tableRowsProperty());

        TableColumn<TableSummaries, String> comments = new TableColumn<>("数据长度");
        comments.setCellValueFactory(param -> param.getValue().commentsProperty());

        tableView.getColumns().add(tableName);
        tableView.getColumns().add(autoIncrement);
        tableView.getColumns().add(modifyTime);
        tableView.getColumns().add(dataLength);
        tableView.getColumns().add(tableType);
        tableView.getColumns().add(tableRows);
        tableView.getColumns().add(comments);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableView;
    }
}
