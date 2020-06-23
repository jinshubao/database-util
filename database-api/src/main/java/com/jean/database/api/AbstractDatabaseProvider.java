package com.jean.database.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDatabaseProvider implements IDatabaseProvider {

    public static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseProvider.class);

    private ViewContext viewContext;

    @Override
    public void init(ViewContext viewContext) {
        logger.debug("{} provider init", getName());
        this.viewContext = viewContext;
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public void close() {

    }

    public ViewContext getViewContext() {
        return viewContext;
    }
}
