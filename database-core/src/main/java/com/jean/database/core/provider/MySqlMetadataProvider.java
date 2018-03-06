package com.jean.database.core.provider;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.constant.DatabaseType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 */
public class MySqlMetadataProvider extends AbstractMetaDataProvider {

    @Override
    public List<Map<String, String>> getTableRows(IConnectionConfiguration connectionConfig, String catalog, String schema,
                                                  String tableNamePattern, int pageSize, int pageIndex) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = getConnection(connectionConfig);
            int offset = pageSize * pageIndex;
            statement = connection.prepareStatement("select * from " + catalog + "." + tableNamePattern + " limit " + offset + "," + pageSize);
            rs = statement.executeQuery();
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
        } finally {
            close(connection, statement, rs);
        }
    }

    @Override
    public int getTableRowCount(IConnectionConfiguration connectionConfig, String catalog, String schema, String tableNamePattern) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = getConnection(connectionConfig);
            statement = connection.prepareStatement("select count(*) from " + catalog + "." + tableNamePattern);
            rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            close(connection, statement, rs);
        }
    }

    @Override
    public boolean support(DatabaseType databaseType) {
        return databaseType == DatabaseType.MySql;
    }

}
