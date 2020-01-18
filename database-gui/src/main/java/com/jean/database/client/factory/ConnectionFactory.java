package com.jean.database.client.factory;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    Connection getConnection() throws SQLException;
}
