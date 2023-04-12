package com.jean.database.mysql.view;

import com.jean.database.ability.ICloseable;
import com.jean.database.ability.IRefreshable;
import com.jean.database.action.IContextMenu;
import com.jean.database.context.ApplicationContext;
import com.jean.database.item.BaseTreeItem;
import com.jean.database.mysql.controller.MySQLObjectTabController;
import com.jean.database.mysql.provider.MySQLMetadataProvider;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.task.BackgroundTask;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.utils.ImageUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.util.List;


public class MySQLServerTreeItem extends BaseTreeItem<String> implements IContextMenu, IRefreshable, ICloseable {

    private final ContextMenu contextMenu;

    private final MySQLMetadataProvider metadataProvider;


    public MySQLServerTreeItem(ApplicationContext context, MySQLMetadataProvider metadataProvider, String value) {
        super(context, value);
        this.metadataProvider = metadataProvider;
        this.contextMenu = createContextMenu();
    }

    @Override
    public void select() {

    }

    private void open() {
        if (!isOpen()) {
            getContext().getBackgroundTaskManager().execute(new OpenServerTask("打开数据库"));
        }
    }

    private ContextMenu createContextMenu() {
        MenuItem open = new MenuItem("打开连接", ImageUtils.createImageView("/image/connect.png"));
        open.disableProperty().bind(openProperty());
        open.setOnAction(event -> open());

        MenuItem close = new MenuItem("关闭连接", ImageUtils.createImageView("/image/disconnect.png"));
        close.disableProperty().bind(openProperty().not());
        close.setOnAction(event -> close());

        MenuItem copy = new MenuItem("复制连接...", ImageUtils.createImageView("/image/copy.png"));
        copy.setOnAction(event -> {
        });

        MenuItem delete = new MenuItem("删除连接", ImageUtils.createImageView("/image/delete.png"));
        delete.setOnAction(event -> {
        });

        MenuItem properties = new MenuItem("连接属性...", ImageUtils.createImageView("/image/info.png"));
        properties.setOnAction(event -> {
        });

        MenuItem create = new MenuItem("新建数据库...", ImageUtils.createImageView("/image/add.png"));
        create.setOnAction(event -> {
        });

        MenuItem commandLine = new MenuItem("命令行界面...", ImageUtils.createImageView("/image/command.png"));
        commandLine.setOnAction(event -> {
        });

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...", ImageUtils.createImageView("/image/run.png"));
        executeSqlFile.setOnAction(event -> {
        });

        MenuItem dataTransform = new MenuItem("数据传输...", ImageUtils.createImageView("/image/trans.png"));
        dataTransform.setOnAction(event -> {
        });

        MenuItem refresh = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));
        refresh.disableProperty().bind(openProperty().not());
        refresh.setOnAction(event -> {
        });

        return new ContextMenu(open, close, new SeparatorMenuItem(),
                copy, delete, properties, new SeparatorMenuItem(),
                commandLine, executeSqlFile, dataTransform, new SeparatorMenuItem(),
                refresh);
    }


    private class OpenServerTask extends BackgroundTask<List<CatalogMetaData>> {

        public OpenServerTask(String taskName) {
            super(taskName);
        }

        @Override
        protected List<CatalogMetaData> call() throws Exception {
            return metadataProvider.getCatalogs();
        }


        @Override
        protected void succeeded() {
            super.succeeded();
            TreeItem<String> serverTreeItem = MySQLServerTreeItem.this;
            ObservableList<TreeItem<String>> children = serverTreeItem.getChildren();
            children.clear();
            List<CatalogMetaData> value = getValue();
            for (CatalogMetaData metaData : value) {
                TreeItem item = new MySQLCatalogTreeItem(getContext(), metaData, metadataProvider);
                serverTreeItem.getChildren().add(item);
            }
            serverTreeItem.setExpanded(true);
            openProperty().set(true);

            try {
                FxmlUtils.LoadFxmlResult fxmlResult = FxmlUtils.loadFxml("fxml/mysql-object-tab.fxml", null,
                        new MySQLObjectTabController(getContext(), "打开数据库"));
                MySQLObjectTabController  objectTabController = (MySQLObjectTabController) fxmlResult.getController();
                getContext().getRootContext().addObjectTab(objectTabController.getObjectTab());
                objectTabController.select();
            } catch (IOException e) {
                DialogUtil.error(e);
            }

        }
    }
}
