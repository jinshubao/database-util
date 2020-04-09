package com.jean.database.mysql;

import com.jean.database.core.AbstractMetaDataProvider;
import com.jean.database.core.meta.KeyValuePair;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.core.meta.TableSummaries;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
public class MySQLMetadataProvider extends AbstractMetaDataProvider {

    @Override
    public List<Map<String, Object>> getTableRows(Connection connection, TableMetaData tableMetaData, int pageSize, int pageIndex) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String quoteString = metaData.getIdentifierQuoteString();
        String separator = metaData.getCatalogSeparator();
        int offset = pageSize * pageIndex;

        String sql = "SELECT * FROM " + quoteString + tableMetaData.getTableCat() + quoteString + separator + quoteString + tableMetaData.getTableName() + quoteString +
                " LIMIT " + offset + "," + pageSize;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<Map<String, Object>> list = new ArrayList<>();
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int columnCount = rsMetaData.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsMetaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        row.put(columnName, value);
                    }
                    list.add(row);
                }
                return list;
            }
        }
    }

    @Override
    public int getTableRowCount(Connection connection, TableMetaData tableMetaData) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String quoteString = metaData.getIdentifierQuoteString();
        String separator = metaData.getCatalogSeparator();


        String sql = "SELECT COUNT(*) FROM " + quoteString + tableMetaData.getTableCat() + quoteString + separator + quoteString + tableMetaData.getTableName() + quoteString;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }


    @Override
    public List<TableSummaries> getTableSummaries(Connection connection, String catalog, String schemaPattern, String[] tableNamePattern, String[] types) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String quoteString = metaData.getIdentifierQuoteString();
        String separator = metaData.getCatalogSeparator();

        String sql = "SELECT TABLE_NAME, AUTO_INCREMENT, UPDATE_TIME, DATA_LENGTH, ENGINE, TABLE_ROWS, TABLE_COMMENT" +
                " FROM " + quoteString + "information_schema" + quoteString + separator + quoteString + "TABLES" + quoteString +
                " WHERE TABLE_CATALOG = ? AND TABLE_SCHEMA = ? AND TABLE_TYPE = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "def");
            statement.setString(2, catalog);
            statement.setString(3, "BASE TABLE");
            try (ResultSet rs = statement.executeQuery()) {
                List<TableSummaries> result = new ArrayList<>();
                while (rs.next()) {
                    TableSummaries summaries = new TableSummaries(
                            rs.getString("TABLE_NAME"),
                            rs.getString("AUTO_INCREMENT"),
                            rs.getString("UPDATE_TIME"),
                            rs.getString("DATA_LENGTH"),
                            rs.getString("ENGINE"),
                            rs.getString("TABLE_ROWS"),
                            rs.getString("TABLE_COMMENT"));
                    result.add(summaries);
                }
                return result;
            }
        }
    }

    @Override
    public List<KeyValuePair<String, Object>> getTableDetails(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String quoteString = metaData.getIdentifierQuoteString();
        String separator = metaData.getCatalogSeparator();

        String sql = "SELECT * FROM " + quoteString + "information_schema" + quoteString + separator + quoteString + "TABLES" + quoteString +
                " WHERE TABLE_CATALOG = ? AND TABLE_SCHEMA = ? AND TABLE_NAME = ? AND TABLE_TYPE = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "def");
            statement.setString(2, catalog);
            statement.setString(3, tableNamePattern);
            statement.setString(4, "BASE TABLE");
            try (ResultSet rs = statement.executeQuery()) {
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int columnCount = rsMetaData.getColumnCount();
                List<KeyValuePair<String, Object>> result = new ArrayList<>(columnCount);
                if (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsMetaData.getColumnName(i);
                        Object value = rs.getObject(columnName);
                        result.add(new KeyValuePair<>(columnName, value));
                    }
                }
                return result;
            }
        }
    }
}
