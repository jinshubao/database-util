package com.jean.database.mogo;

import com.jean.database.api.AbstractDatabaseProvider;

public class MogoDatabaseProvider extends AbstractDatabaseProvider {

    @Override
    public String getIdentifier() {
        return "mogodb";
    }

    @Override
    public String getName() {
        return "mogodb";
    }

}
