package com.jean.database.mysql.view.item;

import com.jean.database.mysql.handler.MySQLCatalogTreeItemActionEventHandler;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.utils.ImageUtils;
import com.jean.database.view.AbstractTreeItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * @author jinshubao
 */
public class MySQLCatalogTreeItem extends AbstractTreeItem<CatalogMetaData> {

    private final ContextMenu contextMenu;

    private MySQLCatalogTreeItemActionEventHandler itemActionEventHandler;


    public MySQLCatalogTreeItem(CatalogMetaData value) {
        super(value, ImageUtils.createImageView("/mysql/catalog.png"));
        this.contextMenu = this.createContextMenu();
    }


    private ContextMenu createContextMenu() {

        MenuItem open = new MenuItem("打开数据库", ImageUtils.createImageView("/image/connect.png"));
//        open.disableProperty().bind(this.openProperty());
        open.setOnAction(event -> getItemActionEventHandler().open());

        MenuItem query = new MenuItem("新建查询", ImageUtils.createImageView("/image/add.png"));
//        query.disableProperty().bind(this.openProperty().not());
        query.setOnAction(event -> getItemActionEventHandler().newQuery());

        MenuItem close = new MenuItem("关闭数据库", ImageUtils.createImageView("/image/disconnect.png"));
//        close.disableProperty().bind(this.openProperty().not());
        close.setOnAction(event -> getItemActionEventHandler().close());

        MenuItem create = new MenuItem("新建数据库...", ImageUtils.createImageView("/image/add.png"));
        create.setOnAction(event -> getItemActionEventHandler().newDatabase());

        MenuItem delete = new MenuItem("删除数据库", ImageUtils.createImageView("/image/delete.png"));
        delete.setOnAction(event -> getItemActionEventHandler().delete());

        MenuItem properties = new MenuItem("数据库属性...", ImageUtils.createImageView("/image/info.png"));
        properties.setOnAction(event -> getItemActionEventHandler().properties());

        MenuItem commandLine = new MenuItem("命令行界面...", ImageUtils.createImageView("/image/command.png"));
        commandLine.setOnAction(event -> getItemActionEventHandler().commandLine());

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...", ImageUtils.createImageView("/image/run.png"));
        executeSqlFile.setOnAction(event -> getItemActionEventHandler().executeSqlFile());

        MenuItem exportStructAndData = new MenuItem("结构和数据...");
        exportStructAndData.setOnAction(event -> getItemActionEventHandler().exportStructAndData());

        MenuItem exportStruct = new MenuItem("仅结构...");
        exportStruct.setOnAction(event -> getItemActionEventHandler().exportStruct());

        Menu exportSqlFile = new Menu("转储SQL文件...", ImageUtils.createImageView("/image/export.png"));
        exportSqlFile.getItems().addAll(exportStructAndData, exportStruct);

        MenuItem printDatabase = new MenuItem("打印数据库...", ImageUtils.createImageView("/image/print.png"));
        printDatabase.setOnAction(event -> getItemActionEventHandler().printDatabase());

        MenuItem dataTransform = new MenuItem("数据传输...");
        dataTransform.setOnAction(event -> getItemActionEventHandler().dataTransform());

        MenuItem convertDatabaseToMode = new MenuItem("逆向数据库到模型...");
        convertDatabaseToMode.setOnAction(event -> getItemActionEventHandler().convertDatabaseToMode());

        MenuItem findInDatabase = new MenuItem("在数据库中查找...", ImageUtils.createImageView("/image/search.png"));
        findInDatabase.setOnAction(event -> getItemActionEventHandler().findInDatabase());

        MenuItem refresh = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));
        refresh.setOnAction(event -> getItemActionEventHandler().refresh());

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(open, close,
                new SeparatorMenuItem(),
                query,
                new SeparatorMenuItem(),
                create, delete, properties,
                new SeparatorMenuItem(),
                commandLine, executeSqlFile, exportSqlFile, printDatabase, dataTransform, convertDatabaseToMode, findInDatabase,
                new SeparatorMenuItem(),
                refresh);
        return contextMenu;
    }

    public void setItemActionEventHandler(MySQLCatalogTreeItemActionEventHandler itemActionEventHandler) {
        this.itemActionEventHandler = itemActionEventHandler;
    }

    public MySQLCatalogTreeItemActionEventHandler getItemActionEventHandler() {
        return itemActionEventHandler;
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public void click() {
        getItemActionEventHandler().click();
    }

    @Override
    public void doubleClick() {
        getItemActionEventHandler().doubleClick();
    }


}
