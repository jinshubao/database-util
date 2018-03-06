package com.jean.database.client.view.treeitem;

import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;

/**
 * 分组
 *
 * @author jinshubao
 */
public class GroupItem extends AbstractTreeItem {

    public GroupItem(IConnectionConfiguration connectionConfiguration,
                     CatalogMetaData catalogMetaData, SchemaMetaData schemaMetaData, String groupName) {
        super(connectionConfiguration, catalogMetaData, schemaMetaData, null, groupName);
    }


    @Override
    public void refreshData() {

    }
}
