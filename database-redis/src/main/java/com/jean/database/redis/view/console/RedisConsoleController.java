package com.jean.database.redis.view.console;

import com.jean.database.api.BaseTask;
import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultController;
import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.RedisConstant;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * Redis Console Controller
 */
public class RedisConsoleController extends DefaultController implements Initializable {

    public static final String ATTR_CONNECTION_CONFIGURATION = "connectionConfiguration";
    public static final String ATTR_DATABASE = "database";

    private TextArea outputArea;
    private TextField inputField;
    private Button executeButton;
    private Button clearButton;

    private RedisConnectionConfiguration connectionConfiguration;
    private int database;

    public RedisConsoleController(ControllerContext context) {
        super(context);
        this.connectionConfiguration = context.getAttribute(ATTR_CONNECTION_CONFIGURATION);
        this.database = context.getAttribute(ATTR_DATABASE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appendOutput("Redis Console 已连接 - 数据库: db" + database);
        appendOutput("输入 Redis 命令并按回车执行");
        appendOutput("----------------------------------------");
    }

    /**
     * 执行命令
     */
    public void executeCommand() {
        String command = inputField.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        appendOutput("\n> " + command);
        inputField.clear();

        TaskManger.execute(new ExecuteCommandTask(command));
    }

    /**
     * 清空输出
     */
    public void clearOutput() {
        outputArea.clear();
        appendOutput("Redis Console - 数据库: db" + database);
        appendOutput("----------------------------------------");
    }

    /**
     * 追加输出
     */
    private void appendOutput(String text) {
        Platform.runLater(() -> {
            outputArea.appendText(text + "\n");
            // 自动滚动到底部
            outputArea.positionCaret(outputArea.getText().length());
        });
    }

    /**
     * 执行命令任务
     */
    private class ExecuteCommandTask extends BaseTask<String> {

        private final String command;

        public ExecuteCommandTask(String command) {
            this.command = command;
        }

        @Override
        protected void scheduled() {
            updateMessage("正在执行命令...");
        }

        @Override
        protected String call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                commands.select(database);

                // 解析命令
                String[] parts = command.trim().split("\\s+");
                if (parts.length == 0) {
                    return "错误: 空命令";
                }

                String cmd = parts[0].toUpperCase();

                try {
                    switch (cmd) {
                        case "PING" -> {
                            String result = commands.ping();
                            return result;
                        }
                        case "GET" -> {
                            if (parts.length < 2) return "错误: GET 需要 key 参数";
                            byte[] value = commands.get(parts[1].getBytes(RedisConstant.CHARSET_UTF8));
                            return value != null ? new String(value, RedisConstant.CHARSET_UTF8) : "(nil)";
                        }
                        case "SET" -> {
                            if (parts.length < 3) return "错误: SET 需要 key value 参数";
                            commands.set(parts[1].getBytes(RedisConstant.CHARSET_UTF8),
                                    command.substring(command.indexOf(' ', 4) + 1).getBytes(RedisConstant.CHARSET_UTF8));
                            return "OK";
                        }
                        case "DEL" -> {
                            if (parts.length < 2) return "错误: DEL 需要 key 参数";
                            Long count = commands.del(parts[1].getBytes(RedisConstant.CHARSET_UTF8));
                            return "(integer) " + count;
                        }
                        case "EXISTS" -> {
                            if (parts.length < 2) return "错误: EXISTS 需要 key 参数";
                            Long count = commands.exists(parts[1].getBytes(RedisConstant.CHARSET_UTF8));
                            return "(integer) " + count;
                        }
                        case "KEYS" -> {
                            // KEYS 命令在大数据量下会阻塞，不推荐使用
                            return "警告: KEYS 命令会阻塞 Redis，不建议在生产环境使用";
                        }
                        case "DBSIZE" -> {
                            Long size = commands.dbsize();
                            return "(integer) " + size;
                        }
                        case "FLUSHDB" -> {
                            String result = commands.flushdb();
                            return result;
                        }
                        case "INFO" -> {
                            String info = commands.info();
                            return info;
                        }
                        case "TYPE" -> {
                            if (parts.length < 2) return "错误: TYPE 需要 key 参数";
                            String type = commands.type(parts[1].getBytes(RedisConstant.CHARSET_UTF8));
                            return type;
                        }
                        case "TTL" -> {
                            if (parts.length < 2) return "错误: TTL 需要 key 参数";
                            Long ttl = commands.ttl(parts[1].getBytes(RedisConstant.CHARSET_UTF8));
                            return "(integer) " + ttl;
                        }
                        case "EXPIRE" -> {
                            if (parts.length < 3) return "错误: EXPIRE 需要 key seconds 参数";
                            Boolean result = commands.expire(parts[1].getBytes(RedisConstant.CHARSET_UTF8), Long.parseLong(parts[2]));
                            return result ? "(integer) 1" : "(integer) 0";
                        }
                        case "LLEN" -> {
                            if (parts.length < 2) return "错误: LLEN 需要 key 参数";
                            Long len = commands.llen(parts[1].getBytes(RedisConstant.CHARSET_UTF8));
                            return "(integer) " + len;
                        }
                        case "SCARD" -> {
                            if (parts.length < 2) return "错误: SCARD 需要 key 参数";
                            Long count = commands.scard(parts[1].getBytes(RedisConstant.CHARSET_UTF8));
                            return "(integer) " + count;
                        }
                        case "HLEN" -> {
                            if (parts.length < 2) return "错误: HLEN 需要 key 参数";
                            Long count = commands.hlen(parts[1].getBytes(RedisConstant.CHARSET_UTF8));
                            return "(integer) " + count;
                        }
                        case "ZCOUNT" -> {
                            if (parts.length < 4) return "错误: ZCOUNT 需要 key min max 参数";
                            Long count = commands.zcount(parts[1].getBytes(RedisConstant.CHARSET_UTF8),
                                    Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                            return "(integer) " + count;
                        }
                        default -> {
                            // 尝试使用原生命令
                            return "不支持的命令: " + cmd + "\n支持的命令: PING, GET, SET, DEL, EXISTS, KEYS, DBSIZE, FLUSHDB, INFO, TYPE, TTL, EXPIRE, LLEN, SCARD, HLEN, ZCOUNT";
                        }
                    }
                } catch (Exception e) {
                    return "错误: " + e.getMessage();
                }
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            appendOutput(getValue());
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            if (ex != null) {
                appendOutput("执行失败: " + ex.getMessage());
            }
        }
    }
}
