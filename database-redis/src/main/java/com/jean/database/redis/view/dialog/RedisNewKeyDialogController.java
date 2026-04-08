package com.jean.database.redis.view.dialog;

import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultController;
import com.jean.database.redis.RedisConstant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Redis 新增 Key 对话框 Controller
 */
public class RedisNewKeyDialogController extends DefaultController implements Initializable {

    public static final String ATTR_CALLBACK = "callback";

    @FXML
    private TextField keyField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextArea valueArea;
    @FXML
    private TextField ttlField;
    @FXML
    private CheckBox persistCheckBox;

    public RedisNewKeyDialogController(ControllerContext context) {
        super(context);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化类型下拉框
        typeComboBox.getItems().addAll(
                RedisConstant.KeyType.STRING,
                RedisConstant.KeyType.LIST,
                RedisConstant.KeyType.SET,
                RedisConstant.KeyType.ZSET,
                RedisConstant.KeyType.HASH
        );
        typeComboBox.getSelectionModel().selectFirst();

        // 默认 TTL 为 -1（永不过期）
        ttlField.setText("-1");

        // 监听永不过期复选框
        persistCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                ttlField.setText("-1");
                ttlField.setDisable(true);
            } else {
                ttlField.setDisable(false);
            }
        });
    }

    public String getKey() {
        return keyField.getText();
    }

    public String getType() {
        return typeComboBox.getValue();
    }

    public String getValue() {
        return valueArea.getText();
    }

    public long getTtl() {
        String text = ttlField.getText();
        if (text == null || text.isEmpty()) {
            return -1;
        }
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public Callback<Boolean, Void> getCallback() {
        return getContext().getAttribute(ATTR_CALLBACK);
    }
}
