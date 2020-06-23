package com.jean.database.api.view;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

public abstract class TableViewTab<T> extends Tab {

    private final TableView<T> tableView;

    public TableViewTab(String text) {
        super(text);
        TableView<T> tableView = createTableView();
        this.tableView = tableView;
        setContent(tableView);
    }

    protected abstract TableView<T> createTableView();

    public void setItems(ObservableList<T> value) {
        tableView.setItems(value);
    }

    public ObservableList<T> getItems() {
        return tableView.getItems();
    }

    public void cleanItems() {
        tableView.getItems().clear();
    }

    public boolean removeItems(T... items) {
        return tableView.getItems().removeAll(items);
    }
}
