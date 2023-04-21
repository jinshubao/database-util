package com.jean.database.sql;


import com.jean.database.api.KeyValuePair;
import com.jean.database.sql.constant.MetaDataColumnName;
import com.jean.database.sql.meta.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jinshubao
 * @date 2017/4/9
 */
public abstract class SQLMetadataFactory {

    protected final Logger logger = LoggerFactory.getLogger(SQLMetadataFactory.class);

    protected final DataSource dataSource;

    public SQLMetadataFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public DatabaseMetaData getDatabaseMetaData() throws SQLException {
        logger.debug("getDatabaseMetaData");
        try (Connection connection = getDataSource().getConnection()) {
            return connection.getMetaData();
        }
    }

    public List<CatalogMetaData> getCatalogs() throws SQLException {
        logger.debug("getCatalogs");
        try (Connection connection = getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getCatalogs()) {
                return covertCatalogMetaDataResultSet(resultSet);
            }
        }
    }

    public List<SchemaMetaData> getSchemas(String catalog, String schemaPattern) throws SQLException {
        logger.debug("getSchemas");
        try (Connection connection = getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getSchemas(catalog, schemaPattern)) {
                return convertSchemaMetaDataResultSet(resultSet);
            }
        }
    }


    public List<TableMetaData> getTableMataData(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        logger.debug("getTableMataData");
        try (Connection connection = getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
                return convertTableMetaDataResultSet(resultSet);
            }
        }
    }


    public List<ColumnMetaData> getColumnMetaData(String catalog, String schema, String tableNamePattern) throws SQLException {
        logger.debug("getColumnMetaData");
        try (Connection connection = getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getColumns(catalog, schema, tableNamePattern, null)) {
                return convertColumnMetaDataResultSet(resultSet);
            }
        }
    }

    public List<String> getTableTypes() throws SQLException {
        logger.debug("getTableTypes");
        try (Connection connection = getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTableTypes()) {
                List<String> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(resultSet.getString(MetaDataColumnName.TABLE_TYPE));
                }
                return list;
            }
        }
    }

    public abstract List<Map<String, Object>> getTableRows(TableMetaData tableMetaData, int pageSize, int pageIndex) throws SQLException;

    public abstract int getTableRowCount(TableMetaData tableMetaData) throws SQLException;

    public abstract List<TableSummaries> getTableSummaries(String catalog, String schemaPattern, String[] tableNamePattern, String[] types) throws SQLException;

    public abstract List<KeyValuePair<String, Object>> getTableDetails(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException;

    protected List<CatalogMetaData> covertCatalogMetaDataResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("convertCatalogMetaDataResultSet");
        List<CatalogMetaData> list = new ArrayList<>();
        while (resultSet.next()) {
            CatalogMetaData data = new CatalogMetaData();
            //表类别（可能为空）
            data.setTableCat(resultSet.getString(MetaDataColumnName.TABLE_CAT));
            list.add(data);
        }
        return list;
    }

    protected List<SchemaMetaData> convertSchemaMetaDataResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("convertSchemaMetaDataResultSet");
        List<SchemaMetaData> list = new ArrayList<>();
        while (resultSet.next()) {
            SchemaMetaData data = new SchemaMetaData();
            //表类别（可能为空）
            data.setTableCat(resultSet.getString(MetaDataColumnName.TABLE_CAT));
            //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
            data.setTableSchema(resultSet.getString(MetaDataColumnName.TABLE_SCHEM));
            list.add(data);
        }
        return list;
    }

    protected List<TableMetaData> convertTableMetaDataResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("convertTableMetaDataResultSet");
        List<TableMetaData> list = new ArrayList<>();
        while (resultSet.next()) {
            TableMetaData data = new TableMetaData();
            //表类别（可能为空）
            data.setTableCat(resultSet.getString(MetaDataColumnName.TABLE_CAT));
            //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
            data.setTableSchema(resultSet.getString(MetaDataColumnName.TABLE_SCHEM));
            //表名
            data.setTableName(resultSet.getString(MetaDataColumnName.TABLE_NAME));
            //表类型,典型的类型是 "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"。
            data.setTableType(resultSet.getString(MetaDataColumnName.TABLE_TYPE));
            data.setRemarks(resultSet.getString(MetaDataColumnName.REMARKS));
            data.setTypeCat(resultSet.getString(MetaDataColumnName.TYPE_CAT));
            data.setTypeSchema(resultSet.getString(MetaDataColumnName.TYPE_SCHEM));
            data.setTypeName(resultSet.getString(MetaDataColumnName.TYPE_NAME));
            data.setSelfReferencingColName(resultSet.getString(MetaDataColumnName.SELF_REFERENCING_COL_NAME));
            data.setRefGeneration(resultSet.getString(MetaDataColumnName.REF_GENERATION));
            list.add(data);
        }
        return list;
    }

    protected List<ColumnMetaData> convertColumnMetaDataResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("convertColumnMetaDataResultSet");
        List<ColumnMetaData> list = new ArrayList<>();
        while (resultSet.next()) {
            ColumnMetaData data = new ColumnMetaData();
            //表类别（可能为空）
            data.setTableCat(resultSet.getString(MetaDataColumnName.TABLE_CAT));
            //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
            data.setTableSchema(resultSet.getString(MetaDataColumnName.TABLE_SCHEM));
            //表名
            data.setTableName(resultSet.getString(MetaDataColumnName.TABLE_NAME));
            //列名
            data.setColumnName(resultSet.getString(MetaDataColumnName.COLUMN_NAME));
            //对应的java.sql.Types的SQL类型(列类型ID)
            data.setDataType(resultSet.getInt(MetaDataColumnName.DATA_TYPE));
            //java.sql.Types类型名称(列类型名称)
            data.setTypeName(resultSet.getString(MetaDataColumnName.TYPE_NAME));
            //列大小
            data.setColumnSize(resultSet.getInt(MetaDataColumnName.COLUMN_SIZE));
            //unused
            data.setBufferLength(resultSet.getInt(MetaDataColumnName.BUFFER_LENGTH));
            //小数位数
            data.setDecimalDigits(resultSet.getInt(MetaDataColumnName.DECIMAL_DIGITS));
            //基数（通常是10或2）
            data.setNumPrecRadix(resultSet.getInt(MetaDataColumnName.NUM_PREC_RADIX));
            //是否允许为null 0:该列不允许为空,1:该列允许为空 2:未知
            data.setNullable(resultSet.getInt(MetaDataColumnName.NULLABLE));
            //列注释
            data.setRemarks(resultSet.getString(MetaDataColumnName.REMARKS));
            //默认值
            data.setColumnDef(resultSet.getString(MetaDataColumnName.COLUMN_DEF));
            //unused
            data.setSqlDataType(resultSet.getInt(MetaDataColumnName.SQL_DATA_TYPE));
            //unused
            data.setSqlDatetimeSub(resultSet.getInt(MetaDataColumnName.SQL_DATETIME_SUB));
            //对于char类型，该长度是列中的最大字节数
            data.setCharOctetLength(resultSet.getInt(MetaDataColumnName.CHAR_OCTET_LENGTH));
            data.setOrdinalPosition(resultSet.getInt(MetaDataColumnName.ORDINAL_POSITION));
            //YES/NO/""
            data.setIsNullable(resultSet.getString(MetaDataColumnName.IS_NULLABLE));
            data.setScopeCatalog(resultSet.getString(MetaDataColumnName.SCOPE_CATALOG));
            data.setScopeSchema(resultSet.getString(MetaDataColumnName.SCOPE_SCHEMA));
            data.setScopeTable(resultSet.getString(MetaDataColumnName.SCOPE_TABLE));
            data.setSourceDataType(resultSet.getInt(MetaDataColumnName.SOURCE_DATA_TYPE));
            data.setIsAutoincrement(resultSet.getString(MetaDataColumnName.IS_AUTOINCREMENT));
            data.setIsGeneratedcolumn(resultSet.getString(MetaDataColumnName.IS_GENERATEDCOLUMN));
            list.add(data);
        }
        return list;
    }
}
