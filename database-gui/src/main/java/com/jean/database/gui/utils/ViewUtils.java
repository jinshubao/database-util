package com.jean.database.gui.utils;

import com.jean.database.core.meta.KeyValuePairData;
import com.jean.database.core.meta.TableSummaries;
import com.jean.database.gui.controller.MainController;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class ViewUtils {

    private MainController mainController;

    private static ViewUtils instance = null;

    private ViewUtils(MainController mainController) {
        this.mainController = mainController;
    }

    public static void init(MainController mainController) {
        if (instance == null) {
            instance = new ViewUtils(mainController);
        } else {
            throw new RuntimeException("不能重复初始化");
        }
    }

    public static ViewUtils getInstance() {
        return instance;
    }

    public TreeView getDatabaseTreeView() {
        return mainController.getDatabaseTreeView();
    }

    public Tab getObjectTab() {
        return mainController.getObjectTab();
    }

    public TableView<TableSummaries> getObjectTableView() {
        return mainController.getObjectTableView();
    }

    public Tab getGeneralInfoTab() {
        return mainController.getGeneralInfoTab();
    }

    public TableView<KeyValuePairData> getGeneralInfoTableView() {
        return mainController.getGeneralInfoTableView();
    }

    public Tab getDdlInfoTab() {
        return mainController.getDdlInfoTab();
    }

    public TextArea getDdlInfoTextArea() {
        return mainController.getDdlInfoTextArea();
    }

    public TabPane getObjectTabPan() {
        return mainController.getObjectTabPan();
    }

    public TabPane getInfoTabPane() {
        return mainController.getInfoTabPane();
    }

    public BorderPane getRoot() {
        return mainController.getRoot();
    }

    public MenuBar getMenuBar() {
        return mainController.getMenuBar();
    }

    public Menu getFileMenu() {
        return mainController.getFileMenu();
    }

    public Menu getNewConnectionMenu() {
        return mainController.getNewConnectionMenu();
    }

    public MenuItem getClose() {
        return mainController.getCloseMenuItem();
    }

    public MenuItem getExitMenuItem() {
        return mainController.getExitMenuItem();
    }

    public MenuButton getConnectionMenuButton() {
        return mainController.getConnectionMenuButton();
    }
}
