package com.jean.database.sql.view;

import com.jean.database.api.KeyValuePair;
import com.jean.database.api.view.TableTab;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SQLGeneralInfoTab extends TableTab<KeyValuePair<String, Object>> {

    public SQLGeneralInfoTab(String text) {
        super(text);
    }

    @Override
    protected TableView<KeyValuePair<String, Object>> createTableView() {
        TableColumn<KeyValuePair<String, Object>, String> keyColumn = new TableColumn<>("表名");
        keyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey(), "key"));

        TableColumn<KeyValuePair<String, Object>, Object> valueColumn = new TableColumn<>("表名");
        valueColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue(), "key"));

        TableView<KeyValuePair<String, Object>>  tableView = new TableView<>();
        tableView.getColumns().add(keyColumn);
        tableView.getColumns().add(valueColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return tableView;
    }
}
