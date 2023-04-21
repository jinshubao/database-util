package com.jean.database.mysql.handler;

import com.jean.database.context.ApplicationContext;
import com.jean.database.handler.AbstractActionHandler;
import com.jean.database.mysql.controller.MySQLQueryTabController;
import com.jean.database.mysql.task.TableTypeTask;
import com.jean.database.mysql.view.item.MySQLTableTreeItem;
import com.jean.database.mysql.view.item.MySQLTableTypeTreeItem;
import com.jean.database.sql.SQLMetadataFactory;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.meta.TableTypeMetaData;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.view.AbstractTreeItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultMySQLCatalogTreeItemActionEventHandler extends AbstractActionHandler implements MySQLCatalogTreeItemActionEventHandler {

    private final AbstractTreeItem<CatalogMetaData> treeItem;

    private final SQLMetadataFactory metadataProvider;

    private BooleanProperty taskRunning = new SimpleBooleanProperty(false, "taskRunning");

    BooleanProperty openMenuDisable = new SimpleBooleanProperty(false, "openMenuDisable");
    BooleanProperty queryMenuDisable = new SimpleBooleanProperty(false, "queryMenuDisable");
    BooleanProperty closeMenuDisable = new SimpleBooleanProperty(false, "closeMenuDisable");
    BooleanProperty createMenuDisable = new SimpleBooleanProperty(false, "createMenuDisable");
    BooleanProperty deleteMenuDisable = new SimpleBooleanProperty(false, "deleteMenuDisable");
    BooleanProperty propertiesMenuDisable = new SimpleBooleanProperty(false, "propertiesMenuDisable");
    BooleanProperty commandLineMenuDisable = new SimpleBooleanProperty(false, "commandLineMenuDisable");
    BooleanProperty executeSqlFileDisable = new SimpleBooleanProperty(false, "executeSqlFileDisable");
    BooleanProperty exportStructAndDataMenuDisable = new SimpleBooleanProperty(false, "exportStructAndDataMenuDisable");
    BooleanProperty exportStructMenuDisable = new SimpleBooleanProperty(false, "exportStructMenuDisable");
    BooleanProperty printDatabaseMenuDisable = new SimpleBooleanProperty(false, "printDatabaseMenuDisable");
    BooleanProperty dataTransformMenuDisable = new SimpleBooleanProperty(false, "dataTransformMenuDisable");
    BooleanProperty convertDatabaseToModeMenuDisable = new SimpleBooleanProperty(false, "convertDatabaseToModeMenuDisable");
    BooleanProperty findInDatabaseMenuDisable = new SimpleBooleanProperty(false, "findInDatabaseMenuDisable");
    BooleanProperty refreshMenuDisable = new SimpleBooleanProperty(false, "refreshMenuDisable");


    public DefaultMySQLCatalogTreeItemActionEventHandler(ApplicationContext context, AbstractTreeItem<CatalogMetaData> treeItem, SQLMetadataFactory metadataProvider) {
        super(context);
        this.treeItem = treeItem;
        this.metadataProvider = metadataProvider;

        initProperties();
    }


    private void initProperties() {
        openMenuDisable.bind(taskRunning);
        queryMenuDisable.bind(taskRunning);
        createMenuDisable.bind(taskRunning);
        deleteMenuDisable.bind(taskRunning);
        propertiesMenuDisable.bind(taskRunning);
        commandLineMenuDisable.bind(taskRunning);
        executeSqlFileDisable.bind(taskRunning);
        exportStructAndDataMenuDisable.bind(taskRunning);
        exportStructMenuDisable.bind(taskRunning);
        printDatabaseMenuDisable.bind(taskRunning);
        dataTransformMenuDisable.bind(taskRunning);
        convertDatabaseToModeMenuDisable.bind(taskRunning);
        findInDatabaseMenuDisable.bind(taskRunning);
        refreshMenuDisable.bind(taskRunning);
    }



    public AbstractTreeItem<CatalogMetaData> getTreeItem() {
        return treeItem;
    }

    @Override
    public void refresh() {
        close();
        open();
    }

    @Override
    public void click() {

    }

    @Override
    public void doubleClick() {
        open();
    }

    private void executeTableTypeTask() {
        TableTypeTask task = new TableTypeTask(metadataProvider, treeItem.getValue());
        task.setOnSucceeded(event -> {
            List<TableTypeMetaData> metaData = task.getValue();
            AbstractTreeItem<CatalogMetaData> item = getTreeItem();
            item.getChildren().clear();
            for (TableTypeMetaData typeMetaData : metaData) {
                TreeItem typeItem = new MySQLTableTypeTreeItem(getContext(), typeMetaData, metadataProvider);
                typeItem.setExpanded(true);
                for (TableMetaData tableMetaData : typeMetaData.getTableMetaDataList()) {
                    MySQLTableTreeItem tableTreeItem = new MySQLTableTreeItem(tableMetaData);
                    MySQLTableTreeItemActionEventHandler handler = new DefaultMySQLTableTreeItemActionEventHandler(getContext(),tableTreeItem, metadataProvider);
                    tableTreeItem.setItemActionEventHandler(handler);
                    typeItem.getChildren().add(tableTreeItem);
                }
                item.getChildren().add(typeItem);
            }
            item.setExpanded(true);
        });
        if (taskRunning.isBound()) {
            taskRunning.unbind();
        }
        taskRunning.bind(task.runningProperty());
        getContext().execute(task);
    }

    @Override
    public void select() {

    }

    @Override
    public void newQuery() {

        CatalogMetaData value = getTreeItem().getValue();
        List<String> list = getTreeItem().getParent().getChildren().stream().map(item -> item.getValue().getTableCat()).collect(Collectors.toList());
        try {
            FxmlUtils.LoadFxmlResult loadFxmlResult =
                    FxmlUtils.loadFxml("fxml/mysql-query-tab.fxml", null, new MySQLQueryTabController(getContext(), list, value.getTableCat()));
            Tab tab = new Tab("查询", loadFxmlResult.getParent());
            getContext().addObjectTab(tab);
        } catch (IOException e) {
            DialogUtil.error(e);
        }
    }

    @Override
    public void open() {
        executeTableTypeTask();
    }

    @Override
    public void close() {

    }

    @Override
    public void newDatabase() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void properties() {

    }

    @Override
    public void commandLine() {

    }

    @Override
    public void executeSqlFile() {

    }

    @Override
    public void exportStructAndData() {

    }

    @Override
    public void exportStruct() {

    }

    @Override
    public void printDatabase() {

    }

    @Override
    public void dataTransform() {

    }

    @Override
    public void convertDatabaseToMode() {

    }

    @Override
    public void findInDatabase() {

    }

    @Override
    public BooleanProperty openMenuDisableProperty() {
        return openMenuDisable;
    }

    @Override
    public BooleanProperty queryMenuDisableProperty() {
        return queryMenuDisable;
    }

    @Override
    public BooleanProperty createMenuDisableProperty() {
        return createMenuDisable;
    }

    @Override
    public BooleanProperty deleteMenuDisableProperty() {
        return deleteMenuDisable;
    }

    @Override
    public BooleanProperty propertiesMenuDisableProperty() {
        return propertiesMenuDisable;
    }

    @Override
    public BooleanProperty commandLineMenuDisableProperty() {
        return commandLineMenuDisable;
    }

    @Override
    public BooleanProperty executeSqlFileDisableProperty() {
        return executeSqlFileDisable;
    }

    @Override
    public BooleanProperty exportStructAndDataMenuDisableProperty() {
        return exportStructAndDataMenuDisable;
    }

    @Override
    public BooleanProperty exportStructMenuDisableProperty() {
        return exportStructMenuDisable;
    }

    @Override
    public BooleanProperty printDatabaseMenuDisableProperty() {
        return printDatabaseMenuDisable;
    }

    @Override
    public BooleanProperty dataTransformMenuDisableProperty() {
        return dataTransformMenuDisable;
    }

    @Override
    public BooleanProperty convertDatabaseToModeMenuDisableProperty() {
        return convertDatabaseToModeMenuDisable;
    }

    @Override
    public BooleanProperty findInDatabaseMenuDisableProperty() {
        return findInDatabaseMenuDisable;
    }

    @Override
    public BooleanProperty refreshMenuDisableProperty() {
        return refreshMenuDisable;
    }

}
