package com.jean.database.mysql;

import com.jean.database.api.BaseTask;
import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultController;
import com.jean.database.api.TaskManger;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * MySQL 连接配置 Controller
 */
public class MySQLConfigurationController extends DefaultController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MySQLConfigurationController.class);

    public static final String ATTR_DEFAULT_CONFIGURATION = "defaultConfiguration";

    public TextField name;
    public TextField host;
    public TextField port;
    public TextField user;
    public PasswordField password;
    public TextField properties;
    public TextField location;
    public ComboBox<Charset> charset;
    public Button testConnectionButton;
    private StringConverter<Properties> converter;

    public MySQLConfigurationController(ControllerContext context) {
        super(context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        converter = new MySQLPropertiesConverter();
        
        // 初始化字符集下拉框
        initCharsetComboBox();
        
        // 添加输入验证
        addInputValidation();
        
        // 添加测试连接按钮事件
        testConnectionButton.setOnAction(event -> testConnection());
    }
    
    private void addInputValidation() {
        // 端口号验证
        port.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                port.setText(oldValue);
            }
        });
        
        // 连接名称验证
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                name.setStyle("-fx-border-color: red;");
            } else {
                name.setStyle("");
            }
        });
        
        // 主机名验证
        host.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                host.setStyle("-fx-border-color: red;");
            } else {
                host.setStyle("");
            }
        });
        
        // 用户名验证
        user.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                user.setStyle("-fx-border-color: red;");
            } else {
                user.setStyle("");
            }
        });
    }
    
    private boolean validateInput() {
        boolean isValid = true;
        
        // 验证连接名称
        if (name.getText() == null || name.getText().trim().isEmpty()) {
            name.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            name.setStyle("");
        }
        
        // 验证主机名
        if (host.getText() == null || host.getText().trim().isEmpty()) {
            host.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            host.setStyle("");
        }
        
        // 验证端口号
        if (port.getText() == null || port.getText().trim().isEmpty()) {
            port.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            try {
                int portNumber = Integer.parseInt(port.getText());
                if (portNumber < 1 || portNumber > 65535) {
                    port.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    port.setStyle("");
                }
            } catch (NumberFormatException e) {
                port.setStyle("-fx-border-color: red;");
                isValid = false;
            }
        }
        
        // 验证用户名
        if (user.getText() == null || user.getText().trim().isEmpty()) {
            user.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            user.setStyle("");
        }
        
        return isValid;
    }
    
    private void initCharsetComboBox() {
        charset.getItems().addAll(Charset.availableCharsets().values());
        charset.setValue(Charset.defaultCharset());
    }
    
    private void testConnection() {
        if (!validateInput()) {
            com.jean.database.api.utils.DialogUtil.information("输入验证", "输入验证", "请检查输入参数");
            return;
        }

        MySQLConnectionConfiguration configuration;
        try {
            configuration = getValue();
        } catch (Exception e) {
            com.jean.database.api.utils.DialogUtil.information("输入验证", "输入验证", "请检查输入参数");
            return;
        }

        String originalText = testConnectionButton.getText();
        testConnectionButton.setDisable(true);
        testConnectionButton.setText("测试中...");

        Task<Void> task = new BaseTask<Void>() {
            @Override
            protected Void call() throws Exception {
                DataSourceManager.testConnectionDirect(configuration);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                testConnectionButton.setDisable(false);
                testConnectionButton.setText(originalText);
                com.jean.database.api.utils.DialogUtil.information("连接测试", "连接测试", "连接测试成功！");
            }

            @Override
            protected void failed() {
                super.failed();
                testConnectionButton.setDisable(false);
                testConnectionButton.setText(originalText);
            }
        };

        TaskManger.execute(task);
    }

    public void setValue(MySQLConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration != null) {
            name.setText(connectionConfiguration.getConnectionName());
            host.setText(connectionConfiguration.getHost());
            port.setText(String.valueOf(connectionConfiguration.getPort()));
            user.setText(connectionConfiguration.getUsername());
            password.setText(connectionConfiguration.getPassword());
            properties.setText(converter.toString(connectionConfiguration.getProperties()));
        }
    }

    public MySQLConnectionConfiguration getValue() {
        if (!validateInput()) {
            throw new IllegalArgumentException("输入参数无效");
        }
        
        String nameText = name.getText();
        String hostText = host.getText();
        Integer portText = Integer.valueOf(port.getText());
        String userText = user.getText();
        String passwordText = password.getText();
        String propertiesText = properties.getText();
        Properties properties = converter.fromString(propertiesText);
        return new MySQLConnectionConfiguration(nameText, hostText, portText, userText, passwordText, properties);
    }

}
