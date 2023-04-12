package com.jean.database.mysql.provider;

import com.jean.database.api.KeyValuePair;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.meta.TableSummaries;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MySQLMetadataProvider extends SQLMetadataProvider {

    public MySQLMetadataProvider(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Map<String, Object>> getTableRows(TableMetaData tableMetaData, int pageSize, int pageIndex) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
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
    }

    @Override
    public int getTableRowCount(TableMetaData tableMetaData) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
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
    }


    @Override
    public List<TableSummaries> getTableSummaries(String catalog, String schemaPattern, String[] tableNamePattern, String[] types) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
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
                        TableSummaries summaries = new TableSummaries();
                        summaries.setTableName(rs.getString("TABLE_NAME"));
                        summaries.setAutoIncrement(rs.getString("AUTO_INCREMENT"));
                        summaries.setModifyTime(rs.getString("UPDATE_TIME"));
                        summaries.setDataLength(rs.getString("DATA_LENGTH"));
                        summaries.setTableType(rs.getString("ENGINE"));
                        summaries.setTableRows(rs.getString("TABLE_ROWS"));
                        summaries.setComments(rs.getString("TABLE_COMMENT"));
                        result.add(summaries);
                    }
                    return result;
                }
            }
        }
    }

    @Override
    public List<KeyValuePair<String, Object>> getTableDetails(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
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
}
