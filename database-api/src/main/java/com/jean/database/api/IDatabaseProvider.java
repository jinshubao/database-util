package com.jean.database.api;

public interface IDatabaseProvider {

    void init();

    String getIdentifier();

    String getName();

    /**
     * 排序字段
     *
     * @return 排序
     */
    default int getOrder() {
        return 0;
    }

    void setViewContext(ViewContext context);

    ViewContext getViewContext();

}
