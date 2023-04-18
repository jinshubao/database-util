package com.jean.database.mysql.view;

import com.jean.database.context.ApplicationContext;
import com.jean.database.task.BackgroundTask;
import com.jean.database.api.KeyValuePair;
import com.jean.database.utils.ImageUtils;
import com.jean.database.sql.item.SQLDatabaseItem;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.TableMetaData;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.List;

/**
 * @author jinshubao
 */
public class MySQLTableTreeItem extends SQLDatabaseItem<TableMetaData> {

    private final ContextMenu contextMenu;
    private MySQLDataTableTab sqlDataTableTab;

    private final SQLMetadataProvider metadataProvider;

    public MySQLTableTreeItem(ApplicationContext context,
                              TableMetaData tableMetaData,
                              SQLMetadataProvider metadataProvider) {
        super(context, tableMetaData);
        this.metadataProvider =  metadataProvider;
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
//        getContext().getBackgroundTaskManager().execute(new TableGeneralInfoTask("获取表信息"));
    }

    private void open() {
        if (isOpen()) {
            return;
        }
        setOpen(true);
        TableMetaData tableMetaData = getValue();
        sqlDataTableTab = new MySQLDataTableTab(getContext(), tableMetaData, metadataProvider);
        sqlDataTableTab.setOnClosed(event -> sqlDataTableTab.close());
        getContext().addObjectTab(sqlDataTableTab);
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
        getContext().execute(new TableGeneralInfoTask("获取表信息"));
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


    private class TableGeneralInfoTask extends BackgroundTask<List<KeyValuePair<String, Object>>> {

        public TableGeneralInfoTask(String taskName) {
            super(taskName);
        }

        @Override
        protected List<KeyValuePair<String, Object>> doBackground() throws Exception {
            TableMetaData tableMetaData = MySQLTableTreeItem.this.getValue();
            SQLMetadataProvider metadataProvider = MySQLTableTreeItem.this.metadataProvider;
            return metadataProvider.getTableDetails(tableMetaData.getTableCat(), tableMetaData.getTableSchema(), tableMetaData.getTableName(),
                    new String[]{tableMetaData.getTableType()});
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            // TODO 刷新数据
//            objectTabController.setGeneralInfoValue(getValue());
//            TableMetaData tableMetaData = MySQLTableTreeItem.this.getValue();
//            objectTabController.setDdlInfo(tableMetaData.getTableName());
        }
    }

}
