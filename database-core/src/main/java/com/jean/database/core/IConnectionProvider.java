package com.jean.database.core;

import com.jean.database.core.constant.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author jinshubao
 * @date 2017/4/9
 */
public interface IConnectionProvider {

    Connection getConnection(String url) throws SQLException;

    Connection getConnection(String url, String user, String password) throws SQLException;

    Connection getConnection(String url, java.util.Properties properties) throws SQLException;

    boolean support(DatabaseType databaseType);
}
