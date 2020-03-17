package com.jean.database.oracle;

import com.jean.database.core.AbstractMetaDataProvider;
import com.jean.database.core.meta.TableMetaData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OracleMetadataProvider extends AbstractMetaDataProvider {

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

}
