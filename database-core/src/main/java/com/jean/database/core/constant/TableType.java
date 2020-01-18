package com.jean.database.core.constant;

/**
 * "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
 *
 * @author jinshubao
 */
public enum TableType {
    TABLE("TABLE"),
    VIEW("VIEW"),
    SYSTEM_TABLE("SYSTEM TABLE"),
    GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
    LOCAL_TEMPORARY("LOCAL TEMPORARY"),
    ALIAS("ALIAS"),
    SYNONYM("SYNONYM");
    private String value;

    TableType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
