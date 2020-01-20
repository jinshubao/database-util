package com.jean.database.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDataProvider {

    List<Map<String, String>> getTableRows(Connection connection, String catalog, String schema, String tableNamePattern, int pageSize, int pageIndex) throws SQLException;

    int getTableRowCount(Connection connection, String catalog, String schema, String tableNamePattern) throws SQLException;
}
