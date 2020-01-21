package com.jean.database.mysql;

import com.jean.database.core.IDatabaseType;

public class MySQLDatabaseType implements IDatabaseType {

    public static String ID = "MySQL";

    private String identifier;
    private String name;
    private String catalogIcon = "/mysql/catalog.png";
    private String schemaIcon;
    private String tableIcon;

    public MySQLDatabaseType(String name) {
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

    @Override
    public String getIcon() {
        return "/mysql/mysql.png";
    }

    @Override
    public String getCatalogIcon() {
        return "/mysql/catalog.png";
    }

    @Override
    public String getTableIcon() {
        return "/mysql/table.png";
    }
}
