package com.jean.database.sql;

import com.jean.database.api.KeyValuePair;
import com.jean.database.api.ViewContext;
import com.jean.database.sql.meta.TableSummaries;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SQLObjectTabController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(SQLObjectTabController.class);

    public SplitPane root;
    public TabPane objectTabPan;
    public TableView<TableSummaries> objectTableView;
    public TableView<KeyValuePair<String, Object>> generalInfoTableView;
    public TextArea ddlInfoTextArea;
    private ViewContext viewContext;
    private Tab tab;

    public SQLObjectTabController() {
    }

    public SQLObjectTabController(String title, ViewContext viewContext) {
        this.viewContext = viewContext;
        this.tab = new Tab(title);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize sql object pan");
        this.tab.setContent(root);
        this.tab.setOnCloseRequest(event -> {
        });
        viewContext.getObjectTabPan().getTabs().add(tab);
        selected();

        TableColumn<TableSummaries, String> tableName = (TableColumn<TableSummaries, String>) objectTableView.getColumns().get(0);
        tableName.setCellValueFactory(param -> param.getValue().tableNameProperty());
        TableColumn<TableSummaries, String> autoIncrement = (TableColumn<TableSummaries, String>) objectTableView.getColumns().get(1);
        autoIncrement.setCellValueFactory(param -> param.getValue().autoIncrementProperty());
        TableColumn<TableSummaries, String> modifyTime = (TableColumn<TableSummaries, String>) objectTableView.getColumns().get(2);
        modifyTime.setCellValueFactory(param -> param.getValue().modifyTimeProperty());
        TableColumn<TableSummaries, String> dataLength = (TableColumn<TableSummaries, String>) objectTableView.getColumns().get(3);
        dataLength.setCellValueFactory(param -> param.getValue().dataLengthProperty());
        TableColumn<TableSummaries, String> tableType = (TableColumn<TableSummaries, String>) objectTableView.getColumns().get(4);
        tableType.setCellValueFactory(param -> param.getValue().tableTypeProperty());
        TableColumn<TableSummaries, String> tableRows = (TableColumn<TableSummaries, String>) objectTableView.getColumns().get(5);
        tableRows.setCellValueFactory(param -> param.getValue().tableRowsProperty());
        TableColumn<TableSummaries, String> comments = (TableColumn<TableSummaries, String>) objectTableView.getColumns().get(6);
        comments.setCellValueFactory(param -> param.getValue().commentsProperty());

        TableColumn<KeyValuePair<String, Object>, String> keyColumn = (TableColumn<KeyValuePair<String, Object>, String>) generalInfoTableView.getColumns().get(0);
        keyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));
        TableColumn<KeyValuePair<String, Object>, Object> valueColumn = (TableColumn<KeyValuePair<String, Object>, Object>) generalInfoTableView.getColumns().get(1);
        valueColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue()));
    }

    public void setObjectValue(List<TableSummaries> value) {
        objectTableView.getItems().setAll(value);
    }

    public void setGeneralInfoValue(List<KeyValuePair<String, Object>> value) {
        generalInfoTableView.getItems().clear();
        generalInfoTableView.getItems().addAll(value);
    }

    public void setDdlInfo(String value) {
        ddlInfoTextArea.setText(value);
    }

    public void close() {
        tab.getTabPane().getTabs().remove(tab);
    }

    public void selected() {
        tab.getTabPane().getSelectionModel().select(tab);
    }

    public static Callback<Class<?>, Object> getFactory(String title, ViewContext viewContext) {
        return param -> new SQLObjectTabController(title, viewContext);
    }

    public void addObjectTab(Tab tab) {
        objectTabPan.getTabs().add(tab);
    }

    public void selectObjectTab(Tab tab) {
        objectTabPan.getSelectionModel().select(tab);
    }
}
