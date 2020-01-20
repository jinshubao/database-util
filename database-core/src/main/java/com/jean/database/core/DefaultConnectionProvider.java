package com.jean.database.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DefaultConnectionProvider implements IConnectionProvider {

    @Override
    public Connection getConnection(IConnectionConfiguration configuration) throws SQLException {
        return DriverManager.getConnection(configuration.getUrl(), configuration.getProperties());
    }
}
