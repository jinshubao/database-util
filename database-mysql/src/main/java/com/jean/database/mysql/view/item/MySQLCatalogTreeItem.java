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

    MenuItem open;
    MenuItem query;
    MenuItem create;
    MenuItem delete;
    MenuItem properties;
    MenuItem commandLine;
    MenuItem executeSqlFile;
    MenuItem exportStructAndData;
    MenuItem exportStruct;
    MenuItem printDatabase;
    MenuItem dataTransform;
    MenuItem convertDatabaseToMode;


    MenuItem findInDatabase;
    MenuItem refresh;


    private MySQLCatalogTreeItemActionEventHandler itemActionEventHandler;


    public MySQLCatalogTreeItem(CatalogMetaData value) {
        super(value, ImageUtils.createImageView("/mysql/catalog.png"));
        this.contextMenu = this.createContextMenu();
    }


    public void setItemActionEventHandler(MySQLCatalogTreeItemActionEventHandler itemActionEventHandler) {
        this.itemActionEventHandler = itemActionEventHandler;

        open.disableProperty().bind(getItemActionEventHandler().openMenuDisableProperty());
        open.setOnAction(event -> getItemActionEventHandler().open());

        query.disableProperty().bind(getItemActionEventHandler().queryMenuDisableProperty());
        query.setOnAction(event -> getItemActionEventHandler().newQuery());

        create.disableProperty().bind(getItemActionEventHandler().createMenuDisableProperty());
        create.setOnAction(event -> getItemActionEventHandler().newDatabase());

        delete.disableProperty().bind(getItemActionEventHandler().deleteMenuDisableProperty());
        delete.setOnAction(event -> getItemActionEventHandler().delete());

        properties.disableProperty().bind(getItemActionEventHandler().propertiesMenuDisableProperty());
        properties.setOnAction(event -> getItemActionEventHandler().properties());

        commandLine.disableProperty().bind(getItemActionEventHandler().commandLineMenuDisableProperty());
        commandLine.setOnAction(event -> getItemActionEventHandler().commandLine());

        executeSqlFile.disableProperty().bind(getItemActionEventHandler().executeSqlFileDisableProperty());
        executeSqlFile.setOnAction(event -> getItemActionEventHandler().executeSqlFile());

        exportStructAndData.disableProperty().bind(getItemActionEventHandler().exportStructAndDataMenuDisableProperty());
        exportStructAndData.setOnAction(event -> getItemActionEventHandler().exportStructAndData());

        exportStruct.disableProperty().bind(getItemActionEventHandler().exportStructMenuDisableProperty());
        exportStruct.setOnAction(event -> getItemActionEventHandler().exportStruct());

        printDatabase.disableProperty().bind(getItemActionEventHandler().printDatabaseMenuDisableProperty());
        printDatabase.setOnAction(event -> getItemActionEventHandler().printDatabase());

        dataTransform.disableProperty().bind(getItemActionEventHandler().dataTransformMenuDisableProperty());
        dataTransform.setOnAction(event -> getItemActionEventHandler().dataTransform());

        convertDatabaseToMode.disableProperty().bind(getItemActionEventHandler().convertDatabaseToModeMenuDisableProperty());
        convertDatabaseToMode.setOnAction(event -> getItemActionEventHandler().convertDatabaseToMode());

        findInDatabase.disableProperty().bind(getItemActionEventHandler().findInDatabaseMenuDisableProperty());
        findInDatabase.setOnAction(event -> getItemActionEventHandler().findInDatabase());

        refresh.disableProperty().bind(getItemActionEventHandler().refreshMenuDisableProperty());
        refresh.setOnAction(event -> getItemActionEventHandler().refresh());


    }


    private ContextMenu createContextMenu() {

        open = new MenuItem("打开数据库", ImageUtils.createImageView("/image/connect.png"));

        query = new MenuItem("新建查询", ImageUtils.createImageView("/image/add.png"));

        create = new MenuItem("新建数据库...", ImageUtils.createImageView("/image/add.png"));

        delete = new MenuItem("删除数据库", ImageUtils.createImageView("/image/delete.png"));

        properties = new MenuItem("数据库属性...", ImageUtils.createImageView("/image/info.png"));

        commandLine = new MenuItem("命令行界面...", ImageUtils.createImageView("/image/command.png"));

        executeSqlFile = new MenuItem("运行SQL文件...", ImageUtils.createImageView("/image/run.png"));

        exportStructAndData = new MenuItem("结构和数据...");

        exportStruct = new MenuItem("仅结构...");

        Menu exportSqlFile = new Menu("转储SQL文件...", ImageUtils.createImageView("/image/export.png"));
        exportSqlFile.getItems().addAll(exportStructAndData, exportStruct);

        printDatabase = new MenuItem("打印数据库...", ImageUtils.createImageView("/image/print.png"));

        dataTransform = new MenuItem("数据传输...");

        convertDatabaseToMode = new MenuItem("逆向数据库到模型...");

        findInDatabase = new MenuItem("在数据库中查找...", ImageUtils.createImageView("/image/search.png"));

        refresh = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                open,
                new SeparatorMenuItem(),
                query,
                new SeparatorMenuItem(),
                create, delete, properties,
                new SeparatorMenuItem(),
                commandLine, executeSqlFile, exportSqlFile, printDatabase, dataTransform, convertDatabaseToMode, findInDatabase,
                new SeparatorMenuItem(),
                refresh
        );
        return contextMenu;
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
