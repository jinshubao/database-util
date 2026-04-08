package com.jean.database.redis;

import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultController;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Redis 连接配置 Controller
 */
public class RedisConnectionConfigurationController extends DefaultController implements Initializable {

    public static final String ATTR_DEFAULT_CONFIGURATION = "defaultConfiguration";

    public TextField name;
    public TextField host;
    public TextField port;
    public PasswordField password;
    public TextField properties;
    private RedisConnectionConfiguration defaultConfiguration;

    public RedisConnectionConfigurationController(ControllerContext context) {
        super(context);
        this.defaultConfiguration = context.getAttribute(ATTR_DEFAULT_CONFIGURATION);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(defaultConfiguration.getConnectionName());
        host.setText(defaultConfiguration.getHost());
        port.setText(String.valueOf(defaultConfiguration.getPort()));
        password.setText(defaultConfiguration.getPassword());
        properties.setText(null);
    }

    public RedisConnectionConfiguration getValue() {
        String nameText = name.getText();
        String hostText = host.getText();
        Integer portText = Integer.valueOf(port.getText());
        String passwordText = password.getText();
        return new RedisConnectionConfiguration(nameText, hostText, portText, passwordText);
    }
}
