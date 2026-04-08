package com.jean.database.api;

/**
 * Controller 工厂接口
 * 用于创建和初始化 Controller 实例，封装复杂的创建逻辑
 *
 * @param <T> Controller 类型
 */
@FunctionalInterface
public interface ControllerFactory<T> {

    /**
     * 创建 Controller 实例
     *
     * @param context Controller 上下文
     * @return 创建的 Controller 实例
     */
    T create(ControllerContext context);
}
