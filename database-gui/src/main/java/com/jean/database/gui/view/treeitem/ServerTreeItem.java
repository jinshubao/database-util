package com.jean.database.gui.view.treeitem;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.view.handler.IMouseEventHandler;
import com.jean.database.gui.view.handler.IServerItemActionEventHandler;
import com.jean.database.gui.view.handler.impl.ServerItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author jinshubao
 */
public class ServerTreeItem extends BaseTreeItem<String> {

    private final ContextMenu contextMenu;
    private final IServerItemActionEventHandler serverItemActionEventHandler;

    public ServerTreeItem(String value, IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider) {
        super(value, connectionConfiguration, metadataProvider);
        this.serverItemActionEventHandler = LoggerWrapper.warp(new ServerItemActionEventHandlerImpl());
        this.contextMenu = this.createContextMenu();
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public IMouseEventHandler getMouseEventHandler() {
        return this.serverItemActionEventHandler;
    }


    @Override
    public void close() {
        serverItemActionEventHandler.onClose(this);
    }


    private ContextMenu createContextMenu() {
        MenuItem open = new MenuItem("打开连接");
        open.disableProperty().bind(openProperty());
        open.setOnAction(event -> serverItemActionEventHandler.onOpen(ServerTreeItem.this));

        MenuItem close = new MenuItem("关闭连接");
        close.disableProperty().bind(openProperty().not());
        close.setOnAction(event -> serverItemActionEventHandler.onClose(ServerTreeItem.this));

        MenuItem copy = new MenuItem("复制连接...");
        copy.setOnAction(event -> serverItemActionEventHandler.onCopy(ServerTreeItem.this));

        MenuItem delete = new MenuItem("删除连接", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> serverItemActionEventHandler.onDelete(ServerTreeItem.this));

        MenuItem properties = new MenuItem("连接属性...");
        properties.setOnAction(event -> serverItemActionEventHandler.onDetails(ServerTreeItem.this));

        MenuItem create = new MenuItem("新建数据库...");
        create.setOnAction(event -> serverItemActionEventHandler.onCreate(ServerTreeItem.this));

        MenuItem commandLine = new MenuItem("命令行界面...");
        commandLine.setOnAction(event -> this.serverItemActionEventHandler.onOpenCommandLine(this));

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...");
        executeSqlFile.setOnAction(event -> this.serverItemActionEventHandler.onExecuteSqlFile(this));

        MenuItem dataTransform = new MenuItem("数据传输...");
        dataTransform.setOnAction(event -> this.serverItemActionEventHandler.onTransformData(this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.disableProperty().bind(openProperty().not());
        refresh.setOnAction(event -> serverItemActionEventHandler.refresh(ServerTreeItem.this));

        return new ContextMenu(open, close, new SeparatorMenuItem(),
                copy, delete, properties, new SeparatorMenuItem(),
                commandLine, executeSqlFile, dataTransform, new SeparatorMenuItem(),
                refresh);
    }

}
