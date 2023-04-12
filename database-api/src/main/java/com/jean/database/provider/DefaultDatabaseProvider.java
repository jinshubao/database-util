package com.jean.database.provider;

import com.jean.database.context.ApplicationContext;

public abstract class DefaultDatabaseProvider implements IDatabaseProvider {

    private ApplicationContext context;

    @Override
    public void init(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public ApplicationContext getContext() {
        return context;
    }
}
