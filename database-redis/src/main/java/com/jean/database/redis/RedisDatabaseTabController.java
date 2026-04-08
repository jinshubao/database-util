package com.jean.database.redis;

import com.jean.database.api.BaseTask;
import com.jean.database.api.ControllerContext;
import com.jean.database.api.DefaultController;
import com.jean.database.api.TaskManger;
import com.jean.database.api.TableViewRowIndexColumnCellFactory;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.redis.factory.RedisKeyTableRowFactory;
import com.jean.database.redis.factory.RedisValueTableRowFactory;
import com.jean.database.redis.factory.TableViewByteColumnCellFactory;
import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.model.RedisValue;
import com.jean.database.redis.view.handler.IRedisKeyActionEventHandler;
import com.jean.database.redis.view.handler.IRedisValueActionEventHandler;
import com.jean.database.redis.view.handler.impl.RedisKeyActionEventHandlerImpl;
import com.jean.database.redis.view.handler.impl.RedisValueActionEventHandlerImpl;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Redis 数据库标签页 Controller
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class RedisDatabaseTabController extends DefaultController implements Initializable {

    public static final String ATTR_OBJECT_TAB_CONTROLLER = "objectTabController";
    public static final String ATTR_CONNECTION_CONFIGURATION = "connectionConfiguration";
    public static final String ATTR_DATABASE = "database";

    public SplitPane root;
    public TableView<RedisKey> keyTableView;
    public SplitPane valueSplitPane;
    public TableView<RedisValue> valueTableView;
    public TextField keyTextFiled;
    public TextArea valueTextArea;
    public Button saveButton;
    private Tab databaseTab;

    private RedisObjectTabController objectTabController;
    private RedisConnectionConfiguration connectionConfiguration;
    private int database;
    private RedisKey currentKey;

    public RedisDatabaseTabController(ControllerContext context) {
        super(context);
        this.databaseTab = new Tab(getTitle());
        this.objectTabController = context.getAttribute(ATTR_OBJECT_TAB_CONTROLLER);
        this.connectionConfiguration = context.getAttribute(ATTR_CONNECTION_CONFIGURATION);
        this.database = context.getAttribute(ATTR_DATABASE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.databaseTab.setContent(root);
        this.databaseTab.setOnCloseRequest(event -> {

        });

        TableViewRowIndexColumnCellFactory tableViewRowIndexColumnCellFactory = new TableViewRowIndexColumnCellFactory();
        TableViewByteColumnCellFactory tableViewByteColumnCellFactory = new TableViewByteColumnCellFactory();

        TableColumn<RedisKey, Integer> keyNoColumn = (TableColumn<RedisKey, Integer>) keyTableView.getColumns().get(0);
        keyNoColumn.setCellFactory(tableViewRowIndexColumnCellFactory);
        TableColumn<RedisKey, byte[]> keyColumn = (TableColumn<RedisKey, byte[]>) keyTableView.getColumns().get(1);
        keyColumn.setCellFactory(tableViewByteColumnCellFactory);
        keyColumn.setCellValueFactory(param -> param.getValue().keyProperty());
        TableColumn<RedisKey, String> typeColumn = (TableColumn<RedisKey, String>) keyTableView.getColumns().get(2);
        typeColumn.setCellValueFactory(param -> param.getValue().typeProperty());
        TableColumn<RedisKey, Number> sizeColumn = (TableColumn<RedisKey, Number>) keyTableView.getColumns().get(3);
        sizeColumn.setCellValueFactory(param -> param.getValue().sizeProperty());
        TableColumn<RedisKey, Number> ttlColumn = (TableColumn<RedisKey, Number>) keyTableView.getColumns().get(4);
        ttlColumn.setCellValueFactory(param -> param.getValue().ttlProperty());

        IRedisKeyActionEventHandler handler = new RedisKeyActionEventHandlerImpl(this);
        keyTableView.setRowFactory(new RedisKeyTableRowFactory(handler));

        TableColumn<RedisValue, Integer> valueNoColumn = (TableColumn<RedisValue, Integer>) valueTableView.getColumns().get(0);
        valueNoColumn.setCellFactory(tableViewRowIndexColumnCellFactory);
        TableColumn<RedisValue, byte[]> valueKeyColumn = (TableColumn<RedisValue, byte[]>) valueTableView.getColumns().get(1);
        valueKeyColumn.setCellFactory(tableViewByteColumnCellFactory);
        valueKeyColumn.setCellValueFactory(param -> param.getValue().keyProperty());
        TableColumn<RedisValue, byte[]> valueColumn = (TableColumn<RedisValue, byte[]>) valueTableView.getColumns().get(2);
        valueColumn.setCellFactory(tableViewByteColumnCellFactory);
        valueColumn.setCellValueFactory(param -> param.getValue().valueProperty());
        TableColumn<RedisValue, Number> valueScoreColumn = (TableColumn<RedisValue, Number>) valueTableView.getColumns().get(3);
        valueScoreColumn.setCellValueFactory(param -> param.getValue().scoreProperty());

        IRedisValueActionEventHandler valueActionEventHandler = new RedisValueActionEventHandlerImpl(this);
        valueTableView.setRowFactory(new RedisValueTableRowFactory(valueActionEventHandler));

        // 保存按钮点击事件
        saveButton.setOnAction(event -> saveKeyValue());

        // Key 表格选择变化时更新 keyTextFiled
        keyTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                currentKey = newVal;
                keyTextFiled.setText(new String(newVal.getKey()));
            }
        });

    }

    /**
     * 保存 Key 的 Value 和 TTL
     */
    private void saveKeyValue() {
        if (currentKey == null) {
            DialogUtil.warning("警告", "请先选择一个 Key", null);
            return;
        }

        String newValue = valueTextArea.getText();

        TaskManger.execute(new SaveKeyTask(currentKey, newValue));
    }

    /**
     * 保存 Key 任务
     */
    private class SaveKeyTask extends BaseTask<Boolean> {

        private final RedisKey redisKey;
        private final String newValue;

        public SaveKeyTask(RedisKey redisKey, String newValue) {
            this.redisKey = redisKey;
            this.newValue = newValue;
        }

        @Override
        protected void scheduled() {
            updateMessage("正在保存...");
        }

        @Override
        protected Boolean call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                commands.select(database);

                byte[] keyBytes = redisKey.getKey();
                String type = redisKey.getType();

                switch (type) {
                    case RedisConstant.KeyType.STRING -> commands.set(keyBytes, newValue.getBytes(RedisConstant.CHARSET_UTF8));
                    case RedisConstant.KeyType.LIST -> {
                        commands.del(keyBytes);
                        commands.rpush(keyBytes, newValue.getBytes(RedisConstant.CHARSET_UTF8));
                    }
                    case RedisConstant.KeyType.SET -> {
                        commands.del(keyBytes);
                        commands.sadd(keyBytes, newValue.getBytes(RedisConstant.CHARSET_UTF8));
                    }
                    default -> throw new RuntimeException("当前类型不支持直接编辑: " + type);
                }

                return true;
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            if (getValue()) {
                DialogUtil.information("成功", "保存成功！", null);
            }
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            if (ex != null) {
                DialogUtil.error("保存失败", ex);
            }
        }
    }


    public void updateKeyTableView(List<RedisKey> list) {
        this.keyTableView.getItems().clear();
        if (list != null && !list.isEmpty()) {
            this.keyTableView.getItems().addAll(list);
        }
    }

    public void updateValueTableView(List<RedisValue> list) {
        this.valueTableView.getItems().clear();
        if (list != null && !list.isEmpty()) {
            this.valueTableView.getItems().addAll(list);
        }
    }

    public void selected() {
        objectTabController.selectDatabaseTab(databaseTab);
    }

    /**
     * 获取数据库 Tab
     */
    public Tab getDatabaseTab() {
        return databaseTab;
    }
}
