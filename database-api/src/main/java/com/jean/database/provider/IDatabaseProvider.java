package com.jean.database.provider;

import com.jean.database.context.ApplicationContext;

public interface IDatabaseProvider {

    void init(ApplicationContext context);

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

    ApplicationContext getContext();

}
