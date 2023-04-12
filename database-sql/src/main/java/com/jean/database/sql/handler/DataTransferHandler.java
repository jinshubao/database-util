package com.jean.database.sql.handler;

public interface DataTransferHandler {

    void transfer(String tableName, String columnName, String columnValue);
}
