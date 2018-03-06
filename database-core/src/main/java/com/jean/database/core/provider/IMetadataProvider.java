package com.jean.database.core.provider;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.constant.DatabaseType;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;

import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 * @date 2017/4/9
 */
public interface IMetadataProvider {

    List<CatalogMetaData> getCatalogs(IConnectionConfiguration connectionConfig) throws Exception;

    List<SchemaMetaData> getSchemas(IConnectionConfiguration connectionConfig, String catalog) throws Exception;

    List<TableMetaData> getTables(IConnectionConfiguration connectionConfig, String catalog, String schema) throws Exception;

    List<ColumnMetaData> getColumns(IConnectionConfiguration connectionConfig, String catalog, String schema, String tableNamePattern) throws Exception;

    List<String> getTableTypes(IConnectionConfiguration connectionConfig) throws Exception;

    List<Map<String, String>> getTableRows(IConnectionConfiguration connectionConfig, String catalog, String schema, String tableNamePattern, int pageSize, int pageIndex) throws Exception;

    int getTableRowCount(IConnectionConfiguration connectionConfig, String catalog, String schema, String tableNamePattern) throws Exception;


    boolean support(DatabaseType databaseType);
}
