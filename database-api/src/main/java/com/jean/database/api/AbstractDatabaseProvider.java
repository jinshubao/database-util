package com.jean.database.api;

import com.jean.database.api.view.ViewContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDatabaseProvider implements IDatabaseProvider {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(ViewContext viewContext) {
        logger.debug("{} init", getName());
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public void close() {

    }

}
