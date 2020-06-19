package com.jean.database.redis.factory;

import com.jean.database.redis.model.RedisValue;
import com.jean.database.redis.view.handler.IRedisValueActionEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;


public class RedisValueTableRowFactory implements Callback<TableView<RedisValue>, TableRow<RedisValue>> {

    private final IRedisValueActionEventHandler handler;

    public RedisValueTableRowFactory(IRedisValueActionEventHandler handler) {
        this.handler = handler;
    }

    @Override
    public TableRow<RedisValue> call(TableView<RedisValue> param) {
        return new RedisKeyTableRow(handler);
    }

    private static class RedisKeyTableRow extends TableRow<RedisValue> {

        private final ChangeListener<Boolean> changeListener;
        private final EventHandler<MouseEvent> mouseEventEventHandler;

        private RedisKeyTableRow(IRedisValueActionEventHandler handler) {

            this.changeListener = (observable, oldValue, newValue) -> {
                if (newValue) {
                    handler.onSelected(RedisKeyTableRow.this);
                }
            };
            selectedProperty().addListener(new WeakChangeListener<>(this.changeListener));

            this.mouseEventEventHandler = event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (event.getClickCount() == 1) {
                        handler.onClick(RedisKeyTableRow.this);
                    } else if (event.getClickCount() == 2) {
                        handler.onDoubleClick(RedisKeyTableRow.this);
                    }
                }
            };
            setOnMouseClicked(new WeakEventHandler<>(this.mouseEventEventHandler));
        }
    }
}
