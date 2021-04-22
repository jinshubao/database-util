package com.jean.database.mysql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.jean.database.api.DefaultController;
import com.jean.database.api.ViewContext;
import com.jean.database.api.utils.DialogUtil;
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

public class MySQLQueryTabController extends DefaultController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MySQLQueryTabController.class);

    public Pane root;
    public ComboBox<String> catalogComboBox;
    public Button explainButton;
    public Button formatButton;
    public Button saveButton;
    public Button executeButton;
    public TextArea sqlTextArea;
    public SplitPane splitPane;
    public TabPane resultTabPane;
    public Tab executeInfoTab;
    public TextArea executeInfoTextArea;
    public Tab analyseTab;
    public TableView<ExecuteAnalyse> analyseTableView;
    public TableColumn<ExecuteAnalyse, String> analyseStatusColumn;
    public TableColumn<ExecuteAnalyse, String> analyseDurationColumn;
    public TableColumn<ExecuteAnalyse, String> analysePercentageColumn;
    public Tab statusTab;
    public TableView<ExecuteStatus> statusTableView;
    public TableColumn<ExecuteStatus, String> statusVariableNameColumn;
    public TableColumn<ExecuteStatus, String> statusValueColumn;
    public TableColumn<ExecuteStatus, String> statusDescriptionColumn;

    List<String> catalogs;
    String catalog;


    public MySQLQueryTabController(ViewContext viewContext, List<String> catalogs, String catalog) {
        super(viewContext);
        this.catalogs = catalogs;
        this.catalog = catalog;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize mysql query tab");

        this.initializeFunctionBar();

        this.initializeSqlTextArea();

        this.initializeExecuteResult();

    }


    private void initializeFunctionBar() {
        catalogComboBox.getItems().addAll(this.catalogs);
        catalogComboBox.getSelectionModel().select(catalog);

        formatButton.setOnAction(event -> {
            String sql = sqlTextArea.getText();
            try {
                String result = SQLUtils.format(sql, JdbcConstants.MYSQL, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
                sqlTextArea.setText(result);
            } catch (Throwable e) {
                DialogUtil.error(e);
            }
        });
    }


    private void initializeSqlTextArea() {

    }


    private void initializeExecuteResult() {

        executeInfoTextArea.setText(null);

        analyseStatusColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus()));
        analyseDurationColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDuration()));
        analysePercentageColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPercentage()));

        statusVariableNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getVariableName()));
        statusValueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));
        statusDescriptionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDescription()));

    }

    public Pane getRoot() {
        return root;
    }
}
