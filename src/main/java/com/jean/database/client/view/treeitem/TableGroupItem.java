package com.jean.database.client.view.treeitem;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.core.provider.IMetadataProvider;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jinshubao
 */
public class TableGroupItem extends GroupItem {

    public TableGroupItem(IConnectionConfiguration connectionConfiguration,
                          CatalogMetaData catalogMetaData,
                          SchemaMetaData schemaMetaData,
                          String groupName) {
        super(connectionConfiguration, catalogMetaData, schemaMetaData, groupName);
    }

    @Override
    public void refreshData() {
        try {
            ObservableList children = getChildren();
            children.clear();
            IMetadataProvider provider = getMetadataProvider();
            IConnectionConfiguration configuration = getConnectionConfiguration();
            List<TableMetaData> catalogs = provider.getTables(configuration,
                    catalogMetaData.getTableCat(),
                    schemaMetaData != null ? schemaMetaData.getTableSchem() : null);
            for (TableMetaData metaData : catalogs) {
                TableTreeItem item = new TableTreeItem(configuration, catalogMetaData, schemaMetaData, metaData);
                //noinspection unchecked
                children.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem create = new MenuItem("新建表");
        MenuItem properties = new MenuItem("属性");
        contextMenu.getItems().addAll(create, properties);
        return contextMenu;
    }

    @Override
    public void onSelected(AbstractTreeItem treeItem) {
        super.onSelected(treeItem);
        List<TableMetaData> metaDataList = new ArrayList<>();
        for (TreeItem<Object> item : getChildren()) {
            TableMetaData value = (TableMetaData)item.getValue();
            metaDataList.add(value);
        }
        getController().showTableGroups(metaDataList);
    }
}
