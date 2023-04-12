package com.jean.database.handler;

import com.jean.database.context.ApplicationContext;

public class AbstractActionHandler implements ActionHandler {

    private final ApplicationContext context;

    public AbstractActionHandler(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public ApplicationContext getContext() {
        return context;
    }
}
