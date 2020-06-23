package com.jean.database.sql.view.treeitem;

import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.ViewContext;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.SQLObjectTabController;
import com.jean.database.sql.constant.Images;
import com.jean.database.sql.meta.CatalogMetaData;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.sql.Connection;
import java.util.List;

/**
 * @author jinshubao
 */
public class ServerTreeItem extends BaseDatabaseItem<String> {

    private final ContextMenu contextMenu;
    private final ViewContext viewContext;

    private SQLObjectTabController objectTabController;

    public ServerTreeItem(SQLConnectionConfiguration connectionConfiguration,
                          SQLMetadataProvider metadataProvider,
                          ViewContext viewContext) {
        super(connectionConfiguration.getConnectionName(), connectionConfiguration, metadataProvider);
        this.viewContext = viewContext;
        this.contextMenu = this.createContextMenu();
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    @Override
    public void doubleClick() {
        this.open();
    }

    @Override
    public void select() {
        if (objectTabController != null) {
            objectTabController.selected();
        }
    }

    @Override
    public void close() {
        if (objectTabController != null) {
            objectTabController.close();
        }
    }

    private void open() {
        if (!isOpen()) {
            TaskManger.execute(new OpenServerTask());
        }
    }

    private ContextMenu createContextMenu() {
        MenuItem open = new MenuItem("打开连接");
        open.disableProperty().bind(openProperty());
        open.setOnAction(event -> open());

        MenuItem close = new MenuItem("关闭连接");
        close.disableProperty().bind(openProperty().not());
        close.setOnAction(event -> close());

        MenuItem copy = new MenuItem("复制连接...");
        copy.setOnAction(event -> {
        });

        MenuItem delete = new MenuItem("删除连接", new ImageView(new Image(getClass().getResourceAsStream(Images.DELETE_IMAGE))));
        delete.setOnAction(event -> {
        });

        MenuItem properties = new MenuItem("连接属性...");
        properties.setOnAction(event -> {
        });

        MenuItem create = new MenuItem("新建数据库...");
        create.setOnAction(event -> {
        });

        MenuItem commandLine = new MenuItem("命令行界面...");
        commandLine.setOnAction(event -> {
        });

        MenuItem executeSqlFile = new MenuItem("运行SQL文件...");
        executeSqlFile.setOnAction(event -> {
        });

        MenuItem dataTransform = new MenuItem("数据传输...");
        dataTransform.setOnAction(event -> {
        });

        MenuItem refresh = new MenuItem("刷新", new ImageView(new Image(getClass().getResourceAsStream(Images.REFRESH_IMAGE))));
        refresh.disableProperty().bind(openProperty().not());
        refresh.setOnAction(event -> {
        });

        return new ContextMenu(open, close, new SeparatorMenuItem(),
                copy, delete, properties, new SeparatorMenuItem(),
                commandLine, executeSqlFile, dataTransform, new SeparatorMenuItem(),
                refresh);
    }


    private class OpenServerTask extends BaseTask<List<CatalogMetaData>> {

        @Override
        protected List<CatalogMetaData> call() throws Exception {
            try (Connection connection = getConnectionConfiguration().getConnection()) {
                return getMetadataProvider().getCatalogs(connection);
            }
        }


        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        protected void succeeded() {
            super.succeeded();
            try {
                String title = getConnectionConfiguration().getConnectionName();
                Callback<Class<?>, Object> factory = SQLObjectTabController.getFactory(title, viewContext);
                FxmlUtils.LoadFxmlResult fxmlResult = FxmlUtils.loadFxml("/fxml/sql-object-tab.fxml", factory);
                objectTabController = (SQLObjectTabController) fxmlResult.getController();
                ServerTreeItem serverTreeItem = ServerTreeItem.this;
                ObservableList children = serverTreeItem.getChildren();
                children.clear();
                List<CatalogMetaData> value = getValue();
                for (CatalogMetaData metaData : value) {
                    TreeItem item = new CatalogTreeItem(metaData, objectTabController, getConnectionConfiguration(), getMetadataProvider());
                    serverTreeItem.getChildren().add(item);
                }
                serverTreeItem.setExpanded(true);
                serverTreeItem.setOpen(true);
            } catch (Exception e) {
                DialogUtil.error(e);
            }
        }
    }
}
