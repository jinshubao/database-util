package com.jean.database.redis.view.handler.impl;

import com.jean.database.redis.RedisConstant;
import com.jean.database.redis.RedisDatabaseTabController;
import com.jean.database.redis.model.RedisValue;
import com.jean.database.redis.view.handler.IRedisValueActionEventHandler;
import javafx.scene.control.TableRow;

public class RedisValueActionEventHandlerImpl implements IRedisValueActionEventHandler {

    private final RedisDatabaseTabController objectTabController;

    public RedisValueActionEventHandlerImpl(RedisDatabaseTabController objectTabController) {
        this.objectTabController = objectTabController;
    }

    @Override
    public void onClick(TableRow<RedisValue> tableRow) {

    }

    @Override
    public void onDoubleClick(TableRow<RedisValue> tableRow) {

    }

    @Override
    public void onSelected(TableRow<RedisValue> tableRow) {
        RedisValue item = tableRow.getItem();
        if (item.getKey() != null) {
            objectTabController.keyTextFiled.setText(new String(item.getKey(), RedisConstant.CHARSET_UTF8));
        } else {
            objectTabController.keyTextFiled.setText(null);
        }
        if (item.getValue() != null) {
            objectTabController.valueTextArea.setText(new String(item.getValue(), RedisConstant.CHARSET_UTF8));
        } else {
            objectTabController.valueTextArea.setText(null);
        }
    }

    @Override
    public void copy(TableRow<RedisValue> tableRow) {

    }

    @Override
    public void delete(TableRow<RedisValue> tableRow) {

    }
}
