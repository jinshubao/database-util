package com.jean.database.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller 上下文对象，用于封装传递给 Controller 的参数
 * 减少构造函数参数数量，提高代码可维护性
 */
public class ControllerContext {

    private final ViewContext viewContext;
    private final String title;
    private final Map<String, Object> attributes = new HashMap<>();

    public ControllerContext(ViewContext viewContext, String title) {
        this.viewContext = viewContext;
        this.title = title;
    }

    public ViewContext getViewContext() {
        return viewContext;
    }

    public String getTitle() {
        return title;
    }

    /**
     * 设置属性值
     *
     * @param key   属性键
     * @param value 属性值
     * @return 当前上下文对象（链式调用）
     */
    public ControllerContext setAttribute(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

    /**
     * 获取属性值
     *
     * @param key 属性键
     * @return 属性值，如果不存在返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    /**
     * 获取属性值，如果不存在返回默认值
     *
     * @param key          属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, T defaultValue) {
        return (T) attributes.getOrDefault(key, defaultValue);
    }

    /**
     * 检查是否包含指定属性
     *
     * @param key 属性键
     * @return 是否包含
     */
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    /**
     * 移除属性
     *
     * @param key 属性键
     * @return 被移除的属性值
     */
    @SuppressWarnings("unchecked")
    public <T> T removeAttribute(String key) {
        return (T) attributes.remove(key);
    }

    /**
     * 清空所有属性
     */
    public void clearAttributes() {
        attributes.clear();
    }

    /**
     * 创建 Builder 实例
     */
    public static Builder builder(ViewContext viewContext, String title) {
        return new Builder(viewContext, title);
    }

    /**
     * Builder 模式构建器
     */
    public static class Builder {
        private final ControllerContext context;

        public Builder(ViewContext viewContext, String title) {
            this.context = new ControllerContext(viewContext, title);
        }

        public Builder attribute(String key, Object value) {
            context.setAttribute(key, value);
            return this;
        }

        public ControllerContext build() {
            return context;
        }
    }
}
