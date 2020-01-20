package com.jean.database.core.mysql;

import com.jean.database.core.IDataProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 */
public class MySQLDataProvider implements IDataProvider {

    public MySQLDataProvider() {
    }

    @Override
    public List<Map<String, String>> getTableRows(Connection connection, String catalog, String schema, String tableNamePattern, int pageSize, int pageIndex) throws SQLException {
        int offset = pageSize * pageIndex;
        String sql = "select * from " + catalog + "." + tableNamePattern + " limit " + offset + "," + pageSize;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<Map<String, String>> list = new ArrayList<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (rs.next()) {
                    Map<String, String> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String value = rs.getString(columnName);
                        row.put(columnName, value);
                    }
                    list.add(row);
                }
                return list;
            }
        }
    }

    @Override
    public int getTableRowCount(Connection connection, String catalog, String schema, String tableNamePattern) throws SQLException {
        String sql = "select count(*) from " + catalog + "." + tableNamePattern;
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
