package com.jean.database.redis;

import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RedisConnectionConfigurationController implements Initializable {

    public TextField name;
    public TextField host;
    public TextField port;
    public PasswordField password;
    public TextField properties;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void setValue(RedisConnectionConfiguration connectionConfiguration) {
        name.setText(connectionConfiguration.getConnectionName());
        host.setText(connectionConfiguration.getHost());
        port.setText(String.valueOf(connectionConfiguration.getPort()));
        password.setText(connectionConfiguration.getPassword());
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
