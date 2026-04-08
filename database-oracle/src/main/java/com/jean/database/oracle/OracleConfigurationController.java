package com.jean.database.oracle;

import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultController;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Oracle 连接配置 Controller
 */
public class OracleConfigurationController extends DefaultController implements Initializable {

    public static final String ATTR_DEFAULT_CONFIGURATION = "defaultConfiguration";

    public TextField name;
    public TextField host;
    public TextField port;
    public TextField user;
    public PasswordField password;
    public TextField properties;
    public TextField location;
    public ComboBox<Charset> charset;
    private StringConverter<Properties> converter;

    private OracleConnectionConfiguration defaultConfiguration;

    public OracleConfigurationController(ControllerContext context) {
        super(context);
        this.defaultConfiguration = context.getAttribute(ATTR_DEFAULT_CONFIGURATION);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        converter = new OraclePropertiesStringConverter();
        setValue(defaultConfiguration);
    }

    private void setValue(OracleConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration != null) {
            name.setText(connectionConfiguration.getConnectionName());
            host.setText(connectionConfiguration.getHost());
            port.setText(String.valueOf(connectionConfiguration.getPort()));
            user.setText(connectionConfiguration.getUsername());
            password.setText(connectionConfiguration.getPassword());
            properties.setText(converter.toString(connectionConfiguration.getProperties()));
        }
    }

    public OracleConnectionConfiguration getValue() {
        String nameText = name.getText();
        String hostText = host.getText();
        Integer portText = Integer.valueOf(port.getText());
        String userText = user.getText();
        String passwordText = password.getText();
        String propertiesText = properties.getText();
        Properties properties = converter.fromString(propertiesText);
        return new OracleConnectionConfiguration(nameText, hostText, portText, userText, passwordText, properties);
    }
}
