package com.jean.database.oracle;

import com.jean.database.core.IDatabaseType;

public class OracleDatabaseType implements IDatabaseType {

    private static final String ID = "Oracle";

    private String identifier;
    private String name;

    public OracleDatabaseType(String name) {
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
        return "/oracle/oracle.png";
    }

    @Override
    public String getCatalogIcon() {
        return "/oracle/catalog.png";
    }

    @Override
    public String getTableIcon() {
        return "/oracle/table.png";
    }
}
