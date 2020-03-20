package com.jean.database.gui.view.treeitem;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.factory.LoggerWrapper;
import com.jean.database.gui.view.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.view.handler.IMouseEventHandler;
import com.jean.database.gui.view.handler.impl.CatalogItemActionEventHandlerImpl;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author jinshubao
 */
public class CatalogTreeItem extends BaseTreeItem<CatalogMetaData> {

    private final ContextMenu contextMenu;
    private final ICatalogItemActionEventHandler catalogItemActionEventHandler;

    public CatalogTreeItem(CatalogMetaData value, Node root, IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider) {
        super(value, root, connectionConfiguration, metadataProvider);
        this.catalogItemActionEventHandler = LoggerWrapper.warp(new CatalogItemActionEventHandlerImpl(root));
        this.contextMenu = this.createContextMenu();
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.DATABASE_IMAGE))));
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public IMouseEventHandler getMouseEventHandler() {
        return this.catalogItemActionEventHandler;
    }

    @Override
    public void close() {
        catalogItemActionEventHandler.onClose(this);
    }

    private ContextMenu createContextMenu() {

        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.openProperty());
        open.setOnAction(event -> this.catalogItemActionEventHandler.onOpen(this));

        MenuItem close = new MenuItem("关闭数据库");
        close.disableProperty().bind(this.openProperty().not());
        close.setOnAction(event -> this.catalogItemActionEventHandler.onClose(this));

        MenuItem create = new MenuItem("新建数据库...");
        create.setOnAction(event -> this.catalogItemActionEventHandler.onCreate(this));

        MenuItem delete = new MenuItem("删除数据库", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> this.catalogItemActionEventHandler.onDelete(this));

        MenuItem properties = new MenuItem("数据库属性...");
        properties.setOnAction(event -> this.catalogItemActionEventHandler.onDetails(this));

        MenuItem commandLine = new MenuItem("命令行界面...");
        commandLine.setOnAction(event -> this.catalogItemActionEventHandler.onOpenCommandLine(this));

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...");
        executeSqlFile.setOnAction(event -> this.catalogItemActionEventHandler.onExecuteSqlFile(this));

        MenuItem exportStructAndData = new MenuItem("结构和数据...");
        exportStructAndData.setOnAction(event -> this.catalogItemActionEventHandler.onExportStructAndData(this));

        MenuItem exportStruct = new MenuItem("仅结构...");
        exportStruct.setOnAction(event -> this.catalogItemActionEventHandler.onExportStruct(this));

        Menu exportSqlFile = new Menu("转储SQL文件...");
        exportSqlFile.getItems().addAll(exportStructAndData, exportStruct);

        MenuItem printDatabase = new MenuItem("打印数据库...");
        printDatabase.setOnAction(event -> this.catalogItemActionEventHandler.onPrintDatabase(this));

        MenuItem dataTransform = new MenuItem("数据传输...");
        dataTransform.setOnAction(event -> this.catalogItemActionEventHandler.onTransformData(this));

        MenuItem convertDatabaseToMode = new MenuItem("逆向数据库到模型...");
        convertDatabaseToMode.setOnAction(event -> this.catalogItemActionEventHandler.onConvertToMode(this));

        MenuItem findInDatabase = new MenuItem("在数据库中查找...");
        findInDatabase.setOnAction(event -> this.catalogItemActionEventHandler.onFind(this));

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.setOnAction(event -> this.catalogItemActionEventHandler.refresh(this));

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(open, close, new SeparatorMenuItem(),
                create, delete, properties, new SeparatorMenuItem(),
                commandLine, executeSqlFile, exportSqlFile, printDatabase, dataTransform, convertDatabaseToMode, findInDatabase, new SeparatorMenuItem(),
                refresh);
        return contextMenu;
    }

}
