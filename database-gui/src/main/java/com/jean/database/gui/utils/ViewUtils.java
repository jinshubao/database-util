package com.jean.database.gui.utils;

import com.jean.database.core.meta.KeyValuePair;
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

    public TableView<KeyValuePair> getGeneralInfoTableView() {
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

}
