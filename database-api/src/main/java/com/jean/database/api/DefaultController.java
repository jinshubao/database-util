package com.jean.database.api;

/**
 * Controller 基类，支持通过 ControllerContext 初始化
 */
public abstract class DefaultController implements IController {

    private ControllerContext context;

    /**
     * 通过 ControllerContext 构造
     *
     * @param context Controller 上下文
     */
    public DefaultController(ControllerContext context) {
        this.context = context;
    }

    /**
     * 获取 ControllerContext
     */
    public ControllerContext getContext() {
        return context;
    }

    /**
     * 获取 ViewContext（便捷方法）
     */
    public ViewContext getViewContext() {
        return context != null ? context.getViewContext() : null;
    }

    /**
     * 获取标题（便捷方法）
     */
    public String getTitle() {
        return context != null ? context.getTitle() : null;
    }
}
