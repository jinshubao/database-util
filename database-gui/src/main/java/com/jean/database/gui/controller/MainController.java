package com.jean.database.gui.controller;

import com.jean.database.api.IDatabaseProvider;
import com.jean.database.api.TreeCellFactory;
import com.jean.database.api.ViewContext;
import com.jean.database.api.ViewManger;
import com.jean.database.api.action.IMouseAction;
import com.jean.database.gui.ProviderManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MainController implements ViewContext, Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private BorderPane root;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem setting;
    @FXML
    private Menu file;
    @FXML
    private Menu connections;
    @FXML
    private MenuItem exist;
    @FXML
    private Menu view;
    @FXML
    private Menu collections;
    @FXML
    private Menu tools;
    @FXML
    private Menu window;
    @FXML
    private Menu help;
    @FXML
    private TreeView<?> databaseTreeView;
    @FXML
    private TabPane objectTabPan;
    @FXML
    private HBox messageBox;
    @FXML
    private ProgressBar progressBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("main controller initialize");
        this.initMenuBar();
        databaseTreeView.setCellFactory(TreeCellFactory.forTreeView());
        databaseTreeView.setRoot(new TreeItem<>());
        databaseTreeView.setShowRoot(false);
        databaseTreeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeItem<?> treeItem = databaseTreeView.getTreeItem(newValue.intValue());
                if (treeItem instanceof IMouseAction) {
                    ((IMouseAction) treeItem).select();
                }
            }
        });

        ViewManger.init(this);

        List<IDatabaseProvider> providers = ProviderManager.getProviders();
        providers.sort(Comparator.comparingInt(IDatabaseProvider::getOrder));
        for (IDatabaseProvider provider : providers) {
            provider.init();
        }
    }

    private void initMenuBar() {
        exist.setOnAction(event -> Platform.exit());
    }

    @Override
    public void addObjectTab(Tab tab) {
        objectTabPan.getTabs().add(tab);
    }

    @Override
    public void removeObjectTab(Tab tab) {
        objectTabPan.getTabs().remove(tab);
    }

    @Override
    public void addDatabaseItem(TreeItem treeItem) {
        databaseTreeView.getRoot().getChildren().add(treeItem);
    }

    @Override
    public void addConnectionMenus(MenuItem... menu) {
        connections.getItems().addAll(menu);
    }

    @Override
    public void addViewMenus(MenuItem... menu) {
        view.getItems().addAll(menu);
    }

    @Override
    public void updateProgress(double progress) {
        progressBar.setProgress(progress);
    }
}
