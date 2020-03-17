package com.jean.database.core.meta;

import com.jean.database.common.utils.StringUtil;

/**
 * 表信息
 *
 * @author jinshubao
 */
public class TableMetaData extends SchemaMetaData {

    public TableMetaData() {
    }

    private String tableName;

    private String tableType;

    private String remarks;

    private String typeCat;

    private String typeSchema;

    private String typeName;

    private String selfReferencingColName;

    private String refGeneration;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(String typeCat) {
        this.typeCat = typeCat;
    }

    public String getTypeSchema() {
        return typeSchema;
    }

    public void setTypeSchema(String typeSchema) {
        this.typeSchema = typeSchema;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    public String getRefGeneration() {
        return refGeneration;
    }

    public void setRefGeneration(String refGeneration) {
        this.refGeneration = refGeneration;
    }

    public String getFullName() {
        String fullName = getTableCat();
        if (StringUtil.isNotBlank(getTableSchem())) {
            fullName += ("." + getTableSchem());
        }
        fullName += ("." + getTableName());
        return fullName;
    }

    @Override
    public String toString() {
        return tableName;
    }
}
