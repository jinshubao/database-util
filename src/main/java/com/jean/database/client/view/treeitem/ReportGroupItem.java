package com.jean.database.client.view.treeitem;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import javafx.scene.control.ContextMenu;

/**
 * @author jinshubao
 */
public class ReportGroupItem extends GroupItem {
    public ReportGroupItem(IConnectionConfiguration connectionConfiguration,
                           CatalogMetaData catalogMetaData, SchemaMetaData schemaMetaData, String groupName) {
        super(connectionConfiguration, catalogMetaData, schemaMetaData, groupName);
    }

    @Override
    public void refreshData() {

    }

    @Override
    public ContextMenu createContextMenu() {
        return null;
    }
}
