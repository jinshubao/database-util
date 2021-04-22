package com.jean.database.redis;

import com.jean.database.api.DefaultController;
import com.jean.database.api.ViewContext;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RedisConnectionConfigurationController extends DefaultController implements Initializable {

    public TextField name;
    public TextField host;
    public TextField port;
    public PasswordField password;
    public TextField properties;
    private RedisConnectionConfiguration defaultConfiguration;

    public RedisConnectionConfigurationController(ViewContext viewContext, RedisConnectionConfiguration defaultConfiguration) {
        super(viewContext);
        this.defaultConfiguration = defaultConfiguration;

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
