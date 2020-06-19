package com.jean.database.sql.view.treeitem;

import com.jean.database.api.LoggerWrapper;
import com.jean.database.api.view.ViewContext;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.constant.Images;
import com.jean.database.sql.view.SQLDDLInfoTab;
import com.jean.database.sql.view.SQLGeneralInfoTab;
import com.jean.database.sql.view.SQLObjectTab;
import com.jean.database.sql.view.TreeItemViewContext;
import com.jean.database.sql.view.handler.IDatabaseTreeItemActionEventHandler;
import com.jean.database.sql.view.handler.impl.DatabaseTreeItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author jinshubao
 */
public class DatabaseTreeItem extends BaseDatabaseItem<String> {

    private final ContextMenu contextMenu;
    private final IDatabaseTreeItemActionEventHandler actionEventHandler;

    public DatabaseTreeItem(String value,
                            ViewContext viewContext,
                            SQLConnectionConfiguration connectionConfiguration,
                            SQLMetadataProvider metadataProvider) {
        super(value, new TreeItemViewContext(viewContext, new SQLObjectTab("对象"), new SQLGeneralInfoTab("常规"), new SQLDDLInfoTab("DDL")), connectionConfiguration, metadataProvider);
        this.actionEventHandler = LoggerWrapper.warp(new DatabaseTreeItemActionEventHandlerImpl());
        this.contextMenu = this.createContextMenu();
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public void click() {
        this.actionEventHandler.onClick(this);
    }

    @Override
    public void doubleClick() {
        this.actionEventHandler.onDoubleClick(this);
    }

    @Override
    public void select() {
        this.actionEventHandler.onSelected(this);
    }

    @Override
    public void close() {
        actionEventHandler.onClose(this);
    }

    private ContextMenu createContextMenu() {
        MenuItem open = new MenuItem("打开连接");
        open.disableProperty().bind(openProperty());
        open.setOnAction(event -> actionEventHandler.onOpen(this));

        MenuItem close = new MenuItem("关闭连接");
        close.disableProperty().bind(openProperty().not());
        close.setOnAction(event -> actionEventHandler.onClose(this));

        MenuItem copy = new MenuItem("复制连接...");
        copy.setOnAction(event -> actionEventHandler.onCopy(this));

        MenuItem delete = new MenuItem("删除连接", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> actionEventHandler.onDelete(this));

        MenuItem properties = new MenuItem("连接属性...");
        properties.setOnAction(event -> actionEventHandler.onDetails(this));

        MenuItem create = new MenuItem("新建数据库...");
        create.setOnAction(event -> actionEventHandler.onCreate(this));

        MenuItem commandLine = new MenuItem("命令行界面...");
        commandLine.setOnAction(event -> this.actionEventHandler.onOpenCommandLine(this));

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...");
        executeSqlFile.setOnAction(event -> this.actionEventHandler.onExecuteSqlFile(this));

        MenuItem dataTransform = new MenuItem("数据传输...");
        dataTransform.setOnAction(event -> this.actionEventHandler.onTransformData(this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.disableProperty().bind(openProperty().not());
        refresh.setOnAction(event -> actionEventHandler.onRefresh(this));

        return new ContextMenu(open, close, new SeparatorMenuItem(),
                copy, delete, properties, new SeparatorMenuItem(),
                commandLine, executeSqlFile, dataTransform, new SeparatorMenuItem(),
                refresh);
    }
}
