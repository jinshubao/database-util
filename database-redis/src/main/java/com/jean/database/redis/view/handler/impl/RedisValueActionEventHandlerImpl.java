package com.jean.database.redis.view.handler.impl;

import com.jean.database.redis.model.RedisValue;
import com.jean.database.redis.view.handler.IRedisValueActionEventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.HashMap;
import java.util.Map;

public class RedisValueActionEventHandlerImpl implements IRedisValueActionEventHandler {

    @Override
    public void copy(TableRow<RedisValue> tableRow) {
        RedisValue redisValue = tableRow.getItem();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, new String(redisValue.getValue()));
        clipboard.setContent(content);
    }

    @Override
    public void delete(TableRow<RedisValue> tableRow) {

    }

    @Override
    public void onClick(TableRow<RedisValue> redisValueTableRow) {

    }

    @Override
    public void onDoubleClick(TableRow<RedisValue> redisValueTableRow) {

    }

    @Override
    public void onSelected(TableRow<RedisValue> redisValueTableRow) {

    }
}
