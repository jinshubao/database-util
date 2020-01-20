package com.jean.database.core.mysql;

import com.jean.database.core.IDataBaseType;

public class MySQLDataBaseType implements IDataBaseType {

    public static String ID = "com.jean.database.core.mysql.MySQLDataBaseType";

    private String identifier;
    private String name;

    public MySQLDataBaseType(String name) {
        this.identifier = ID;
        this.name = name;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return name;
    }
}
