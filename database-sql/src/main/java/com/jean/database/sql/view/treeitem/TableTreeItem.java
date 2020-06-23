package com.jean.database.sql.view.treeitem;

import com.jean.database.api.BaseTask;
import com.jean.database.api.KeyValuePair;
import com.jean.database.api.TaskManger;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.SQLObjectTabController;
import com.jean.database.sql.constant.Images;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.view.SQLDataTableTab;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.util.List;

/**
 * @author jinshubao
 */
public class TableTreeItem extends BaseDatabaseItem<TableMetaData> {

    private final ContextMenu contextMenu;
    private final SQLObjectTabController objectTabController;

    private ObjectProperty<SQLDataTableTab> sqlDataTableTab;

    public TableTreeItem(TableMetaData tableMetaData,
                         SQLConnectionConfiguration connectionConfiguration,
                         SQLMetadataProvider metadataProvider,
                         SQLObjectTabController objectTabController) {
        super(tableMetaData, connectionConfiguration, metadataProvider);
        this.objectTabController = objectTabController;
        this.contextMenu = this.createContextMenu();
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.TABLE_IMAGE))));
        this.sqlDataTableTab = new SimpleObjectProperty<>(this, "sqlDataTableTab");
        openProperty().bind(this.sqlDataTableTab.isNotNull());
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public void click() {
    }

    @Override
    public void doubleClick() {
        open();
    }

    @Override
    public void select() {
        TaskManger.execute(new TableGeneralInfoTask());
    }

    private void open() {
        if (isOpen()) {
            return;
        }
        SQLDataTableTab dataTableTab = new SQLDataTableTab(getValue(), getConnectionConfiguration(), getMetadataProvider());
        this.sqlDataTableTab.set(dataTableTab);
        dataTableTab.setOnClosed(event -> {
            this.sqlDataTableTab.get().close();
            this.sqlDataTableTab.set(null);
        });
        objectTabController.addObjectTab(dataTableTab);
        objectTabController.selectObjectTab(dataTableTab);
        dataTableTab.refresh();
        TaskManger.execute(new TableGeneralInfoTask());
    }

    @Override
    public void close() {
    }

    @Override
    public void refresh() {
        SQLDataTableTab dataTableTab = this.sqlDataTableTab.get();
        if (dataTableTab != null) {
            dataTableTab.refresh();
        }
        TaskManger.execute(new TableGeneralInfoTask());
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开表");
        open.disableProperty().bind(openProperty());
        open.setOnAction(event -> open());

        MenuItem copy = new MenuItem("复制表");
        copy.setOnAction(event -> {
        });

        MenuItem delete = new MenuItem("删除表", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> {
        });

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.disableProperty().bind(openProperty().not());
        refresh.setOnAction(event -> refresh());

        contextMenu.getItems().addAll(open, copy, delete, refresh);
        return contextMenu;
    }


    private class TableGeneralInfoTask extends BaseTask<List<KeyValuePair<String, Object>>> {

        @Override
        protected List<KeyValuePair<String, Object>> call() throws Exception {
            TableMetaData tableMetaData = TableTreeItem.this.getValue();
            SQLConnectionConfiguration connectionConfiguration = getConnectionConfiguration();
            SQLMetadataProvider metadataProvider = getMetadataProvider();
            try (Connection connection = connectionConfiguration.getConnection()) {
                return metadataProvider.getTableDetails(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchema(), tableMetaData.getTableName(),
                        new String[]{tableMetaData.getTableType()});
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            objectTabController.setGeneralInfoValue(getValue());
            TableMetaData tableMetaData = TableTreeItem.this.getValue();
            objectTabController.setDdlInfo(tableMetaData.getTableName());

        }
    }

}
