package com.jean.database.mysql.controller;

import com.jean.database.ability.ICloseable;
import com.jean.database.action.IMouseAction;
import com.jean.database.context.ApplicationContext;
import com.jean.database.controller.AbstractController;
import com.jean.database.mysql.config.MySQLConnectionConfiguration;
import com.jean.database.mysql.handler.ServerTreeItemActionHandler;
import com.jean.database.mysql.provider.MySQLMetadataProvider;
import com.jean.database.mysql.view.MySQLCatalogTreeItem;
import com.jean.database.mysql.view.MySQLServerTreeItem;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.task.BackgroundTask;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.ImageUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class MySQLServerTreeItemController extends AbstractController implements IMouseAction, ServerTreeItemActionHandler {

    private TreeItem treeItem;

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

    private ObjectProperty<DataSource> dataSource = new SimpleObjectProperty<>(null, "dataSource");
    private ObjectProperty<SQLMetadataProvider> metadataProvider = new SimpleObjectProperty<>(null, "metadataProvider");

    private BooleanProperty taskRunning = new SimpleBooleanProperty(false, "taskRunning");
    private BooleanProperty connected = new SimpleBooleanProperty(false, "connected");


    public MySQLServerTreeItemController(ApplicationContext applicationContext, MySQLConnectionConfiguration configuration) {
        super(applicationContext);
        this.configuration = configuration;
        this.contextMenu = createContextMenu();
        this.treeItem = createTreeItem();
        getContext().addDatabaseItem(this.treeItem);
        getContext().setSelected(this.treeItem);

        connected.bind(new BooleanBinding() {
            {
                bind(dataSource);
            }

            @Override
            protected boolean computeValue() {
                return dataSource.get() != null;
            }
        });
    }

    private TreeItem createTreeItem() {
        return new MySQLServerTreeItem(configuration, ImageUtils.createImageView("/mysql/mysql.png"), this, contextMenu);
    }


    private ContextMenu createContextMenu() {
        openMenu = new MenuItem("打开连接", ImageUtils.createImageView("/image/connect.png"));
        openMenu.disableProperty().bind(connected.or(taskRunning));
        openMenu.setOnAction(event -> this.open());

        closeMenu = new MenuItem("关闭连接", ImageUtils.createImageView("/image/disconnect.png"));
        closeMenu.disableProperty().bind(connected.not().or(taskRunning));
        closeMenu.setOnAction(event -> this.close());

        copyMenu = new MenuItem("复制连接...", ImageUtils.createImageView("/image/copy.png"));
        copyMenu.disableProperty().bind(taskRunning);
        copyMenu.setOnAction(event -> this.copy());

        deleteMenu = new MenuItem("删除连接", ImageUtils.createImageView("/image/delete.png"));
        deleteMenu.disableProperty().bind(taskRunning);
        deleteMenu.setOnAction(event -> this.delete());

        propertiesMenu = new MenuItem("连接属性...", ImageUtils.createImageView("/image/info.png"));
        propertiesMenu.disableProperty().bind(taskRunning);
        propertiesMenu.setOnAction(event -> this.properties());

        createMenu = new MenuItem("新建数据库...", ImageUtils.createImageView("/image/add.png"));
        createMenu.disableProperty().bind(connected.not().or(taskRunning));
        createMenu.setOnAction(event -> this.create());

        commandLineMenu = new MenuItem("命令行界面...", ImageUtils.createImageView("/image/command.png"));
        commandLineMenu.disableProperty().bind(connected.not().or(taskRunning));
        commandLineMenu.setOnAction(event -> this.commandLine());

        executeSqlFileMenu = new MenuItem("运行SQL文件...", ImageUtils.createImageView("/image/run.png"));
        executeSqlFileMenu.disableProperty().bind(connected.not().or(taskRunning));
        executeSqlFileMenu.setOnAction(event -> this.executeSqlFile());

        dataTransformMenu = new MenuItem("数据传输...", ImageUtils.createImageView("/image/trans.png"));
        dataTransformMenu.disableProperty().bind(connected.not().or(taskRunning));
        dataTransformMenu.setOnAction(event -> this.dataTransform());

        refreshMenu = new MenuItem("刷新", ImageUtils.createImageView("/image/refresh.png"));
        refreshMenu.disableProperty().bind(connected.not().or(taskRunning));
        refreshMenu.setOnAction(event -> this.refresh());

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

    @Override
    public void click() {

    }

    @Override
    public void doubleClick() {
        open();
    }

    @Override
    public void select() {

    }

    @Override
    public void open() {

        if (dataSource.get() == null) {
            CreateDataSourceTask createDataSourceTask = new CreateDataSourceTask(configuration);
            createDataSourceTask.setOnSucceeded(event -> {
                dataSource.set((DataSource) event.getSource().getValue());
                metadataProvider.set(new MySQLMetadataProvider(dataSource.get()));
                executeOpenServerTask();
            });

            createDataSourceTask.setOnFailed(event -> {
                DialogUtil.error("连接数据库失败", event.getSource().getException());
            });

            bindTaskState(createDataSourceTask);
            getContext().execute(createDataSourceTask);
        } else {
            executeOpenServerTask();
        }
    }

    private void bindTaskState(BackgroundTask<?> task) {
        if (taskRunning.isBound()) {
            taskRunning.unbind();
        }
        taskRunning.bind(task.runningProperty());
    }

    private void executeOpenServerTask() {
        OpenServerTask openServerTask = new OpenServerTask(metadataProvider.get());
        openServerTask.setOnSucceeded(event -> refreshData(openServerTask.getValue()));
        bindTaskState(openServerTask);
        getContext().execute(openServerTask);
    }

    private void refreshData(List<CatalogMetaData> value) {
        ObservableList<TreeItem> children = treeItem.getChildren();
        children.clear();
        for (CatalogMetaData metaData : value) {
            TreeItem item = new MySQLCatalogTreeItem(getContext(), metaData, metadataProvider.get());
            children.add(item);
        }
        treeItem.setExpanded(true);
    }

    @Override
    public void close() {

        // TODO 关闭从该item打开的tab

        for (Object child : treeItem.getChildren()) {
            if (child instanceof ICloseable) {
                ((ICloseable) child).close();
            }
        }
        treeItem.getChildren().clear();
        if (treeItem.isExpanded()) {
            treeItem.setExpanded(false);
        }
        DataSource source = dataSource.get();
        if (source != null) {
            if (source instanceof Closeable) {
                try {
                    ((Closeable) source).close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
            dataSource.set(null);
        }
    }

    @Override
    public void create() {

    }

    @Override
    public void copy() {

    }

    @Override
    public void delete() {
        close();
        treeItem.getParent().getChildren().remove(treeItem);
    }

    @Override
    public void properties() {

    }

    @Override
    public void commandLine() {

    }

    @Override
    public void executeSqlFile() {

    }

    @Override
    public void dataTransform() {

    }

    @Override
    public void refresh() {
        close();
        open();
    }


    private static class CreateDataSourceTask extends BackgroundTask<DataSource> {

        private final MySQLConnectionConfiguration configuration;

        public CreateDataSourceTask(MySQLConnectionConfiguration configuration) {
            super("连接数据库");
            this.configuration = configuration;
        }

        @Override
        protected DataSource doBackground() throws Exception {
            updateMessage("正在连接数据库");
            updateProgress(0, 1);
            HikariConfig config = new HikariConfig();
            config.setMaximumPoolSize(10);
            config.setJdbcUrl(configuration.getUrl());
            config.setUsername(configuration.getUsername());
            config.setPassword(configuration.getPassword());
            config.setConnectionTimeout(1000 * 5L);
            HikariDataSource dataSource = new HikariDataSource(config);
            updateMessage("连接数据库完成");
            updateProgress(1, 1);
            return dataSource;
        }
    }


    private static class OpenServerTask extends BackgroundTask<List<CatalogMetaData>> {


        SQLMetadataProvider metadataProvider;

        public OpenServerTask(SQLMetadataProvider metadataProvider) {
            super("打开数据库");
            this.metadataProvider = metadataProvider;
        }

        @Override
        protected List<CatalogMetaData> doBackground() throws Exception {
            updateMessage("正在获取数据库信息");
            updateProgress(0, 1);
            List<CatalogMetaData> catalogs = metadataProvider.getCatalogs();
            updateMessage("获取数据库信息完成");
            updateProgress(1, 1);
            return catalogs;
        }
    }
}
