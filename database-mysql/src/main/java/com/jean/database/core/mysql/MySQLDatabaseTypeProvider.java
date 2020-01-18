package com.jean.database.core.mysql;

import com.jean.database.core.IDatabaseTypeProvider;

public class MySQLDatabaseTypeProvider implements IDatabaseTypeProvider {

    public MySQLDatabaseTypeProvider() {
    }

    @Override
    public String getName() {
        return "MySQL";
    }

    @Override
    public String getDriverClass() {
        return "com.mysql.jdbc.Driver";
    }
}
