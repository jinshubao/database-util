package com.jean.database.sql;

import com.jean.database.api.AbstractConnectionConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class SQLConnectionConfiguration extends AbstractConnectionConfiguration {


    public SQLConnectionConfiguration(String connectionName, String host, Integer port, String user, String password) {
        super(connectionName, host, port, user, password);
    }

    public SQLConnectionConfiguration(String connectionName, String host, Integer port, String user, String password, Properties properties) {
        super(connectionName, host, port, user, password, properties);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getProperties());
    }
}
