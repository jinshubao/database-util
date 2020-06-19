package com.jean.database.redis.view.handler;

import com.jean.database.api.view.action.IRefreshable;
import com.jean.database.api.view.handler.ICommonActionEventHandler;
import com.jean.database.api.view.handler.IMouseEventHandler;
import com.jean.database.redis.view.RedisDatabaseItem;

/**
 * 服务器信息节点事件处理器
 *
 * @author jinshubao
 */
public interface IRedisDatabaseItemActionEventHandler extends IMouseEventHandler<RedisDatabaseItem>, ICommonActionEventHandler<RedisDatabaseItem>, IRefreshable {

    void flush(RedisDatabaseItem redisDatabaseItem);

    void refresh(RedisDatabaseItem redisDatabaseItem);
}
