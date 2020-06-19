package com.jean.database.sql.view.treeitem;

import com.jean.database.api.LoggerWrapper;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.constant.Images;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.sql.view.TreeItemViewContext;
import com.jean.database.sql.view.handler.ICatalogItemActionEventHandler;
import com.jean.database.sql.view.handler.impl.CatalogItemActionEventHandlerImpl;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author jinshubao
 */
public class CatalogTreeItem extends BaseDatabaseItem<CatalogMetaData> {

    private final ContextMenu contextMenu;
    private final ICatalogItemActionEventHandler actionEventHandler;


    public CatalogTreeItem(CatalogMetaData value, TreeItemViewContext viewContext, SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider) {
        super(value, viewContext, connectionConfiguration, metadataProvider);
        this.actionEventHandler = LoggerWrapper.warp(new CatalogItemActionEventHandlerImpl());
        this.contextMenu = this.createContextMenu();
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.DATABASE_IMAGE))));
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

        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.openProperty());
        open.setOnAction(event -> this.actionEventHandler.onOpen(this));

        MenuItem close = new MenuItem("关闭数据库");
        close.disableProperty().bind(this.openProperty().not());
        close.setOnAction(event -> this.actionEventHandler.onClose(this));

        MenuItem create = new MenuItem("新建数据库...");
        create.setOnAction(event -> this.actionEventHandler.onCreate(this));

        MenuItem delete = new MenuItem("删除数据库", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> this.actionEventHandler.onDelete(this));

        MenuItem properties = new MenuItem("数据库属性...");
        properties.setOnAction(event -> this.actionEventHandler.onDetails(this));

        MenuItem commandLine = new MenuItem("命令行界面...");
        commandLine.setOnAction(event -> this.actionEventHandler.onOpenCommandLine(this));

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...");
        executeSqlFile.setOnAction(event -> this.actionEventHandler.onExecuteSqlFile(this));

        MenuItem exportStructAndData = new MenuItem("结构和数据...");
        exportStructAndData.setOnAction(event -> this.actionEventHandler.onExportStructAndData(this));

        MenuItem exportStruct = new MenuItem("仅结构...");
        exportStruct.setOnAction(event -> this.actionEventHandler.onExportStruct(this));

        Menu exportSqlFile = new Menu("转储SQL文件...");
        exportSqlFile.getItems().addAll(exportStructAndData, exportStruct);

        MenuItem printDatabase = new MenuItem("打印数据库...");
        printDatabase.setOnAction(event -> this.actionEventHandler.onPrintDatabase(this));

        MenuItem dataTransform = new MenuItem("数据传输...");
        dataTransform.setOnAction(event -> this.actionEventHandler.onTransformData(this));

        MenuItem convertDatabaseToMode = new MenuItem("逆向数据库到模型...");
        convertDatabaseToMode.setOnAction(event -> this.actionEventHandler.onConvertToMode(this));

        MenuItem findInDatabase = new MenuItem("在数据库中查找...");
        findInDatabase.setOnAction(event -> this.actionEventHandler.onFind(this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.setOnAction(event -> this.actionEventHandler.onRefresh(this));

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(open, close, new SeparatorMenuItem(),
                create, delete, properties, new SeparatorMenuItem(),
                commandLine, executeSqlFile, exportSqlFile, printDatabase, dataTransform, convertDatabaseToMode, findInDatabase, new SeparatorMenuItem(),
                refresh);
        return contextMenu;
    }

}
