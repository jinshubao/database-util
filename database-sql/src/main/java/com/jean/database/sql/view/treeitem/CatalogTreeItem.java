package com.jean.database.sql.view.treeitem;

import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.SQLObjectTabController;
import com.jean.database.sql.constant.Images;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.meta.TableTypeMetaData;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class CatalogTreeItem extends BaseDatabaseItem<CatalogMetaData> {

    private final ContextMenu contextMenu;
    private final SQLObjectTabController objectTabController;

    public CatalogTreeItem(CatalogMetaData value, SQLObjectTabController objectTabController, SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider) {
        super(value, connectionConfiguration, metadataProvider);
        this.objectTabController = objectTabController;
        this.contextMenu = this.createContextMenu();
        this.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Images.DATABASE_IMAGE))));
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
        if (!isOpen()) {
            TaskManger.execute(new TableTypeTask());
        }
    }

    @Override
    public void select() {
        objectTabController.setDdlInfo(getValue().getTableCat());
    }

    @Override
    public void close() {
    }

    private ContextMenu createContextMenu() {

        MenuItem open = new MenuItem("打开数据库");
        open.disableProperty().bind(this.openProperty());
        open.setOnAction(event -> doubleClick());

        MenuItem close = new MenuItem("关闭数据库");
        close.disableProperty().bind(this.openProperty().not());
        close.setOnAction(event -> close());

        MenuItem create = new MenuItem("新建数据库...");
        create.setOnAction(event -> {
            //TODO
        });

        MenuItem delete = new MenuItem("删除数据库", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> {
            //TODO
        });

        MenuItem properties = new MenuItem("数据库属性...");
        properties.setOnAction(event -> {

        });

        MenuItem commandLine = new MenuItem("命令行界面...");
        commandLine.setOnAction(event -> {

        });

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...");
        executeSqlFile.setOnAction(event -> {
        });

        MenuItem exportStructAndData = new MenuItem("结构和数据...");
        exportStructAndData.setOnAction(event -> {
        });

        MenuItem exportStruct = new MenuItem("仅结构...");
        exportStruct.setOnAction(event -> {
        });

        Menu exportSqlFile = new Menu("转储SQL文件...");
        exportSqlFile.getItems().addAll(exportStructAndData, exportStruct);

        MenuItem printDatabase = new MenuItem("打印数据库...");
        printDatabase.setOnAction(event -> {
        });

        MenuItem dataTransform = new MenuItem("数据传输...");
        dataTransform.setOnAction(event -> {
        });

        MenuItem convertDatabaseToMode = new MenuItem("逆向数据库到模型...");
        convertDatabaseToMode.setOnAction(event -> {
        });

        MenuItem findInDatabase = new MenuItem("在数据库中查找...");
        findInDatabase.setOnAction(event -> {
        });

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.setOnAction(event -> {
            TaskManger.execute(new TableTypeTask());
        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(open, close, new SeparatorMenuItem(),
                create, delete, properties, new SeparatorMenuItem(),
                commandLine, executeSqlFile, exportSqlFile, printDatabase, dataTransform, convertDatabaseToMode, findInDatabase, new SeparatorMenuItem(),
                refresh);
        return contextMenu;
    }


    private class TableTypeTask extends BaseTask<List<TableMetaData>> {

        private final WeakReference<CatalogTreeItem> catalogTreeItem = new WeakReference<>(CatalogTreeItem.this);
        private final CatalogMetaData catalogMetaData = CatalogTreeItem.this.getValue();
        private final SQLMetadataProvider metadataProvider = CatalogTreeItem.this.getMetadataProvider();
        private final SQLConnectionConfiguration connectionConfiguration = CatalogTreeItem.this.getConnectionConfiguration();

        private List<String> tableTypes = null;

        @Override
        protected List<TableMetaData> call() throws Exception {
            try (Connection connection = connectionConfiguration.getConnection()) {
                this.tableTypes = metadataProvider.getTableTypes(connection);
                return metadataProvider.getTableMataData(connection, catalogMetaData.getTableCat(), null, null, null);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            List<TableMetaData> tableMataData = getValue();
            CatalogTreeItem catalogTreeItem = this.catalogTreeItem.get();
            if (catalogTreeItem == null) {
                return;
            }
            catalogTreeItem.getChildren().clear();
            if (tableMataData == null || tableMataData.isEmpty()) {
                return;
            }

            for (String tableType : tableTypes) {
                TableTypeMetaData tableTypeMetaData = new TableTypeMetaData();
                tableTypeMetaData.setTableCat(catalogMetaData.getTableCat());
                tableTypeMetaData.setSeparator(catalogMetaData.getSeparator());
                tableTypeMetaData.setQuoteString(catalogMetaData.getQuoteString());
                tableTypeMetaData.setTableType(tableType);
                TreeItem typeItem = new TableTypeTreeItem(tableTypeMetaData, connectionConfiguration, metadataProvider, objectTabController);
                List<TableTreeItem> items = tableMataData.stream()
                        .filter(metaData -> metaData.getTableType().equals(tableType))
                        .map(metaData -> new TableTreeItem(metaData, connectionConfiguration, metadataProvider, objectTabController))
                        .collect(Collectors.toList());
                //noinspection unchecked
                catalogTreeItem.getChildren().add(typeItem);
                if (!items.isEmpty()) {
                    //noinspection unchecked
                    typeItem.getChildren().addAll(items);
                    typeItem.setExpanded(true);
                }
            }
            catalogTreeItem.setExpanded(true);
            catalogTreeItem.setOpen(true);
        }
    }
}
