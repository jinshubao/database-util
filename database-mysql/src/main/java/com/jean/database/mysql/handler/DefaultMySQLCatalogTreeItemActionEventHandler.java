package com.jean.database.mysql.handler;

import com.jean.database.context.ApplicationContext;
import com.jean.database.handler.AbstractActionHandler;
import com.jean.database.mysql.controller.MySQLQueryTabController;
import com.jean.database.mysql.task.TableTypeTask;
import com.jean.database.mysql.view.item.MySQLTableTreeItem;
import com.jean.database.mysql.view.item.MySQLTableTypeTreeItem;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.meta.TableTypeMetaData;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.view.AbstractTreeItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultMySQLCatalogTreeItemActionEventHandler extends AbstractActionHandler implements MySQLCatalogTreeItemActionEventHandler {

    private final AbstractTreeItem<CatalogMetaData> treeItem;

    private final SQLMetadataProvider metadataProvider;

    public DefaultMySQLCatalogTreeItemActionEventHandler(ApplicationContext context, AbstractTreeItem<CatalogMetaData> treeItem, SQLMetadataProvider metadataProvider) {
        super(context);
        this.treeItem = treeItem;
        this.metadataProvider = metadataProvider;
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
}
