package com.jean.database.oracle;

import com.jean.database.api.KeyValuePair;
import com.jean.database.sql.AbstractSQLMetaDataProvider;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.meta.TableSummaries;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OracleMetadataProvider extends AbstractSQLMetaDataProvider {

    @Override
    public List<Map<String, Object>> getTableRows(Connection connection, TableMetaData tableMetaData, int pageSize, int pageIndex) throws SQLException {
        //TODO
        return Collections.emptyList();
    }

    @Override
    public int getTableRowCount(Connection connection, TableMetaData tableMetaData) throws SQLException {
        //TODO
        return 0;
    }

    @Override
    public List<TableSummaries> getTableSummaries(Connection connection, String catalog, String schemaPattern, String[] tableNamePattern, String[] types) throws SQLException {
        //TODO
        return Collections.emptyList();
    }

    @Override
    public List<KeyValuePair<String, Object>> getTableDetails(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        //TODO
        return Collections.emptyList();
    }

}
