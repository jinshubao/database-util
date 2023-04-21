package com.jean.database.oracle.provider;

import com.jean.database.api.KeyValuePair;
import com.jean.database.sql.SQLMetadataFactory;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.meta.TableSummaries;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OracleMetadataFactory extends SQLMetadataFactory {

    public OracleMetadataFactory(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Map<String, Object>> getTableRows(TableMetaData tableMetaData, int pageSize, int pageIndex) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            //TODO
            return Collections.emptyList();
        }
    }

    @Override
    public int getTableRowCount(TableMetaData tableMetaData) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            //TODO
            return 0;
        }
    }

    @Override
    public List<TableSummaries> getTableSummaries(String catalog, String schemaPattern, String[] tableNamePattern, String[] types) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            //TODO
            return Collections.emptyList();
        }
    }

    @Override
    public List<KeyValuePair<String, Object>> getTableDetails(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            //TODO
            return Collections.emptyList();
        }
    }

}
