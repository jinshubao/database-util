package com.jean.database.mysql;

import com.jean.database.api.IObjectTabController;
import com.jean.database.api.KeyValuePair;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.sql.meta.TableSummaries;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MySQLObjectTabController implements Initializable, IObjectTabController {

    private static final Logger logger = LoggerFactory.getLogger(MySQLObjectTabController.class);

    public Pane root;
    public SplitPane splitPane;
    public TabPane sqlObjectTabPan;
    public TableView<TableSummaries> sqlObjectTableView;
    public TableView<KeyValuePair<String, Object>> generalInfoTableView;
    public TextArea ddlInfoTextArea;
    private Tab objectTab;

    private String title;

    public MySQLObjectTabController() {
    }

    public MySQLObjectTabController(String title) {
        this.title = title;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize sql object pan");
        this.objectTab = new Tab(title);
        this.objectTab.setGraphic(ImageUtils.createImageView("/mysql/mysql.png"));
        this.objectTab.setContent(root);
        this.objectTab.setOnCloseRequest(event -> {
        });
        this.objectTab.setOnClosed(event -> {

        });

        TableColumn<TableSummaries, String> tableName = (TableColumn<TableSummaries, String>) sqlObjectTableView.getColumns().get(0);
        tableName.setCellValueFactory(param -> param.getValue().tableNameProperty());
        TableColumn<TableSummaries, String> autoIncrement = (TableColumn<TableSummaries, String>) sqlObjectTableView.getColumns().get(1);
        autoIncrement.setCellValueFactory(param -> param.getValue().autoIncrementProperty());
        TableColumn<TableSummaries, String> modifyTime = (TableColumn<TableSummaries, String>) sqlObjectTableView.getColumns().get(2);
        modifyTime.setCellValueFactory(param -> param.getValue().modifyTimeProperty());
        TableColumn<TableSummaries, String> dataLength = (TableColumn<TableSummaries, String>) sqlObjectTableView.getColumns().get(3);
        dataLength.setCellValueFactory(param -> param.getValue().dataLengthProperty());
        TableColumn<TableSummaries, String> tableType = (TableColumn<TableSummaries, String>) sqlObjectTableView.getColumns().get(4);
        tableType.setCellValueFactory(param -> param.getValue().tableTypeProperty());
        TableColumn<TableSummaries, String> tableRows = (TableColumn<TableSummaries, String>) sqlObjectTableView.getColumns().get(5);
        tableRows.setCellValueFactory(param -> param.getValue().tableRowsProperty());
        TableColumn<TableSummaries, String> comments = (TableColumn<TableSummaries, String>) sqlObjectTableView.getColumns().get(6);
        comments.setCellValueFactory(param -> param.getValue().commentsProperty());

        TableColumn<KeyValuePair<String, Object>, String> keyColumn = (TableColumn<KeyValuePair<String, Object>, String>) generalInfoTableView.getColumns().get(0);
        keyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));
        TableColumn<KeyValuePair<String, Object>, Object> valueColumn = (TableColumn<KeyValuePair<String, Object>, Object>) generalInfoTableView.getColumns().get(1);
        valueColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue()));
    }

    public void setObjectValue(List<TableSummaries> value) {
        sqlObjectTableView.getItems().clear();
        if (value != null) {
            sqlObjectTableView.getItems().addAll(value);
        }
    }

    public void setGeneralInfoValue(List<KeyValuePair<String, Object>> value) {
        generalInfoTableView.getItems().clear();
        if (value != null) {
            generalInfoTableView.getItems().addAll(value);
        }
    }

    public void setDdlInfo(String value) {
        ddlInfoTextArea.setText(value);
    }

    public void clear() {
        sqlObjectTableView.getItems().clear();
        generalInfoTableView.getItems().clear();
        ddlInfoTextArea.setText(null);
    }

    public void addObjectTab(Tab tab) {
        sqlObjectTabPan.getTabs().add(tab);
    }

    public void selectObjectTab(Tab tab) {
        sqlObjectTabPan.getSelectionModel().select(tab);
    }

    @Override
    public Tab getObjectTab() {
        return this.objectTab;
    }

    @Override
    public void select() {
        TabPane tabPane = objectTab.getTabPane();
        if (tabPane != null) {
            tabPane.getSelectionModel().select(objectTab);
        }
    }
}
