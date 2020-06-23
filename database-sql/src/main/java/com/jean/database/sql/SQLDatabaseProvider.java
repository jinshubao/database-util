package com.jean.database.sql;

import com.jean.database.api.AbstractDatabaseProvider;
import com.jean.database.api.ViewContext;
import com.jean.database.sql.view.treeitem.ServerTreeItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

public abstract class SQLDatabaseProvider extends AbstractDatabaseProvider {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void init(ViewContext context) {
        super.init(context);
        MenuBar menuBar = context.getMenuBar();
        Menu fileMenu = menuBar.getMenus().get(0);
        Menu menu = (Menu) fileMenu.getItems().get(0);
        MenuItem menuItem = new MenuItem(getName());
        menu.getItems().add(menuItem);
        menuItem.setOnAction(event -> {
            SQLConnectionConfiguration configuration = this.getConnectionConfiguration();
            if (configuration != null) {
                SQLMetadataProvider metadataProvider = this.getMetadataProvider();
                TreeItem treeItem = new ServerTreeItem(configuration, metadataProvider, context);
                context.getDatabaseTreeView().getRoot().getChildren().add(treeItem);
            }
        });
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
