package com.jean.database.redis;

import com.jean.database.api.DefaultController;
import com.jean.database.context.ApplicationContext;
import com.jean.database.factory.TableViewRowIndexColumnCellFactory;
import com.jean.database.context.ViewContext;
import com.jean.database.redis.factory.RedisKeyTableRowFactory;
import com.jean.database.redis.factory.RedisValueTableRowFactory;
import com.jean.database.redis.factory.TableViewByteColumnCellFactory;
import com.jean.database.redis.model.RedisKey;
import com.jean.database.redis.model.RedisValue;
import com.jean.database.redis.view.handler.IRedisKeyActionEventHandler;
import com.jean.database.redis.view.handler.IRedisValueActionEventHandler;
import com.jean.database.redis.view.handler.impl.RedisKeyActionEventHandlerImpl;
import com.jean.database.redis.view.handler.impl.RedisValueActionEventHandlerImpl;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RedisDatabaseTabController extends DefaultController implements Initializable {

    public SplitPane root;
    public TableView<RedisKey> keyTableView;
    public SplitPane valueSplitPane;
    public TableView<RedisValue> valueTableView;
    public TextField keyTextFiled;
    public TextArea valueTextArea;
    public Button saveButton;
    private Tab databaseTab;

    RedisObjectTabController objectTabController;



    public RedisDatabaseTabController(ApplicationContext context, String title, RedisObjectTabController objectTabController) {
        super(context);
        this.databaseTab = new Tab(title);
        this.objectTabController = objectTabController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.databaseTab.setContent(root);
        this.databaseTab.setOnCloseRequest(event -> {

        });

        objectTabController.addDatabaseTab(databaseTab);

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

        IRedisKeyActionEventHandler handler = new RedisKeyActionEventHandlerImpl(getContext(),this);
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
}
