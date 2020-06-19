package com.jean.database.redis.view;

import com.jean.database.api.TableViewRowIndexColumnCellFactory;
import com.jean.database.api.view.TableTab;
import com.jean.database.redis.factory.RedisKeyTableRowFactory;
import com.jean.database.redis.factory.TableViewByteColumnCellFactory;
import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.view.handler.impl.RedisKeyActionEventHandlerImpl;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RedisKeyTableTab extends TableTab<RedisKey> {

    public RedisKeyTableTab(String text) {
        super(text);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected TableView<RedisKey> createTableView() {
        TableView<RedisKey> tableView = new TableView<>();
        TableColumn<RedisKey, Integer> keyNoColumn = new TableColumn<>();
        keyNoColumn.setCellFactory(new TableViewRowIndexColumnCellFactory<>());
        TableColumn<RedisKey, byte[]> keyColumn = new TableColumn<>();
        keyColumn.setCellFactory(new TableViewByteColumnCellFactory<>());
        keyColumn.setCellValueFactory(param -> param.getValue().keyProperty());
        TableColumn<RedisKey, String> typeColumn = new TableColumn<>();
        typeColumn.setCellValueFactory(param -> param.getValue().typeProperty());
        typeColumn.setVisible(false);
        TableColumn<RedisKey, Number> sizeColumn = new TableColumn<>();
        sizeColumn.setCellValueFactory(param -> param.getValue().sizeProperty());
        sizeColumn.setVisible(false);
        TableColumn<RedisKey, Number> ttlColumn = new TableColumn<>();
        ttlColumn.setCellValueFactory(param -> param.getValue().ttlProperty());
        ttlColumn.setVisible(false);
        tableView.setRowFactory(new RedisKeyTableRowFactory(new RedisKeyActionEventHandlerImpl()));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(keyNoColumn, keyColumn, typeColumn, sizeColumn, ttlColumn);
        return tableView;
    }
}
