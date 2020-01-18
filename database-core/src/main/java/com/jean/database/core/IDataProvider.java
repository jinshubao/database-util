package com.jean.database.core;

import com.jean.database.core.constant.DatabaseType;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface IDataProvider {

    List<Map<String, String>> getTableRows(Connection connection, String catalog, String schema, String tableNamePattern, int pageSize, int pageIndex) throws Exception;

    int getTableRowCount(Connection connection, String catalog, String schema, String tableNamePattern) throws Exception;

    boolean support(DatabaseType databaseType);
}
