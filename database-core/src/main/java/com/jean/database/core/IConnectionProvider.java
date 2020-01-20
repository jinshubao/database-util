package com.jean.database.core;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author jinshubao
 * @date 2017/4/9
 */
public interface IConnectionProvider {

    Connection getConnection(IConnectionConfiguration configuration) throws SQLException;
}
