package com.jean.database.mysql;

import com.jean.database.api.DefaultController;
import com.jean.database.api.ViewContext;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.ResourceBundle;

public class MySQLConfigurationController extends DefaultController implements Initializable {

    public TextField name;
    public TextField host;
    public TextField port;
    public TextField user;
    public PasswordField password;
    public TextField properties;
    public TextField location;
    public ComboBox<Charset> charset;
    private StringConverter<Properties> converter;

    public MySQLConfigurationController(ViewContext viewContext) {
        super(viewContext);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        converter = new MySQLPropertiesConverter();

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
