package com.jean.database.mysql;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL 语法高亮处理器
 */
public class SQLSyntaxHighlighter {

    // SQL 关键字
    private static final String KEYWORDS = "SELECT|FROM|WHERE|INSERT|UPDATE|DELETE|CREATE|DROP|ALTER|TABLE|DATABASE|SCHEMA|VIEW|INDEX|TRIGGER|PROCEDURE|FUNCTION|RETURN|AS|JOIN|LEFT|RIGHT|INNER|OUTER|ON|AND|OR|NOT|IN|LIKE|BETWEEN|ORDER|GROUP|HAVING|LIMIT|OFFSET|ASC|DESC|NULL|NOT|IS|DISTINCT|ALL|ANY|SOME|UNION|INTERSECT|EXCEPT|VALUES|SET|DEFAULT|PRIMARY|FOREIGN|KEY|REFERENCES|CHECK|UNIQUE|AUTO_INCREMENT|DEFAULT|NULL|NOT|NULL|TRUE|FALSE";
    
    // 字符串模式
    private static final String STRING_PATTERN = "('([^'\\]|\\.)*')|(\"([^\"\\]|\\.)*\")";
    
    // 数字模式
    private static final String NUMBER_PATTERN = "\\b\\d+(\\.\\d+)?\\b";
    
    // 注释模式
    private static final String COMMENT_PATTERN = "--.*$|/\\*[\\s\\S]*?\\*/";
    
    // 综合模式
    private static final String PATTERN = "(?<KEYWORD>" + KEYWORDS + ")|(?<STRING>" + STRING_PATTERN + ")|(?<NUMBER>" + NUMBER_PATTERN + ")|(?<COMMENT>" + COMMENT_PATTERN + ")";
    
    private static final Pattern SQL_PATTERN = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    /**
     * 为 SQL 文本添加语法高亮
     *
     * @param sql SQL 文本
     * @param textFlow 文本流组件
     */
    public static void highlightSQL(String sql, TextFlow textFlow) {
        textFlow.getChildren().clear();
        
        if (sql == null || sql.isEmpty()) {
            return;
        }
        
        Matcher matcher = SQL_PATTERN.matcher(sql);
        int lastIndex = 0;
        
        while (matcher.find()) {
            // 添加匹配前的普通文本
            if (matcher.start() > lastIndex) {
                Text normalText = new Text(sql.substring(lastIndex, matcher.start()));
                textFlow.getChildren().add(normalText);
            }
            
            // 根据匹配类型添加高亮文本
            if (matcher.group("KEYWORD") != null) {
                Text keywordText = new Text(matcher.group("KEYWORD"));
                keywordText.setFill(Color.BLUE);
                textFlow.getChildren().add(keywordText);
            } else if (matcher.group("STRING") != null) {
                Text stringText = new Text(matcher.group("STRING"));
                stringText.setFill(Color.GREEN);
                textFlow.getChildren().add(stringText);
            } else if (matcher.group("NUMBER") != null) {
                Text numberText = new Text(matcher.group("NUMBER"));
                numberText.setFill(Color.ORANGE);
                textFlow.getChildren().add(numberText);
            } else if (matcher.group("COMMENT") != null) {
                Text commentText = new Text(matcher.group("COMMENT"));
                commentText.setFill(Color.GRAY);
                textFlow.getChildren().add(commentText);
            }
            
            lastIndex = matcher.end();
        }
        
        // 添加剩余的普通文本
        if (lastIndex < sql.length()) {
            Text normalText = new Text(sql.substring(lastIndex));
            textFlow.getChildren().add(normalText);
        }
    }
}