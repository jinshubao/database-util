package com.jean.database.sql;


import com.jean.database.api.KeyValuePair;
import com.jean.database.api.AbstractConnectionConfiguration;
import com.jean.database.sql.meta.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 * @date 2017/4/9
 */
public interface SQLMetadataProvider {

    default Connection getConnection(AbstractConnectionConfiguration configuration) throws SQLException {
        return DriverManager.getConnection(configuration.getUrl(), configuration.getProperties());
    }

    default DatabaseMetaData getDatabaseMetaData(Connection connection) throws SQLException {
        return connection.getMetaData();
    }

    List<CatalogMetaData> getCatalogs(Connection connection) throws SQLException;

    List<SchemaMetaData> getSchemas(Connection connection, String catalog, String schemaPattern) throws SQLException;

    List<TableMetaData> getTableMataData(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException;

    List<ColumnMetaData> getColumnMetaData(Connection connection, String catalog, String schema, String tableNamePattern) throws SQLException;

    List<String> getTableTypes(Connection connection) throws SQLException;

    List<Map<String, Object>> getTableRows(Connection connection, TableMetaData tableMetaData, int pageSize, int pageIndex) throws SQLException;

    int getTableRowCount(Connection connection, TableMetaData tableMetaData) throws SQLException;

    List<TableSummaries> getTableSummaries(Connection connection, String catalog, String schemaPattern, String[] tableNamePattern, String[] types) throws SQLException;

    List<KeyValuePair<String, Object>> getTableDetails(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException;

}
