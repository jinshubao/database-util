package com.jean.database.client.controller;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.connection.MySqlConnectionConfiguration;
import com.jean.database.core.constant.DatabaseType;
import com.jean.database.core.constant.EncodingType;
import com.jean.database.core.utils.StringUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
@Controller
public class ConnectionController extends BaseController {

    private static final String DEFAULT_HOST = "10.52.2.110";
    private static final Integer DEFAULT_PORT = 3307;
    private static final String DEFAULT_USER = "dev_sy8";
    private static final String DEFAULT_PASSWORD = "w2kFS02fD8";

    @FXML
    private ComboBox<DatabaseType> dataBaseType;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private ComboBox<EncodingType> encoding;
    @FXML
    private TextField properties;
    @FXML
    private Button testConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dataBaseType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.properties.setPromptText(newValue.getDefaultProperties());
                this.properties.setText(newValue.getDefaultProperties());
            } else {
                this.properties.setText(null);
                this.properties.setPromptText(null);
            }
        });
        this.host.setText(DEFAULT_HOST);
        this.port.setText(DEFAULT_PORT.toString());
        this.user.setText(DEFAULT_USER);
        this.password.setText(DEFAULT_PASSWORD);
        //其他数据库暂时不支持
        this.dataBaseType.getItems().addAll(DatabaseType.MySql);
        this.dataBaseType.getSelectionModel().selectFirst();
        this.encoding.getItems().addAll(EncodingType.values());
        this.encoding.getSelectionModel().selectFirst();
    }

    public IConnectionConfiguration getConnectionConfig() {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        if (StringUtil.isNotBlank(this.host.getText())) {
            host = this.host.getText();
        }
        if (StringUtil.isNotBlank(this.port.getText())) {
            try {
                port = Integer.parseInt(this.port.getText());
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
            }
        }
        DatabaseType databaseType = this.dataBaseType.getValue();
        if (databaseType == DatabaseType.MySql) {
            MySqlConnectionConfiguration configuration = new MySqlConnectionConfiguration();
            configuration.setDatabaseType(databaseType);
            configuration.setHost(host);
            configuration.setPort(port);
            configuration.setUser(this.user.getText());
            configuration.setPassword(this.password.getText());
            configuration.setStringProperties(this.properties.getText());
            return configuration;
        }
        return null;
    }

}
