package com.jean.database.client.controller;

import com.jean.database.client.factory.TreeCellFactory;
import com.jean.database.client.manager.DatabaseTypeManager;
import com.jean.database.client.view.ISelecte;
import com.jean.database.core.IDatabaseTypeProvider;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author jinshubao
 */
public class MainController extends BaseController {
    @FXML
    public MenuBar menuBar;
    @FXML
    public Menu fileMenu;
    @FXML
    public Menu newConnectionMenu;
    @FXML
    public MenuItem newConnection;
    @FXML
    public ToggleButton tableButton;
    @FXML
    public TreeView treeView;
    @FXML
    public TabPane tablePane;
    @FXML
    public Tab objectTab;
    @FXML
    public FlowPane objectPane;
    @FXML
    public SplitPane splitPane;

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.initializeMenuBar();
        treeView.setCellFactory(TreeCellFactory.forTreeView());
        treeView.setShowRoot(false);
        treeView.setRoot(new TreeItem());
        treeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue instanceof ISelecte) {
                    ISelecte value = ((ISelecte) newValue);
                    value.onSelected(value);
                }
            }
        }));

        tableButton.setOnAction(event -> {
        });
    }

    private void initializeMenuBar() {
        List<IDatabaseTypeProvider> types = DatabaseTypeManager.getDatabaseTypes();
        ObservableList<MenuItem> items = newConnectionMenu.getItems();
        for (IDatabaseTypeProvider type : types) {
            MenuItem menuItem = new MenuItem(type.getName());
            menuItem.setOnAction(event -> {
                //TODO 点击菜单
            });
            items.add(menuItem);
        }
    }
}
