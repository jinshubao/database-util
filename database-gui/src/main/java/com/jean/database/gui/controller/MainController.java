package com.jean.database.gui.controller;

import com.jean.database.api.IDatabaseProvider;
import com.jean.database.api.TreeCellFactory;
import com.jean.database.api.ViewContext;
import com.jean.database.api.action.IMouseAction;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.gui.ProviderManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MainController implements Initializable {

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
    private Menu connection;
    @FXML
    private MenuItem exist;
    @FXML
    private Menu view;
    @FXML
    private Menu collection;
    @FXML
    private Menu tools;
    @FXML
    private Menu window;
    @FXML
    private Menu help;
    @FXML
    private TreeView<?> treeView;
    @FXML
    private TabPane objectTabPan;
    @FXML
    private ProgressBar progressBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("main controller initialize");
        this.initMenuBar();
        treeView.setCellFactory(TreeCellFactory.forTreeView());
        treeView.setRoot(new TreeItem<>());
        treeView.setShowRoot(false);
        treeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeItem<?> treeItem = treeView.getTreeItem(newValue.intValue());
                if (treeItem instanceof IMouseAction) {
                    ((IMouseAction) treeItem).select();
                }
            }
        });

        List<IDatabaseProvider> providers = ProviderManager.getProviders();
        providers.sort(Comparator.comparingInt(IDatabaseProvider::getOrder));
        for (IDatabaseProvider provider : providers) {
            provider.setViewContext(new DefaultViewContext());
            provider.init();
        }
    }

    private void initMenuBar() {
        setting.setGraphic(ImageUtils.createImageView("/image/settings.png"));
        exist.setGraphic(ImageUtils.createImageView("/image/exit.png"));
        exist.setOnAction(event -> Platform.exit());
    }


    private class DefaultViewContext implements ViewContext {

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
            treeView.getRoot().getChildren().add(treeItem);
        }

        @Override
        public void addFileMenus(MenuItem... menu) {
            file.getItems().addAll(menu);
        }

        @Override
        public void addConnectionMenus(MenuItem... menu) {
            connection.getItems().addAll(menu);
        }

        @Override
        public void addViewMenus(MenuItem... menu) {
            view.getItems().addAll(menu);
        }

        @Override
        public void addCollectionMenus(MenuItem... menu) {
            collection.getItems().addAll(menu);
        }

        @Override
        public void addToolsMenus(MenuItem... menu) {
            tools.getItems().addAll(menu);
        }

        @Override
        public void addWindowMenus(MenuItem... menu) {
            window.getItems().addAll(menu);
        }


        @Override
        public void addHelpMenus(MenuItem... menu) {
            help.getItems().addAll(menu);
        }

        @Override
        public void updateProgress(double progress) {
            if (Platform.isFxApplicationThread()) {
                progressBar.setProgress(progress);
            } else {
                Platform.runLater(() -> progressBar.setProgress(progress));
            }
        }


        public BorderPane getRoot() {
            return root;
        }

        public MenuBar getMenuBar() {
            return menuBar;
        }

        public TreeView<?> getTreeView() {
            return treeView;
        }

        public TabPane getObjectTabPan() {
            return objectTabPan;
        }
    }
}

