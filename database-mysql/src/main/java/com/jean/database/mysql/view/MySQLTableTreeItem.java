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
        // 先切换到主连接Tab
        objectTabController.select();
        // 然后打开表（会创建或切换到对应的子Tab）
        open();
    }

    @Override
    public void select() {
        // 先切换到 Object Tab
        objectTabController.select();
        // 如果表已打开，切换到对应的数据 Tab
        if (isOpen() && sqlDataTableTab != null) {
            objectTabController.selectObjectTab(sqlDataTableTab);
        }
        TaskManger.execute(new TableGeneralInfoTask());
    }

    private void open() {
        if (isOpen()) {
            // 如果已经打开，直接切换到对应的子 Tab
            // 主 Tab 已经在 select() 方法中切换过了
            objectTabController.selectObjectTab(sqlDataTableTab);
            return;
        }
        setOpen(true);
        sqlDataTableTab = new MySQLDataTableTab(getValue(), getMetadataProvider());
        sqlDataTableTab.setOnClosed(event -> {
            if (sqlDataTableTab != null) {
                sqlDataTableTab.close();
                sqlDataTableTab = null;
                setOpen(false);
            }
        });
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


    private class TableGeneralInfoTask extends BaseTask<List<com.jean.database.api.KeyValuePair<String, Object>>> {

        private String ddlStatement;

        @Override
        protected List<com.jean.database.api.KeyValuePair<String, Object>> call() throws Exception {
            TableMetaData tableMetaData = MySQLTableTreeItem.this.getValue();
            SQLMetadataProvider metadataProvider = getMetadataProvider();
            
            // 获取表详情
            List<com.jean.database.api.KeyValuePair<String, Object>> details = metadataProvider.getTableDetails(
                    tableMetaData.getTableCat(), 
                    tableMetaData.getTableSchema(), 
                    tableMetaData.getTableName(),
                    new String[]{tableMetaData.getTableType()});
            
            // 获取表 DDL 语句
            try {
                ddlStatement = metadataProvider.getTableDDL(
                        tableMetaData.getTableCat(), 
                        tableMetaData.getTableSchema(), 
                        tableMetaData.getTableName());
            } catch (Exception e) {
                ddlStatement = "-- 获取 DDL 失败: " + e.getMessage();
            }
            
            return details;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            objectTabController.setGeneralInfoValue(getValue());
            objectTabController.setDdlInfo(ddlStatement);
        }
    }

}
