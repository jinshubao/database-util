package com.jean.database.mysql.exception;

import com.jean.database.api.utils.DialogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MySQL 异常处理器
 * 统一处理 MySQL 相关的异常
 */
public class MySQLExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MySQLExceptionHandler.class);

    /**
     * 处理异常
     *
     * @param e 异常
     * @param message 错误消息
     */
    public static void handleException(Exception e, String message) {
        logger.error(message, e);
        DialogUtil.error(message, e);
    }

    /**
     * 处理连接异常
     *
     * @param e 异常
     */
    public static void handleConnectionException(Exception e) {
        handleException(e, "数据库连接失败");
    }

    /**
     * 处理 SQL 执行异常
     *
     * @param e 异常
     */
    public static void handleSqlExecutionException(Exception e) {
        handleException(e, "SQL 执行失败");
    }

    /**
     * 处理配置异常
     *
     * @param e 异常
     */
    public static void handleConfigurationException(Exception e) {
        handleException(e, "配置错误");
    }

    /**
     * 处理通用异常
     *
     * @param e 异常
     */
    public static void handleGeneralException(Exception e) {
        handleException(e, "操作失败");
    }
}
