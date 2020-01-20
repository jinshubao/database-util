package com.jean.database.core;

import com.jean.database.core.constant.CommonConstant;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jinshubao
 */
public class DefaultMetaDataProvider implements IMetadataProvider {

    @Override
    public List<CatalogMetaData> getCatalogs(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getCatalogs()) {
            return covertCatalogMetaDataResultSet(resultSet);
        }
    }

    @Override
    public List<SchemaMetaData> getSchemas(Connection connection, String catalog, String schemaPattern) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getSchemas(catalog, schemaPattern)) {
            return convertSchemaMetaDataResultSet(resultSet);
        }
    }


    @Override
    public List<TableMetaData> getTableMataData(Connection connection, String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            return convertTableMetaDataResultSet(resultSet);
        }
    }

    @Override
    public List<ColumnMetaData> getColumnMetaData(Connection connection, String catalog, String schema, String tableNamePattern) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getColumns(catalog, schema, tableNamePattern, null)) {
            return convertColumnMetaDataResultSet(resultSet);
        }
    }

    @Override
    public List<String> getTableTypes(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getTableTypes()) {
            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_TYPE));
            }
            return list;
        }
    }

    protected List<CatalogMetaData> covertCatalogMetaDataResultSet(ResultSet resultSet) throws SQLException {
        List<CatalogMetaData> list = new ArrayList<>();
        while (resultSet.next()) {
            CatalogMetaData data = new CatalogMetaData();
            //表类别（可能为空）
            data.setTableCat(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_CAT));
            list.add(data);
        }
        return list;
    }

    protected List<SchemaMetaData> convertSchemaMetaDataResultSet(ResultSet resultSet) throws SQLException {
        List<SchemaMetaData> list = new ArrayList<>();
        while (resultSet.next()) {
            SchemaMetaData data = new SchemaMetaData();
            //表类别（可能为空）
            data.setTableCat(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_CAT));
            //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
            data.setTableSchem(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_SCHEM));
            list.add(data);
        }
        return list;
    }

    protected List<TableMetaData> convertTableMetaDataResultSet(ResultSet resultSet) throws SQLException {
        List<TableMetaData> list = new ArrayList<>();
        while (resultSet.next()) {
            TableMetaData data = new TableMetaData();
            //表类别（可能为空）
            data.setTableCat(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_CAT));
            //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
            data.setTableSchem(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_SCHEM));
            //表名
            data.setTableName(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_NAME));
            //表类型,典型的类型是 "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"。
            data.setTableType(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_TYPE));
            data.setRemarks(resultSet.getString(CommonConstant.MetaDataColumnName.REMARKS));
            data.setTypeCat(resultSet.getString(CommonConstant.MetaDataColumnName.TYPE_CAT));
            data.setTypeSchema(resultSet.getString(CommonConstant.MetaDataColumnName.TYPE_SCHEM));
            data.setTypeName(resultSet.getString(CommonConstant.MetaDataColumnName.TYPE_NAME));
            data.setSelfReferencingColName(resultSet.getString(CommonConstant.MetaDataColumnName.SELF_REFERENCING_COL_NAME));
            data.setRefGeneration(resultSet.getString(CommonConstant.MetaDataColumnName.REF_GENERATION));
            list.add(data);
        }
        return list;
    }

    protected List<ColumnMetaData> convertColumnMetaDataResultSet(ResultSet resultSet) throws SQLException {
        List<ColumnMetaData> list = new ArrayList<>();
        while (resultSet.next()) {
            ColumnMetaData data = new ColumnMetaData();
            //表类别（可能为空）
            data.setTableCat(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_CAT));
            //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
            data.setTableSchem(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_SCHEM));
            //表名
            data.setTableName(resultSet.getString(CommonConstant.MetaDataColumnName.TABLE_NAME));
            //列名
            data.setColumnName(resultSet.getString(CommonConstant.MetaDataColumnName.COLUMN_NAME));
            //对应的java.sql.Types的SQL类型(列类型ID)
            data.setDataType(resultSet.getInt(CommonConstant.MetaDataColumnName.DATA_TYPE));
            //java.sql.Types类型名称(列类型名称)
            data.setTypeName(resultSet.getString(CommonConstant.MetaDataColumnName.TYPE_NAME));
            //列大小
            data.setColumnSize(resultSet.getInt(CommonConstant.MetaDataColumnName.COLUMN_SIZE));
            //unused
            data.setBufferLength(resultSet.getInt(CommonConstant.MetaDataColumnName.BUFFER_LENGTH));
            //小数位数
            data.setDecimalDigits(resultSet.getInt(CommonConstant.MetaDataColumnName.DECIMAL_DIGITS));
            //基数（通常是10或2）
            data.setNumPrecRadix(resultSet.getInt(CommonConstant.MetaDataColumnName.NUM_PREC_RADIX));
            //是否允许为null 0:该列不允许为空,1:该列允许为空 2:未知
            data.setNullable(resultSet.getInt(CommonConstant.MetaDataColumnName.NULLABLE));
            //列注释
            data.setRemarks(resultSet.getString(CommonConstant.MetaDataColumnName.REMARKS));
            //默认值
            data.setColumnDef(resultSet.getString(CommonConstant.MetaDataColumnName.COLUMN_DEF));
            //unused
            data.setSqlDataType(resultSet.getInt(CommonConstant.MetaDataColumnName.SQL_DATA_TYPE));
            //unused
            data.setSqlDatetimeSub(resultSet.getInt(CommonConstant.MetaDataColumnName.SQL_DATETIME_SUB));
            //对于char类型，该长度是列中的最大字节数
            data.setCharOctetLength(resultSet.getInt(CommonConstant.MetaDataColumnName.CHAR_OCTET_LENGTH));
            data.setOrdinalPosition(resultSet.getInt(CommonConstant.MetaDataColumnName.ORDINAL_POSITION));
            //YES/NO/""
            data.setIsNullable(resultSet.getString(CommonConstant.MetaDataColumnName.IS_NULLABLE));
            data.setScopeCatalog(resultSet.getString(CommonConstant.MetaDataColumnName.SCOPE_CATALOG));
            data.setScopeSchema(resultSet.getString(CommonConstant.MetaDataColumnName.SCOPE_SCHEMA));
            data.setScopeTable(resultSet.getString(CommonConstant.MetaDataColumnName.SCOPE_TABLE));
            data.setSourceDataType(resultSet.getShort(CommonConstant.MetaDataColumnName.SOURCE_DATA_TYPE));
            data.setIsAutoincrement(resultSet.getString(CommonConstant.MetaDataColumnName.IS_AUTOINCREMENT));
            data.setIsGeneratedcolumn(resultSet.getString(CommonConstant.MetaDataColumnName.IS_GENERATEDCOLUMN));
            list.add(data);
        }
        return list;
    }

    @Override
    public boolean supportCatalog() {
        return true;
    }

    @Override
    public boolean supportSchema() {
        return false;
    }
}
