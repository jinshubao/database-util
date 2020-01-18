package com.jean.database.core;

import com.jean.database.core.constant.DatabaseType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DefaultConnectionProvider implements IConnectionProvider {

    @Override
    public Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Connection getConnection(String url, Properties properties) throws SQLException {
        return DriverManager.getConnection(url, properties);
    }

    @Override
    public boolean support(DatabaseType databaseType) {
        return true;
    }
}
