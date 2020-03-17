package com.jean.database.mysql;

import com.jean.database.core.AbstractMetaDataProvider;
import com.jean.database.core.meta.TableMetaData;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MySQLMetadataProvider extends AbstractMetaDataProvider {

    @Override
    public List<Map<String, Object>> getTableRows(Connection connection, TableMetaData tableMetaData, int pageSize, int pageIndex) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String quoteString = metaData.getIdentifierQuoteString();
        String separator = metaData.getCatalogSeparator();
        int offset = pageSize * pageIndex;
        String sql = "select * from " + quoteString + tableMetaData.getTableCat() + quoteString + separator + quoteString + tableMetaData.getTableName() + quoteString + " limit " + offset + "," + pageSize;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<Map<String, Object>> list = new ArrayList<>();
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int columnCount = rsMetaData.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsMetaData.getColumnName(i);
                        Object value = rs.getObject(columnName);
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
        String sql = "select count(*) from " + quoteString + tableMetaData.getTableCat() + quoteString + separator + quoteString + tableMetaData.getTableName() + quoteString;
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
