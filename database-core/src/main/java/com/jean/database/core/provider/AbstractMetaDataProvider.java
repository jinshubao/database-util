package com.jean.database.core.provider;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.constant.CommonConstant;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.ColumnMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jinshubao
 */
public abstract class AbstractMetaDataProvider implements IMetadataProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Map<String, Class<?>> DRIVERS = new ConcurrentHashMap<>();

    private static final String USER = "user";

    private static final String PASSWORD = "password";

    private static final String REMARKS = "remarks";

    Connection getConnection(IConnectionConfiguration connectionConfig) throws SQLException, ClassNotFoundException {
        Properties props = connectionConfig.getProperties();
        if (connectionConfig.getUser() != null && !props.containsKey(USER)) {
            props.setProperty(USER, connectionConfig.getUser());
        }
        if (connectionConfig.getPassword() != null && !props.containsKey(PASSWORD)) {
            props.setProperty(PASSWORD, connectionConfig.getPassword());
        }
        String driverClass = connectionConfig.getDatabaseType().getDriverClass();
        if (!DRIVERS.containsKey(driverClass)) {
            DRIVERS.put(driverClass, Class.forName(driverClass));
        }

        //设置可以获取remarks信息
        props.setProperty(REMARKS, Boolean.toString(true));
        return DriverManager.getConnection(connectionConfig.getConnectionURL(), props);
    }


    @Override
    public List<CatalogMetaData> getCatalogs(IConnectionConfiguration connectionConfig) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection(connectionConfig);
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getCatalogs();
            List<CatalogMetaData> catalogs = new ArrayList<>();
            while (rs.next()) {
                catalogs.add(getCatalogMetaData(rs));
            }
            return catalogs;
        } finally {
            close(connection, rs);
        }
    }

    @Override
    public List<SchemaMetaData> getSchemas(IConnectionConfiguration connectionConfig, String catalog) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection(connectionConfig);
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getSchemas(catalog, null);
            List<SchemaMetaData> schemas = new ArrayList<>();
            while (rs.next()) {
                schemas.add(getSchemaMetaData(rs));
            }
            return schemas;
        } finally {
            close(connection, rs);
        }
    }


    @Override
    public List<TableMetaData> getTables(IConnectionConfiguration connectionConfig, String catalog, String schema) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection(connectionConfig);
            DatabaseMetaData metaData = connection.getMetaData();
