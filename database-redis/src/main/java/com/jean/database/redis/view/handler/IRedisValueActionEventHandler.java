package com.jean.database.redis.view.handler;

import com.jean.database.redis.model.RedisValue;
import javafx.scene.control.TableRow;

/**
 * Redis value 列表 事件处理器
 *
 * @author jinshubao
 */
public interface IRedisValueActionEventHandler {

    void onClick(TableRow<RedisValue> t);

    void onDoubleClick(TableRow<RedisValue> t);

    void onSelected(TableRow<RedisValue> t);

    /**
     * 复制
     *
     * @param tableRow tableRow
     */
    void copy(TableRow<RedisValue> tableRow);

    /**
     * 删除
     *
     * @param tableRow tableRow
     */
    void delete(TableRow<RedisValue> tableRow);
}
