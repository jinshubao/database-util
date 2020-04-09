package com.jean.database.gui.view.handler;

/**
 * @author jinshubao
 */
public interface IRefreshActionEventHandler<T> {

    /**
     * 刷新
     */
    default void onRefresh(T t) {
    }

}
