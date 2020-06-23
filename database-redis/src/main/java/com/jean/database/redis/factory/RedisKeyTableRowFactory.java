package com.jean.database.redis.factory;

import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.view.handler.IRedisKeyActionEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;


public class RedisKeyTableRowFactory implements Callback<TableView<RedisKey>, TableRow<RedisKey>> {

    private final IRedisKeyActionEventHandler handler;

    public RedisKeyTableRowFactory(IRedisKeyActionEventHandler handler) {
        this.handler = handler;
    }

    @Override
    public TableRow<RedisKey> call(TableView<RedisKey> param) {
        return new RedisKeyTableRow(handler);
    }

    private static class RedisKeyTableRow extends TableRow<RedisKey> implements ChangeListener<Boolean>, EventHandler<MouseEvent> {

        private final IRedisKeyActionEventHandler handler;

        private RedisKeyTableRow(IRedisKeyActionEventHandler handler) {
            this.handler = handler;
            this.selectedProperty().addListener(this);
            this.setOnMouseClicked(this);
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
