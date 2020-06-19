package com.jean.database.sql;

import com.jean.database.api.AbstractDatabaseProvider;
import com.jean.database.api.view.ViewContext;
import com.jean.database.sql.view.treeitem.DatabaseTreeItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public abstract class SQLDatabaseProvider extends AbstractDatabaseProvider {

    @Override
    public void init(ViewContext context) {
        MenuBar menuBar = context.getMenuBar();
        Menu fileMenu = menuBar.getMenus().get(0);
        Menu menu = (Menu) fileMenu.getItems().get(0);
        MenuItem menuItem = new MenuItem(getName());
        menuItem.setOnAction(event -> {
            SQLConnectionConfiguration configuration = this.getConnectionConfiguration();
            if (configuration != null) {
                SQLMetadataProvider metadataProvider = this.getMetadataProvider();
                DatabaseTreeItem treeItem = new DatabaseTreeItem(configuration.toString(), context, configuration, metadataProvider);
                context.getDatabaseTreeView().getRoot().getChildren().add(treeItem);
            }
        });
        menu.getItems().add(menuItem);
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
