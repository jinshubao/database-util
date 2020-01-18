package com.jean.database.core;

import com.jean.database.core.constant.DatabaseType;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class DefaultDataProvider implements IDataProvider {
    @Override
    public List<Map<String, String>> getTableRows(Connection connection, String catalog, String schema, String tableNamePattern, int pageSize, int pageIndex) throws Exception {
        return null;
    }

    @Override
    public int getTableRowCount(Connection connection, String catalog, String schema, String tableNamePattern) throws Exception {
        return 0;
    }

    @Override
    public boolean support(DatabaseType databaseType) {
        return false;
    }
}
