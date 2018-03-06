package com.jean.database.client.view.treeitem;

import com.jean.database.client.view.ISelected;
import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import javafx.scene.control.ContextMenu;

import java.util.List;

/**
 * @author jinshubao
 */
public class ViewGroupItem extends GroupItem {
    public ViewGroupItem(IConnectionConfiguration connectionConfiguration,
                         CatalogMetaData catalogMetaData,
                         SchemaMetaData schemaMetaData,
                         String groupName) {
        super(connectionConfiguration, catalogMetaData, schemaMetaData, groupName);
    }

    @Override
    public void refreshData() {

    }

    @Override
    public ContextMenu createContextMenu() {
        return null;
    }

    @Override
    public void onSelected(ISelected treeItem) {
        super.onSelected(treeItem);
        getController().showViewGroups();
    }
}
