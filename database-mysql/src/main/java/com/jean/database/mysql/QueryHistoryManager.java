package com.jean.database.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 查询历史记录管理器
 * 用于存储和管理 SQL 查询历史
 */
public class QueryHistoryManager {

    private static final Logger logger = LoggerFactory.getLogger(QueryHistoryManager.class);

    // 历史记录列表
    private final List<String> history = new CopyOnWriteArrayList<>();
    
    // 最大历史记录数量
    private static final int MAX_HISTORY_SIZE = 100;
    
    // 历史记录文件路径
    private static final String HISTORY_FILE = System.getProperty("user.home") + "/.database-mgr/mysql-history.txt";

    /**
     * 构造函数，加载历史记录
     */
    public QueryHistoryManager() {
        loadHistory();
    }

    /**
     * 添加查询到历史记录
     *
     * @param sql SQL 语句
     */
    public void addHistory(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return;
        }
        
        // 移除重复的历史记录
        history.remove(sql);
        
        // 添加到历史记录开头
        history.add(0, sql);
        
        // 限制历史记录数量
        if (history.size() > MAX_HISTORY_SIZE) {
            history.remove(history.size() - 1);
        }
        
        // 保存历史记录
        saveHistory();
    }

    /**
     * 获取历史记录列表
     *
     * @return 历史记录列表
     */
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    /**
     * 清空历史记录
     */
    public void clearHistory() {
        history.clear();
        saveHistory();
    }

    /**
     * 加载历史记录
     */
    private void loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    history.add(line);
                }
            }
            logger.debug("加载历史记录成功，共 {} 条", history.size());
        } catch (IOException e) {
            logger.error("加载历史记录失败", e);
        }
    }

    /**
     * 保存历史记录
     */
    private void saveHistory() {
        File file = new File(HISTORY_FILE);
        
        // 创建目录
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String sql : history) {
                writer.write(sql);
                writer.newLine();
            }
            logger.debug("保存历史记录成功，共 {} 条", history.size());
        } catch (IOException e) {
            logger.error("保存历史记录失败", e);
        }
    }
}
