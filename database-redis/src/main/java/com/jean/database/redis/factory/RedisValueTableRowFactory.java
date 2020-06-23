package com.jean.database.redis.factory;

import com.jean.database.redis.model.RedisValue;
import com.jean.database.redis.view.handler.IRedisValueActionEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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

    private static class RedisKeyTableRow extends TableRow<RedisValue> implements ChangeListener<Boolean>, EventHandler<MouseEvent> {

        private final IRedisValueActionEventHandler handler;

        private RedisKeyTableRow(IRedisValueActionEventHandler handler) {
            this.handler = handler;

            selectedProperty().addListener(this);

            setOnMouseClicked(this);
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (newValue) {
                handler.onSelected(this);
            }
        }

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 1) {
                    handler.onClick(this);
                } else if (event.getClickCount() == 2) {
                    handler.onDoubleClick(this);
                }
            }
        }
    }
}
