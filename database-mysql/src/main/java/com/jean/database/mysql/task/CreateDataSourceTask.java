package com.jean.database.mysql.task;

import com.jean.database.mysql.config.MySQLConnectionConfiguration;
import com.jean.database.task.BackgroundTask;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class CreateDataSourceTask extends BackgroundTask<DataSource> {

    private final MySQLConnectionConfiguration configuration;

    public CreateDataSourceTask(MySQLConnectionConfiguration configuration) {
        super("连接数据库");
        this.configuration = configuration;
    }

    @Override
    protected DataSource doBackground() throws Exception {
        updateMessage("正在连接数据库");
        updateProgress(0, 1);
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setJdbcUrl(configuration.getUrl());
        config.setUsername(configuration.getUsername());
        config.setPassword(configuration.getPassword());
        config.setConnectionTimeout(1000 * 5L);
        HikariDataSource dataSource = new HikariDataSource(config);
        updateMessage("连接数据库完成");
        updateProgress(1, 1);
        return dataSource;
    }
}