package com.jean.database.api.view.handler;

/**
 * @author jinshubao
 */
public interface ICommonActionEventHandler<T> {

    /**
     * 新增
     */
    default void onCreate(T t) {
    }

    /**
     * 打开
     */
    default void onOpen(T t) {
    }

    /**
     * 关闭
     */
    default void onClose(T t) {
    }

    /**
     * 删除
     */
    default void onDelete(T t) {
    }

    /**
     * 详情
     */
    default void onDetails(T t) {
    }


}
