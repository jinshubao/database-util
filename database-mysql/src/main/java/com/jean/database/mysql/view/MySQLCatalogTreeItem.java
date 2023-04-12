package com.jean.database.mysql.view;

import com.jean.database.context.ApplicationContext;
import com.jean.database.handler.ActionHandler;
import com.jean.database.item.BaseTreeItem;
import com.jean.database.task.BaseTask;
import com.jean.database.task.TaskManger;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.utils.ImageUtils;
import com.jean.database.mysql.controller.MySQLObjectTabController;
import com.jean.database.mysql.controller.MySQLQueryTabController;
import com.jean.database.sql.item.BaseDatabaseItem;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.meta.TableTypeMetaData;
import com.jean.database.view.CommonTreeItem;
import javafx.scene.control.*;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class MySQLCatalogTreeItem extends BaseTreeItem<CatalogMetaData> {

    private final ContextMenu contextMenu;

    private final SQLMetadataProvider metadataProvider;

    public MySQLCatalogTreeItem(ApplicationContext context, CatalogMetaData value, SQLMetadataProvider metadataProvider) {
        super(context, value);
        this.metadataProvider = metadataProvider;
        this.contextMenu = this.createContextMenu();
        this.setGraphic(ImageUtils.createImageView("/mysql/catalog.png"));
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


    private ContextMenu createContextMenu() {

        MenuItem open = new MenuItem("打开数据库", ImageUtils.createImageView("/image/connect.png"));
        open.disableProperty().bind(this.openProperty());
        open.setOnAction(event -> doubleClick());

        MenuItem query = new MenuItem("新建查询", ImageUtils.createImageView("/image/add.png"));
        query.disableProperty().bind(this.openProperty().not());
        query.setOnAction(event -> {
            List<String> list = getParent().getChildren().stream().map(item -> item.getValue().getTableCat()).collect(Collectors.toList());
            try {
                FxmlUtils.LoadFxmlResult loadFxmlResult =
                        FxmlUtils.loadFxml("fxml/mysql-query-tab.fxml", null, new MySQLQueryTabController(getContext(), list, getValue().getTableCat()));
                Tab tab = new Tab("查询", loadFxmlResult.getParent());
                getContext().getRootContext().addObjectTab(tab);
            } catch (IOException e) {
                DialogUtil.error(e);
            }
        });

        MenuItem close = new MenuItem("关闭数据库", ImageUtils.createImageView("/image/disconnect.png"));
        close.disableProperty().bind(this.openProperty().not());
        close.setOnAction(event -> close());

        MenuItem create = new MenuItem("新建数据库...", ImageUtils.createImageView("/image/add.png"));
        create.setOnAction(event -> {
            //TODO
        });

        MenuItem delete = new MenuItem("删除数据库", ImageUtils.createImageView("/image/delete.png"));
        delete.setOnAction(event -> {
            //TODO
        });

        MenuItem properties = new MenuItem("数据库属性...", ImageUtils.createImageView("/image/info.png"));
        properties.setOnAction(event -> {

        });

        MenuItem commandLine = new MenuItem("命令行界面...", ImageUtils.createImageView("/image/command.png"));
        commandLine.setOnAction(event -> {

        });

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...", ImageUtils.createImageView("/image/run.png"));
        executeSqlFile.setOnAction(event -> {
        });

        MenuItem exportStructAndData = new MenuItem("结构和数据...");
        exportStructAndData.setOnAction(event -> {
        });

        MenuItem exportStruct = new MenuItem("仅结构...");
        exportStruct.setOnAction(event -> {
        });

        Menu exportSqlFile = new Menu("转储SQL文件...", ImageUtils.createImageView("/image/export.png"));
        exportSqlFile.getItems().addAll(exportStructAndData, exportStruct);

        MenuItem printDatabase = new MenuItem("打印数据库...", ImageUtils.createImageView("/image/print.png"));
        printDatabase.setOnAction(event -> {
        });

        MenuItem dataTransform = new MenuItem("数据传输...");
        dataTransform.setOnAction(event -> {
        });

        MenuItem convertDatabaseToMode = new MenuItem("逆向数据库到模型...");
        convertDatabaseToMode.setOnAction(event -> {
        });

        MenuItem findInDatabase = new MenuItem("在数据库中查找...", ImageUtils.createImageView("/image/search.png"));
        findInDatabase.setOnAction(event -> {
        });

        MenuItem refresh = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));
        refresh.setOnAction(event -> {
            TaskManger.execute(new TableTypeTask());
        });

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


    private class TableTypeTask extends BaseTask<List<TableMetaData>> {

        private final WeakReference<MySQLCatalogTreeItem> catalogTreeItem = new WeakReference<>(MySQLCatalogTreeItem.this);
        private final CatalogMetaData catalogMetaData = MySQLCatalogTreeItem.this.getValue();
        private final SQLMetadataProvider metadataProvider = MySQLCatalogTreeItem.this.metadataProvider;

        private List<String> tableTypes = null;

        @Override
        protected List<TableMetaData> call() throws Exception {
            this.tableTypes = metadataProvider.getTableTypes();
            return metadataProvider.getTableMataData(catalogMetaData.getTableCat(), null, null, null);
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            List<TableMetaData> tableMataData = getValue();
            MySQLCatalogTreeItem catalogTreeItem = this.catalogTreeItem.get();
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
                tableTypeMetaData.setQuoteString(catalogMetaData.getQuoteString());
                tableTypeMetaData.setSeparator(catalogMetaData.getSeparator());
                tableTypeMetaData.setTableType(tableType);
                TreeItem typeItem = new MySQLTableTypeTreeItem(getContext(), tableTypeMetaData, metadataProvider);
                List<MySQLTableTreeItem> items = tableMataData.stream()
                        .filter(metaData -> metaData.getTableType().equals(tableType))
                        .map(metaData -> new MySQLTableTreeItem(getContext(), metaData, metadataProvider))
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
