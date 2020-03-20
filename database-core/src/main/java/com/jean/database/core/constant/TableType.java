package com.jean.database.core.constant;

import java.util.Arrays;
import java.util.List;

/**
 * "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
 *
 * @author jinshubao
 */
public class TableType {

    public static final String TABLE = "TABLE";
    public static final String VIEW = "VIEW";
    public static final String SYSTEM_TABLE = "SYSTEM TABLE";
    public static final String GLOBAL_TEMPORARY = "GLOBAL TEMPORARY";
    public static final String LOCAL_TEMPORARY = "LOCAL TEMPORARY";
    public static final String ALIAS = "ALIAS";
    public static final String SYNONYM = "SYNONYM";


    public static List<String> tableTypes = Arrays.asList(TABLE, VIEW, SYSTEM_TABLE, GLOBAL_TEMPORARY, LOCAL_TEMPORARY, ALIAS, SYNONYM);


}
