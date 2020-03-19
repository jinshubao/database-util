package com.jean.database.gui.view.handler;

/**
 * @author jinshubao
 */
public interface ICommonActionEventHandler<T> {

    /**
     * 新增
     */
    void onCreate(T t);

    /**
     * 打开
     */
    void onOpen(T t);

    /**
     * 关闭
     */
    void onClose(T t);

    /**
     * 删除
     */
    void onDelete(T t);

    /**
     * 详情
     */
    void onDetails(T t);


}
