package com.jean.database.mysql.view.item;

import com.jean.database.mysql.config.MySQLConnectionConfiguration;
import com.jean.database.mysql.handler.MySQLServerTreeItemActionEventHandler;
import com.jean.database.utils.ImageUtils;
import com.jean.database.view.AbstractTreeItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class MySQLServerTreeItem extends AbstractTreeItem<MySQLConnectionConfiguration> {

    private MySQLServerTreeItemActionEventHandler itemActionHandler;

    MenuItem openMenu;
    MenuItem closeMenu;
    MenuItem copyMenu;
    MenuItem deleteMenu;
    MenuItem propertiesMenu;
    MenuItem createMenu;
    MenuItem commandLineMenu;
    MenuItem executeSqlFileMenu;
    MenuItem dataTransformMenu;
    MenuItem refreshMenu;

    ContextMenu contextMenu;

    MySQLConnectionConfiguration configuration;

    public MySQLServerTreeItem(MySQLConnectionConfiguration configuration) {
        super(configuration, ImageUtils.createImageView("/mysql/mysql.png"));
        this.configuration = configuration;
        this.contextMenu = createContextMenu();
    }


    private ContextMenu createContextMenu() {

        openMenu = new MenuItem("打开连接", ImageUtils.createImageView("/image/connect.png"));

        closeMenu = new MenuItem("关闭连接", ImageUtils.createImageView("/image/disconnect.png"));

        copyMenu = new MenuItem("复制连接...", ImageUtils.createImageView("/image/copy.png"));

        deleteMenu = new MenuItem("删除连接", ImageUtils.createImageView("/image/delete.png"));

        propertiesMenu = new MenuItem("连接属性...", ImageUtils.createImageView("/image/info.png"));

        createMenu = new MenuItem("新建数据库...", ImageUtils.createImageView("/image/add.png"));

        commandLineMenu = new MenuItem("命令行界面...", ImageUtils.createImageView("/image/command.png"));

        executeSqlFileMenu = new MenuItem("运行SQL文件...", ImageUtils.createImageView("/image/run.png"));

        dataTransformMenu = new MenuItem("数据传输...", ImageUtils.createImageView("/image/trans.png"));

        refreshMenu = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));

        return new ContextMenu(
                openMenu, closeMenu,
                new SeparatorMenuItem(),
                createMenu, refreshMenu,
                new SeparatorMenuItem(),
                copyMenu, deleteMenu, propertiesMenu,
                new SeparatorMenuItem(),
                commandLineMenu, executeSqlFileMenu, dataTransformMenu
        );
    }


    public void setItemActionHandler(MySQLServerTreeItemActionEventHandler itemActionHandler) {
        this.itemActionHandler = itemActionHandler;
        openMenu.disableProperty().bind(itemActionHandler.openMenuDisableProperty());
        openMenu.setOnAction(event -> itemActionHandler.open());

        closeMenu.disableProperty().bind(itemActionHandler.closeMenuDisableProperty());
        closeMenu.setOnAction(event -> itemActionHandler.close());

        copyMenu.disableProperty().bind(itemActionHandler.copyMenuDisableProperty());
        copyMenu.setOnAction(event -> itemActionHandler.copy());

        deleteMenu.disableProperty().bind(itemActionHandler.deleteMenuDisableProperty());
        deleteMenu.setOnAction(event -> itemActionHandler.delete());

        propertiesMenu.disableProperty().bind(itemActionHandler.propertiesMenuDisableProperty());
        propertiesMenu.setOnAction(event -> itemActionHandler.properties());

        createMenu.disableProperty().bind(itemActionHandler.createMenuDisableProperty());
        createMenu.setOnAction(event -> itemActionHandler.create());

        commandLineMenu.disableProperty().bind(itemActionHandler.commandLineMenuDisableProperty());
        commandLineMenu.setOnAction(event -> itemActionHandler.commandLine());

        executeSqlFileMenu.disableProperty().bind(itemActionHandler.executeSqlFileMenuDisableProperty());
        executeSqlFileMenu.setOnAction(event -> itemActionHandler.executeSqlFile());

        dataTransformMenu.disableProperty().bind(itemActionHandler.dataTransformMenuDisableProperty());
        dataTransformMenu.setOnAction(event -> itemActionHandler.dataTransform());

        refreshMenu.disableProperty().bind(itemActionHandler.refreshMenuDisableProperty());
        refreshMenu.setOnAction(event -> itemActionHandler.refresh());




    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }


    public MySQLServerTreeItemActionEventHandler getItemActionHandler() {
        return itemActionHandler;
    }

    @Override
    public void click() {
        getItemActionHandler().click();
    }

    @Override
    public void doubleClick() {
        getItemActionHandler().doubleClick();
    }

    @Override
    public void select() {
        getItemActionHandler().select();
    }
}