package com.jean.database.core;

import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;

import java.sql.Connection;
import java.util.List;

/**
 * @author jinshubao
 * @date 2017/4/9
 */
public interface IMetadataProvider {

    List<CatalogMetaData> getCatalogs(Connection connection) throws Exception;

    List<SchemaMetaData> getSchemas(Connection connection, String catalog, String schemaPattern) throws Exception;

    List<TableMetaData> getTableMataData(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws Exception;

    List<ColumnMetaData> getColumnMetaData(Connection connection, String catalog, String schema, String tableNamePattern) throws Exception;

    List<String> getTableTypes(Connection connection) throws Exception;
}
