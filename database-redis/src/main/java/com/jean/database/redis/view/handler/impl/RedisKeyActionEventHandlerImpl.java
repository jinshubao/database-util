package com.jean.database.redis.view.handler.impl;

import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.view.handler.IRedisKeyActionEventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.HashMap;
import java.util.Map;

public class RedisKeyActionEventHandlerImpl implements IRedisKeyActionEventHandler {

    @Override
    public void copy(TableRow<RedisKey> tableRow) {
        RedisKey redisKey = tableRow.getItem();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, new String(redisKey.getKey()));
        clipboard.setContent(content);
    }

    @Override
    public void delete(TableRow<RedisKey> tableRow) {
        tableRow.getTableView().getItems().remove(tableRow.getItem());
    }

    @Override
    public void onClick(TableRow<RedisKey> redisKeyTableRow) {

    }

    @Override
    public void onDoubleClick(TableRow<RedisKey> redisKeyTableRow) {

    }

    @Override
    public void onSelected(TableRow<RedisKey> redisKeyTableRow) {

    }
}