//            rs = metaData.getTables(catalog, schema, null, new String[]{TableType.TABLE.getValue(),TableType.SYSTEM_TABLE.getValue()});
            rs = metaData.getTables(catalog, schema, null, null);
            List<TableMetaData> tables = new ArrayList<>();
            while (rs.next()) {
                tables.add(getTableMetaData(rs));
            }

            return tables;
        } finally {
            close(connection, rs);
        }
    }

    @Override
    public List<ColumnMetaData> getColumns(IConnectionConfiguration connectionConfig, String catalog, String schema, String tableNamePattern) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection(connectionConfig);
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getColumns(catalog, schema, tableNamePattern, null);
            List<ColumnMetaData> columns = new ArrayList<>();
            while (rs.next()) {
                columns.add(getColumnMetaData(rs));
            }

            return columns;
        } finally {
            close(connection, rs);
        }
    }


    @Override
    public List<String> getTableTypes(IConnectionConfiguration connectionConfig) throws Exception {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getConnection(connectionConfig);
            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getTableTypes();
            List<String> columns = new ArrayList<>();
            while (rs.next()) {
                columns.add(rs.getString(CommonConstant.MetaDataType.TABLE_TYPE));
            }
            return columns;
        } finally {
            close(connection, rs);
        }
    }

    CatalogMetaData getCatalogMetaData(ResultSet resultSet) throws Exception {
        //表类别（可能为空）
        String tableCat = resultSet.getString(CommonConstant.MetaDataType.TABLE_CAT);
        CatalogMetaData catalogMetaData = new CatalogMetaData();
        catalogMetaData.setTableCat(tableCat);
        return catalogMetaData;
    }

    SchemaMetaData getSchemaMetaData(ResultSet resultSet) throws Exception {
        //表类别（可能为空）
        String tableCat = resultSet.getString(CommonConstant.MetaDataType.TABLE_CAT);
        //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
        String tableSchema = resultSet.getString(CommonConstant.MetaDataType.TABLE_SCHEM);
        SchemaMetaData schemaMetaData = new SchemaMetaData();
        schemaMetaData.setTableCat(tableCat);
        schemaMetaData.setTableSchem(tableSchema);
        return schemaMetaData;
    }

    TableMetaData getTableMetaData(ResultSet resultSet) throws Exception {
        //表类别（可能为空）
        String tableCat = resultSet.getString(CommonConstant.MetaDataType.TABLE_CAT);
        //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
        String tableSchema = resultSet.getString(CommonConstant.MetaDataType.TABLE_SCHEM);
        //表名
        String tableName = resultSet.getString(CommonConstant.MetaDataType.TABLE_NAME);
        //表类型,典型的类型是 "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"。
        String tableType = resultSet.getString(CommonConstant.MetaDataType.TABLE_TYPE);
        //表备注
        String remarks = resultSet.getString(CommonConstant.MetaDataType.REMARKS);
        String typeCat = resultSet.getString(CommonConstant.MetaDataType.TYPE_CAT);
        String typeSchema = resultSet.getString(CommonConstant.MetaDataType.TYPE_SCHEM);
        String selfReferencingColName = resultSet.getString(CommonConstant.MetaDataType.SELF_REFERENCING_COL_NAME);
        String typeName = resultSet.getString(CommonConstant.MetaDataType.TYPE_NAME);
        String refGeneration = resultSet.getString(CommonConstant.MetaDataType.REF_GENERATION);

        TableMetaData data = new TableMetaData();
        data.setTableCat(tableCat);
        data.setTableSchem(tableSchema);
        data.setTableName(tableName);
        data.setTableType(tableType);
        data.setRemarks(remarks);
        data.setTypeCat(typeCat);
        data.setTypeSchema(typeSchema);
        data.setTypeName(typeName);
        data.setSelfReferencingColName(selfReferencingColName);
        data.setRefGeneration(refGeneration);
        return data;
    }

    ColumnMetaData getColumnMetaData(ResultSet resultSet) throws Exception {
        //表类别（可能为空）
        String tableCat = resultSet.getString(CommonConstant.MetaDataType.TABLE_CAT);
        //表模式（可能为空）,在oracle中获取的是命名空间,其它数据库未知
        String tableSchema = resultSet.getString(CommonConstant.MetaDataType.TABLE_SCHEM);
        //表名
        String tableName = resultSet.getString(CommonConstant.MetaDataType.TABLE_NAME);
        //列名
        String columnName = resultSet.getString(CommonConstant.MetaDataType.COLUMN_NAME);
        //对应的java.sql.Types的SQL类型(列类型ID)
        int dataType = resultSet.getInt(CommonConstant.MetaDataType.DATA_TYPE);
        //java.sql.Types类型名称(列类型名称)
        String typeName = resultSet.getString(CommonConstant.MetaDataType.TYPE_NAME);
        //列大小
        int columnSize = resultSet.getInt(CommonConstant.MetaDataType.COLUMN_SIZE);
        int bufferLength = resultSet.getInt(CommonConstant.MetaDataType.BUFFER_LENGTH);
        //小数位数
        int decimalDigits = resultSet.getInt(CommonConstant.MetaDataType.DECIMAL_DIGITS);
        //基数（通常是10或2） --未知
        int numPrecRadix = resultSet.getInt(CommonConstant.MetaDataType.NUM_PREC_RADIX);
        //是否允许为null
        int nullAble = resultSet.getInt(CommonConstant.MetaDataType.NULLABLE);
        //列描述
        String remarks = resultSet.getString(CommonConstant.MetaDataType.REMARKS);
        int sqlDataType = resultSet.getInt(CommonConstant.MetaDataType.SQL_DATA_TYPE);
        int sqlDatetimeSub = resultSet.getInt(CommonConstant.MetaDataType.SQL_DATETIME_SUB);
        //默认值
        String columnDef = resultSet.getString(CommonConstant.MetaDataType.COLUMN_DEF);
        // 对于 char 类型，该长度是列中的最大字节数
        int charOctetLength = resultSet.getInt(CommonConstant.MetaDataType.CHAR_OCTET_LENGTH);
        int ordinalPosition = resultSet.getInt(CommonConstant.MetaDataType.ORDINAL_POSITION);
        /*
         * YES,NO,""
         */
        String isNullAble = resultSet.getString(CommonConstant.MetaDataType.IS_NULLABLE);
        ColumnMetaData data = new ColumnMetaData();
        data.setTableCat(tableCat);
        data.setTableSchem(tableSchema);
        data.setTableName(tableName);
        data.setColumnName(columnName);
        data.setDataType(dataType);
        data.setTypeName(typeName);
        data.setColumnSize(columnSize);
        data.setBufferLength(bufferLength);
        data.setDecimalDigits(decimalDigits);
        data.setNumPrecRadix(numPrecRadix);
        data.setNullAble(nullAble);
        data.setRemarks(remarks);
        data.setColumnDef(columnDef);
        data.setSqlDataType(sqlDataType);
        data.setSqlDatetimeSub(sqlDatetimeSub);
        data.setCharOctetLength(charOctetLength);
        data.setOrdinalPosition(ordinalPosition);
        data.setIsNullable(isNullAble);
        data.setScopeCatalog(resultSet.getString(CommonConstant.MetaDataType.SCOPE_CATALOG));
        data.setScopeSchema(resultSet.getString(CommonConstant.MetaDataType.SCOPE_SCHEMA));
        data.setScopeTable(resultSet.getString(CommonConstant.MetaDataType.SCOPE_TABLE));
        data.setSourceDataType(resultSet.getInt(CommonConstant.MetaDataType.SOURCE_DATA_TYPE));
        data.setIsAutoincrement(resultSet.getString(CommonConstant.MetaDataType.IS_AUTOINCREMENT));
        data.setIsGeneratedcolumn(resultSet.getString(CommonConstant.MetaDataType.IS_GENERATEDCOLUMN));
        return data;
    }

    void close(Connection connection, ResultSet resultSet) {
        close(connection);
        close(resultSet);
    }

    void close(Connection connection, Statement statement, ResultSet resultSet) {
        close(connection);
        close(statement);
        close(resultSet);
    }

    void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

    }

    void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
