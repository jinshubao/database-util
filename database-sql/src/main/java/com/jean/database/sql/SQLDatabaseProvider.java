package com.jean.database.sql;

import com.jean.database.api.AbstractDatabaseProvider;
import com.jean.database.api.ViewManger;
import com.jean.database.sql.view.treeitem.ServerTreeItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

public abstract class SQLDatabaseProvider extends AbstractDatabaseProvider {

    @Override
    public void init() {
        super.init();
        MenuItem menuItem = new MenuItem(getName());
        menuItem.setOnAction(event -> {
            SQLConnectionConfiguration configuration = getConnectionConfiguration();
            if (configuration != null) {
                SQLMetadataProvider metadataProvider = getMetadataProvider();
                TreeItem treeItem = new ServerTreeItem(configuration, metadataProvider);
                ViewManger.getViewContext().addDatabaseItem(treeItem);
            }
        });
        ViewManger.getViewContext().addConnectionMenus(menuItem);
    }

    public String getCatalogIcon() {
        return null;
    }

    public String getSchemaIcon() {
        return null;
    }

    public String getTableIcon() {
        return null;
    }

    public abstract SQLMetadataProvider getMetadataProvider();

    public abstract SQLConnectionConfiguration getConnectionConfiguration();

    public abstract boolean supportCatalog();

    public abstract boolean supportSchema();

}
