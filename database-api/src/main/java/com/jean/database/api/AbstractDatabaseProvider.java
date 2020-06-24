package com.jean.database.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDatabaseProvider implements IDatabaseProvider {

    public static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseProvider.class);

    @Override
    public void init() {
        logger.debug("{} provider init", getName());
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public void close() {

    }

}
