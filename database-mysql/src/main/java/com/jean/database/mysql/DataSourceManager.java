package com.jean.database.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.sql.SQLConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源管理器
 * 负责创建、管理和测试数据库连接
 */
public class DataSourceManager {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

    // 数据源缓存
    private static final Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

    /**
     * 创建数据源
     *
     * @param configuration 连接配置
     * @return 数据源
     */
    public static DataSource createDataSource(SQLConnectionConfiguration configuration) {
        String key = generateDataSourceKey(configuration);
        
        // 尝试从缓存获取
        if (dataSourceCache.containsKey(key)) {
            logger.debug("从缓存获取数据源: {}", key);
            return dataSourceCache.get(key);
        }

        // 创建新的数据源
        logger.debug("创建新的数据源: {}", key);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(configuration.getUrl());
        dataSource.setUsername(configuration.getUsername());
        dataSource.setPassword(configuration.getPassword());
        
        // 连接池配置
        dataSource.setMaxActive(10);
        dataSource.setMinIdle(2);
        dataSource.setInitialSize(1);
        dataSource.setMaxWait(60000); // 60秒
        dataSource.setTimeBetweenEvictionRunsMillis(60000); // 60秒
        dataSource.setMinEvictableIdleTimeMillis(300000); // 5分钟
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        
        // 添加到缓存
        dataSourceCache.put(key, dataSource);
        return dataSource;
    }

    /**
     * 测试连接（使用已有数据源）
     *
     * @param dataSource 数据源
     * @return 是否连接成功
     */
    public static boolean testConnection(DataSource dataSource) {
        Connection connection = null;
        try {
            logger.debug("测试数据库连接");
            connection = dataSource.getConnection();
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.error("连接测试失败", e);
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("关闭连接失败", e);
                }
            }
        }
    }

    /**
     * 直连测试（不走连接池缓存，超时5秒），专供「测试连接」按钮使用。
     * 不会污染连接池缓存。
     *
     * @param configuration 连接配置
     * @return 是否连接成功
     * @throws SQLException 连接失败时抛出，包含详细错误信息
     */
    public static void testConnectionDirect(SQLConnectionConfiguration configuration) throws SQLException {
        String url = configuration.getUrl();
        // 在 URL 上强制追加 MySQL 超时参数，确保 connectTimeout 真正生效
        String separator = url.contains("?") ? "&" : "?";
        if (!url.contains("connectTimeout")) {
            url += separator + "connectTimeout=5000";
            separator = "&";
        }
        if (!url.contains("socketTimeout")) {
            url += separator + "socketTimeout=5000";
        }
        try (Connection conn = java.sql.DriverManager.getConnection(url, configuration.getUsername(), configuration.getPassword())) {
            if (conn == null || conn.isClosed()) {
                throw new SQLException("无法获取有效连接");
            }
        }
    }

    /**
     * 关闭数据源
     *
     * @param configuration 连接配置
     */
    public static void closeDataSource(SQLConnectionConfiguration configuration) {
        String key = generateDataSourceKey(configuration);
        DataSource dataSource = dataSourceCache.remove(key);
        if (dataSource instanceof DruidDataSource) {
            ((DruidDataSource) dataSource).close();
            logger.debug("关闭数据源: {}", key);
        }
    }

    /**
     * 生成数据源缓存键
     *
     * @param configuration 连接配置
     * @return 缓存键
     */
    private static String generateDataSourceKey(SQLConnectionConfiguration configuration) {
        return configuration.getUrl() + "|" + configuration.getUsername();
    }

    /**
     * 清空所有数据源
     */
    public static void clearAllDataSources() {
        for (DataSource dataSource : dataSourceCache.values()) {
            if (dataSource instanceof DruidDataSource) {
                ((DruidDataSource) dataSource).close();
            }
        }
        dataSourceCache.clear();
        logger.debug("清空所有数据源");
    }

    /**
     * 获取数据源数量
     */
    public static int getDataSourceCount() {
        return dataSourceCache.size();
    }
}
