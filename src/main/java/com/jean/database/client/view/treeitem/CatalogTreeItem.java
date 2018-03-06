package com.jean.database.client.view.treeitem;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.meta.CatalogMetaData;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * @author jinshubao
 */
public class CatalogTreeItem extends AbstractTreeItem {

    public CatalogTreeItem(IConnectionConfiguration connectionConfiguration, CatalogMetaData catalogMetaData) {
        super(connectionConfiguration, catalogMetaData, null, null, catalogMetaData);
    }

    @Override
    public void refreshData() {
        ObservableList children = getChildren();
        children.clear();

        IConnectionConfiguration configuration = getConnectionConfiguration();

        TableGroupItem groupItem = new TableGroupItem(configuration, catalogMetaData, null, "表");
        groupItem.refreshData();

        ViewGroupItem viewItem = new ViewGroupItem(configuration, catalogMetaData, null, "视图");
        viewItem.refreshData();

        FunctionGroupItem functionItem = new FunctionGroupItem(configuration, catalogMetaData, null, "函数");
        functionItem.refreshData();

        EventGroupItem eventItem = new EventGroupItem(configuration, catalogMetaData, null, "事件");
        eventItem.refreshData();

        QueryGroupItem queryItem = new QueryGroupItem(configuration, catalogMetaData, null, "查询");
        queryItem.refreshData();

        ReportGroupItem reportItem = new ReportGroupItem(configuration, catalogMetaData, null, "报表");
        reportItem.refreshData();

        BackupGroupItem backupItem = new BackupGroupItem(configuration, catalogMetaData, null, "备份");
        backupItem.refreshData();

        //noinspection unchecked
        children.add(groupItem);
        //noinspection unchecked
        children.add(viewItem);
        //noinspection unchecked
        children.add(functionItem);
        //noinspection unchecked
        children.add(eventItem);
        //noinspection unchecked
        children.add(queryItem);
        //noinspection unchecked
        children.add(reportItem);
        //noinspection unchecked
        children.add(backupItem);
        setExpanded(true);
    }

    @Override
    public ContextMenu createContextMenu() {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(expandedProperty());
        open.setOnAction(event -> refreshData());

        MenuItem close = new MenuItem("关闭数据库");
        close.disableProperty().bind(expandedProperty().not());
        close.setOnAction(event -> {
            getChildren().clear();
            setExpanded(false);
        });

        MenuItem create = new MenuItem("新建数据库...");
        MenuItem delete = new MenuItem("删除数据库");
        MenuItem properties = new MenuItem("数据库属性...");

        contextMenu.getItems().addAll(open, close, create, delete, properties);
        return contextMenu;
    }

}
