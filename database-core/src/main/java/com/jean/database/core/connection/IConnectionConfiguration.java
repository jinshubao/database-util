package com.jean.database.core.connection;


import com.jean.database.core.constant.DatabaseType;

import java.util.Properties;

/**
 * @author jinshubao
 */
public interface IConnectionConfiguration {

    /**
     * 数据库类型
     *
     * @return
     */
    DatabaseType getDatabaseType();

    String getHost();

    Integer getPort();

    String getUser();

    String getPassword();

    String getConnectionURL();

    Properties getProperties();

}
