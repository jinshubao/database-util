package com.jean.database.api;

public interface IDatabaseProvider {

    void init();

    String getIdentifier();

    String getName();

    String getIcon();

    void close();

    /**
     * 排序字段
     *
     * @return 排序
     */
    default int getOrder() {
        return 0;
    }

}
