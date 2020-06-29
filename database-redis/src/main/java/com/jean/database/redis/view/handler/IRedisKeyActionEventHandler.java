package com.jean.database.redis.view.handler;

import com.jean.database.api.IMouseEventHandler;
import com.jean.database.redis.model.RedisKey;
import javafx.scene.control.TableRow;

/**
 * Redis key 列表 事件处理器
 *
 * @author jinshubao
 */
public interface IRedisKeyActionEventHandler extends IMouseEventHandler<TableRow<RedisKey>> {

    /**
     * 复制
     *
     * @param tableRow tableRow
     */
    void copy(TableRow<RedisKey> tableRow);

    /**
     * 删除
     *
     * @param tableRow tableRow
     */
    void delete(TableRow<RedisKey> tableRow);
}
