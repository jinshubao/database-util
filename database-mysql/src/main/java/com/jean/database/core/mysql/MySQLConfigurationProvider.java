package com.jean.database.core.mysql;

import com.jean.database.core.IConfigurationProvider;
import com.jean.database.core.IConnectionConfiguration;

public class MySQLConfigurationProvider implements IConfigurationProvider {

    public MySQLConfigurationProvider() {
    }

    @Override
    public IConnectionConfiguration getConfiguration(Object prams) {
        return new MySQLConnectionConfiguration("mysql", "127.0.0.1", 3306, "root", "root", null);
    }

}
