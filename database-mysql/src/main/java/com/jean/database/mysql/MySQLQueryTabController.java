package com.jean.database.mysql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.jean.database.api.BaseTask;
import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultController;
import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.sql.factory.TableCellFactory;
import com.jean.database.sql.view.SelectableTableView;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * MySQL 查询标签页 Controller
 */
public class MySQLQueryTabController extends DefaultController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MySQLQueryTabController.class);

    public static final String ATTR_CATALOGS = "catalogs";
    public static final String ATTR_CATALOG = "catalog";

    public Pane root;
    public ComboBox<String> catalogComboBox;
    public ComboBox<String> historyComboBox;
    public Button explainButton;
    public Button formatButton;
    public Button saveButton;
    public Button executeButton;
    public CodeArea sqlTextArea;
    public SplitPane splitPane;
    public TabPane resultTabPane;
    public Tab executeInfoTab;
    public TextArea executeInfoTextArea;
    public Tab analyseTab;
    public TableView<ExecuteAnalyse> analyseTableView;
    public TableColumn<ExecuteAnalyse, String> analyseStatusColumn;
    public TableColumn<ExecuteAnalyse, String> analyseDurationColumn;
    public TableColumn<ExecuteAnalyse, String> analysePercentageColumn;
    public Tab statusTab;
    public TableView<ExecuteStatus> statusTableView;
    public TableColumn<ExecuteStatus, String> statusVariableNameColumn;
    public TableColumn<ExecuteStatus, String> statusValueColumn;
    public TableColumn<ExecuteStatus, String> statusDescriptionColumn;

    private List<String> catalogs;
    private String catalog;
    private javax.sql.DataSource dataSource;
    private QueryHistoryManager historyManager;

    public MySQLQueryTabController(ControllerContext context) {
        super(context);
        this.catalogs = context.getAttribute(ATTR_CATALOGS);
        this.catalog = context.getAttribute(ATTR_CATALOG);
        this.dataSource = context.getAttribute("dataSource");
        this.historyManager = new QueryHistoryManager();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize mysql query tab");

        this.initializeFunctionBar();

        this.initializeSqlTextArea();

        this.initializeExecuteResult();

    }
    
    private void initializeSqlTextArea() {
        // 加载 CSS 样式
        String cssPath = getClass().getResource("/css/sql-highlight.css").toExternalForm();
        sqlTextArea.getStylesheets().add(cssPath);

        // 设置行号
        sqlTextArea.setParagraphGraphicFactory(LineNumberFactory.get(sqlTextArea));

        // 设置语法高亮
        sqlTextArea.textProperty().addListener((obs, oldText, newText) -> {
            sqlTextArea.setStyleSpans(0, computeHighlighting(newText));
        });

        // 设置初始内容
        sqlTextArea.replaceText("");

        // Ctrl + Enter 执行选中 SQL
        sqlTextArea.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.ENTER) {
                executeSql();
                event.consume();
            }
        });
    }


    private void initializeFunctionBar() {
        catalogComboBox.getItems().addAll(this.catalogs);
        catalogComboBox.getSelectionModel().select(catalog);

        // 初始化历史记录下拉菜单
        updateHistoryComboBox();
        historyComboBox.setOnAction(event -> {
            String selectedSql = historyComboBox.getSelectionModel().getSelectedItem();
            if (selectedSql != null) {
                sqlTextArea.replaceText(selectedSql);
            }
        });

        formatButton.setOnAction(event -> {
            String sql = sqlTextArea.getText();
            try {
                String result = SQLUtils.format(sql, JdbcConstants.MYSQL, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
                sqlTextArea.replaceText(result);
            } catch (Throwable e) {
                DialogUtil.error(e);
            }
        });
    }
    
    private void updateHistoryComboBox() {
        historyComboBox.getItems().clear();
        historyComboBox.getItems().addAll(historyManager.getHistory());
    }


    private void initializeExecuteResult() {

        executeInfoTextArea.setText(null);

        analyseStatusColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus()));
        analyseDurationColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDuration()));
        analysePercentageColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPercentage()));

        statusVariableNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getVariableName()));
        statusValueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));
        statusDescriptionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDescription()));

        // 添加执行按钮事件
        executeButton.setOnAction(event -> executeSql());
    }

    private void executeSql() {
        String sql = sqlTextArea.getSelectedText();
        if (sql == null || sql.trim().isEmpty()) {
            sql = sqlTextArea.getText();
        }
        sql = sql.trim();
        if (sql.isEmpty()) {
            executeInfoTextArea.setText("请输入 SQL 语句");
            return;
        }

        // 添加到历史记录
        historyManager.addHistory(sql);
        updateHistoryComboBox();

        executeInfoTextArea.setText("正在执行 SQL...");
        resultTabPane.setVisible(true);

        TaskManger.execute(new ExecuteSqlTask(sql, catalog, dataSource));
    }

    private class ExecuteSqlTask extends BaseTask<ExecuteSqlResult> {

        private final String sql;
        private final String catalog;
        private final javax.sql.DataSource dataSource;

        public ExecuteSqlTask(String sql, String catalog, javax.sql.DataSource dataSource) {
            this.sql = sql;
            this.catalog = catalog;
            this.dataSource = dataSource;
        }

        @Override
        protected void scheduled() {
            updateMessage("正在执行 SQL...");
        }

        @Override
        protected ExecuteSqlResult call() throws Exception {
            long startTime = System.currentTimeMillis();

            try (java.sql.Connection connection = dataSource.getConnection()) {
                // 设置当前数据库
                if (catalog != null) {
                    connection.setCatalog(catalog);
                }

                java.sql.Statement statement = connection.createStatement();
                boolean hasResult = statement.execute(sql);

                if (hasResult) {
                    // 处理查询结果
                    java.sql.ResultSet resultSet = statement.getResultSet();
                    try {
                        return processResultSet(resultSet, startTime);
                    } catch (java.sql.SQLException e) {
                        logger.error("处理查询结果出错", e);
                        throw new RuntimeException("处理查询结果出错: " + e.getMessage(), e);
                    }
                } else {
                    // 处理更新结果
                    int updateCount = statement.getUpdateCount();
                    long duration = System.currentTimeMillis() - startTime;
                    return new ExecuteSqlResult(updateCount, duration);
                }
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw e;
                }
                throw new RuntimeException("执行出错: " + e.getMessage(), e);
            }
        }

        private ExecuteSqlResult processResultSet(java.sql.ResultSet resultSet, long startTime) throws java.sql.SQLException {
            // 提取元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 创建列
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // 填充数据
            List<Map<String, Object>> data = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                data.add(row);
            }

            long duration = System.currentTimeMillis() - startTime;
            return new ExecuteSqlResult(columnNames, data, duration);
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            ExecuteSqlResult result = getValue();
            if (result.isQueryResult()) {
                // 显示查询结果
                showQueryResult(result.getColumnNames(), result.getData(), result.getDuration());
            } else {
                // 显示更新结果
                executeInfoTextArea.setText("执行成功，影响行数: " + result.getUpdateCount());
                executeInfoTextArea.appendText("\n执行时间: " + result.getDuration() + "ms");
            }
        }

        private void showQueryResult(String[] columnNames, List<Map<String, Object>> data, long duration) {
            // 统一数据模型
            List<Map<String, ObjectProperty>> convertedData = new ArrayList<>();
            for (Map<String, Object> row : data) {
                Map<String, ObjectProperty> newRow = new LinkedHashMap<>();
                for (String colName : columnNames) {
                    newRow.put(colName, new SimpleObjectProperty<>(row.get(colName)));
                }
                convertedData.add(newRow);
            }

            SelectableTableView<Map<String, ObjectProperty>> resultTable = new SelectableTableView<>();
            resultTable.setEditable(false);

            for (String columnName : columnNames) {
                TableColumn<Map<String, ObjectProperty>, ObjectProperty> column = new TableColumn<>(columnName);
                column.setCellValueFactory(param -> param.getValue().get(columnName));
                column.setCellFactory(TableCellFactory.forTableView());
                column.setSortable(false);
                resultTable.getColumns().add(column);
            }
            resultTable.getItems().addAll(convertedData);

            Tab resultTab = new Tab("查询结果");
            resultTab.setContent(resultTable);

            resultTabPane.getTabs().clear();
            resultTabPane.getTabs().addAll(executeInfoTab, analyseTab, statusTab);
            resultTabPane.getTabs().add(0, resultTab);
            resultTabPane.getSelectionModel().select(resultTab);
            executeInfoTextArea.setText("查询成功，返回行数: " + data.size());
            executeInfoTextArea.appendText("\n执行时间: " + duration + "ms");
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable exception = getException();
            if (exception != null) {
                executeInfoTextArea.setText(exception.getMessage());
            }
        }
    }

    private static class ExecuteSqlResult {
        private final String[] columnNames;
        private final List<Map<String, Object>> data;
        private final int updateCount;
        private final long duration;
        private final boolean queryResult;

        // 查询结果构造方法
        public ExecuteSqlResult(String[] columnNames, List<Map<String, Object>> data, long duration) {
            this.columnNames = columnNames;
            this.data = data;
            this.updateCount = 0;
            this.duration = duration;
            this.queryResult = true;
        }

        // 更新结果构造方法
        public ExecuteSqlResult(int updateCount, long duration) {
            this.columnNames = null;
            this.data = null;
            this.updateCount = updateCount;
            this.duration = duration;
            this.queryResult = false;
        }

        public boolean isQueryResult() {
            return queryResult;
        }

        public String[] getColumnNames() {
            return columnNames;
        }

        public List<Map<String, Object>> getData() {
            return data;
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public long getDuration() {
            return duration;
        }
    }
    
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        if (text == null || text.isEmpty()) {
            spansBuilder.add(java.util.Collections.emptyList(), 0);
            return spansBuilder.create();
        }

        // SQL 关键字
        String[] keywords = {"SELECT", "FROM", "WHERE", "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER", "TABLE", "DATABASE", "SCHEMA", "VIEW", "INDEX", "TRIGGER", "PROCEDURE", "FUNCTION", "RETURN", "AS", "JOIN", "LEFT", "RIGHT", "INNER", "OUTER", "ON", "AND", "OR", "NOT", "IN", "LIKE", "BETWEEN", "ORDER", "GROUP", "HAVING", "LIMIT", "OFFSET", "ASC", "DESC", "NULL", "NOT", "IS", "DISTINCT", "ALL", "ANY", "SOME", "UNION", "INTERSECT", "EXCEPT", "VALUES", "SET", "DEFAULT", "PRIMARY", "FOREIGN", "KEY", "REFERENCES", "CHECK", "UNIQUE", "AUTO_INCREMENT", "DEFAULT", "NULL", "NOT", "NULL", "TRUE", "FALSE"};
        
        // 构建正则表达式
        String keywordPattern = String.join("|", keywords);
        String pattern = "(?<KEYWORD>" + keywordPattern + ")|(?<STRING>'([^'\\\\]|\\\\.)*')|(?<NUMBER>\\b\\d+(\\.\\d+)?\\b)|(?<COMMENT>--.*$|/\\*[\\s\\S]*?\\*/)";
        java.util.regex.Pattern sqlPattern = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.MULTILINE);
        java.util.regex.Matcher matcher = sqlPattern.matcher(text);
        
        int lastIndex = 0;
        while (matcher.find()) {
            // 添加普通文本
            if (matcher.start() > lastIndex) {
                spansBuilder.add(java.util.Collections.emptyList(), matcher.start() - lastIndex);
            }
            
            // 添加高亮样式
            if (matcher.group("KEYWORD") != null) {
                spansBuilder.add(java.util.Collections.singleton("keyword"), matcher.end() - matcher.start());
            } else if (matcher.group("STRING") != null) {
                spansBuilder.add(java.util.Collections.singleton("string"), matcher.end() - matcher.start());
            } else if (matcher.group("NUMBER") != null) {
                spansBuilder.add(java.util.Collections.singleton("number"), matcher.end() - matcher.start());
            } else if (matcher.group("COMMENT") != null) {
                spansBuilder.add(java.util.Collections.singleton("comment"), matcher.end() - matcher.start());
            }
            
            lastIndex = matcher.end();
        }
        
        // 添加剩余的普通文本
        if (lastIndex < text.length()) {
            spansBuilder.add(java.util.Collections.emptyList(), text.length() - lastIndex);
        }
        
        return spansBuilder.create();
    }

    public Pane getRoot() {
        return root;
    }
}
