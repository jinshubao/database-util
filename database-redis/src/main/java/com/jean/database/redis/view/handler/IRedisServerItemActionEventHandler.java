package com.jean.database.redis.view.handler;


import com.jean.database.api.view.handler.IMouseEventHandler;
import com.jean.database.redis.view.RedisServerItem;

/**
 * 服务器信息节点事件处理器
 *
 * @author jinshubao
 */
public interface IRedisServerItemActionEventHandler extends IMouseEventHandler<RedisServerItem> {

    /**
     * @param treeItem treeItem
     */
    void open(RedisServerItem treeItem);

    /**
     * @param treeItem treeItem
     */
    void close(RedisServerItem treeItem);

    /**
     * @param treeItem treeItem
     */
    void delete(RedisServerItem treeItem);

    /**
     * @param treeItem treeItem
     */
    void property(RedisServerItem treeItem);


}
