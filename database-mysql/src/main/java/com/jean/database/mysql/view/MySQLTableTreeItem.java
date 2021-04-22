package com.jean.database.mysql.view;

import com.jean.database.api.BaseTask;
import com.jean.database.api.KeyValuePair;
import com.jean.database.api.TaskManger;
import com.jean.database.api.ViewContext;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.mysql.MySQLObjectTabController;
import com.jean.database.sql.BaseDatabaseItem;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.TableMetaData;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.List;

/**
 * @author jinshubao
 */
public class MySQLTableTreeItem extends BaseDatabaseItem<TableMetaData> {

    private final ContextMenu contextMenu;
    private final MySQLObjectTabController objectTabController;
    private MySQLDataTableTab sqlDataTableTab;

    public MySQLTableTreeItem(ViewContext viewContext, TableMetaData tableMetaData,
                              SQLMetadataProvider metadataProvider,
                              MySQLObjectTabController objectTabController) {
        super(viewContext, tableMetaData, metadataProvider);
        this.objectTabController = objectTabController;
        this.contextMenu = this.createContextMenu();
        this.setGraphic(ImageUtils.createImageView("/mysql/table.png"));
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
        setOpen(true);
        sqlDataTableTab = new MySQLDataTableTab(getValue(), getMetadataProvider());
        sqlDataTableTab.setOnClosed(event -> sqlDataTableTab.close());
        objectTabController.addObjectTab(sqlDataTableTab);
        objectTabController.selectObjectTab(sqlDataTableTab);
        refresh();
    }

    @Override
    public void close() {
        super.close();
        if (sqlDataTableTab != null) {
            sqlDataTableTab.close();
        }
    }

    @Override
    public void refresh() {
        sqlDataTableTab.refresh();
        TaskManger.execute(new TableGeneralInfoTask());
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem open = new MenuItem("打开表",ImageUtils.createImageView("/image/connect.png"));
        open.disableProperty().bind(openProperty());
        open.setOnAction(event -> open());

        MenuItem copy = new MenuItem("复制表", ImageUtils.createImageView("/image/copy.png"));
        copy.setOnAction(event -> {
        });

        MenuItem delete = new MenuItem("删除表", ImageUtils.createImageView("/image/delete.png"));
        delete.setOnAction(event -> {
        });

        MenuItem refresh = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));
        refresh.disableProperty().bind(openProperty().not());
        refresh.setOnAction(event -> refresh());

        contextMenu.getItems().addAll(open, copy, delete, refresh);
        return contextMenu;
    }


    private class TableGeneralInfoTask extends BaseTask<List<KeyValuePair<String, Object>>> {

        @Override
        protected List<KeyValuePair<String, Object>> call() throws Exception {
            TableMetaData tableMetaData = MySQLTableTreeItem.this.getValue();
            SQLMetadataProvider metadataProvider = getMetadataProvider();
            return metadataProvider.getTableDetails(tableMetaData.getTableCat(), tableMetaData.getTableSchema(), tableMetaData.getTableName(),
                    new String[]{tableMetaData.getTableType()});
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            objectTabController.setGeneralInfoValue(getValue());
            TableMetaData tableMetaData = MySQLTableTreeItem.this.getValue();
            objectTabController.setDdlInfo(tableMetaData.getTableName());
        }
    }

}
