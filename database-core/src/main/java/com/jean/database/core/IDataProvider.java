package com.jean.database.core;

import com.jean.database.core.meta.TableMetaData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDataProvider {

    List<Map<String, Object>> getTableRows(Connection connection, TableMetaData tableMetaData, int pageSize, int pageIndex) throws SQLException;

    int getTableRowCount(Connection connection, TableMetaData tableMetaData) throws SQLException;
}
