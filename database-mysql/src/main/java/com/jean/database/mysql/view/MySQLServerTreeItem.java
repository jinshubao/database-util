package com.jean.database.mysql.view;

import com.jean.database.context.ApplicationContext;
import com.jean.database.mysql.config.MySQLConnectionConfiguration;
import com.jean.database.mysql.provider.MySQLMetadataProvider;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.item.SQLDatabaseItem;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.task.BackgroundTask;
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
import java.util.List;


public class MySQLServerTreeItem extends SQLDatabaseItem<String> {

    private final MySQLConnectionConfiguration configuration;

    private ObjectProperty<DataSource> dataSource = new SimpleObjectProperty<>(null, "dataSource");

    private final ContextMenu contextMenu;


    private BooleanProperty connected = new SimpleBooleanProperty(false);

    private final OpenServerTask openServerTask;

    private SQLMetadataProvider metadataProvider;

    private BooleanProperty openMenuDisable = new SimpleBooleanProperty(false);


    public MySQLServerTreeItem(ApplicationContext context, MySQLConnectionConfiguration configuration) {
        super(context, configuration.getConnectionName(), ImageUtils.createImageView("/mysql/mysql.png"));
        this.configuration = configuration;
        this.openServerTask = new OpenServerTask(configuration);
        this.contextMenu = createContextMenu();
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


    OpenServerTask createOpenServerTask() {
        OpenServerTask task = new OpenServerTask(configuration);
        task.setOnSucceeded(e -> {
            refreshData((List<CatalogMetaData>) e.getSource().getValue());
            setOpen(true);
        });
        return task;
    }

    @Override
    public void doubleClick() {
        if (!isOpen()) {
            this.open();
        }
    }


    @Override
    public void refresh() {
        super.refresh();
        this.close();
        this.open();
    }

    private void delete() {
        this.close();
        getContext().removeDatabaseItem(this);
    }

    private void open() {
        OpenServerTask task = createOpenServerTask();
        if (openMenuDisable.isBound()) {
            openMenuDisable.unbind();
        }
        openMenuDisable.bind(task.runningProperty().or(connectedProperty()));
        getContext().execute(task);
    }


    @Override
    public void close() {
        try {
            super.close();
            if (dataSource.get() instanceof Closeable) {
                ((Closeable) dataSource.get()).close();
            }
        } catch (Exception e) {
            //
        } finally {
            metadataProvider = null;
            dataSource.set(null);
        }
    }

    private void refreshData(List<CatalogMetaData> value) {
        ObservableList<TreeItem<String>> children = this.getChildren();
        children.clear();
        for (CatalogMetaData metaData : value) {
            TreeItem item = new MySQLCatalogTreeItem(getContext(), metaData, metadataProvider);
            this.getChildren().add(item);
        }
        this.setExpanded(true);
        openProperty().set(true);

    }

    @Override
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    private ContextMenu createContextMenu() {
        MenuItem open = new MenuItem("打开连接", ImageUtils.createImageView("/image/connect.png"));
        open.disableProperty().bind(openMenuDisable);
        open.setOnAction(event -> open());

        MenuItem close = new MenuItem("关闭连接", ImageUtils.createImageView("/image/disconnect.png"));
        close.disableProperty().bind(connectedProperty().not());
        close.setOnAction(event -> close());

        MenuItem copy = new MenuItem("复制连接...", ImageUtils.createImageView("/image/copy.png"));
        copy.setOnAction(event -> {
        });

        MenuItem delete = new MenuItem("删除连接", ImageUtils.createImageView("/image/delete.png"));
        delete.setOnAction(event -> delete());

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
        refresh.setOnAction(event -> refresh());

        return new ContextMenu(open, close, new SeparatorMenuItem(),
                copy, delete, properties, new SeparatorMenuItem(),
                commandLine, executeSqlFile, dataTransform, new SeparatorMenuItem(),
                refresh);
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }

    public ObjectProperty<DataSource> dataSourceProperty() {
        return dataSource;
    }

    private class OpenServerTask extends BackgroundTask<List<CatalogMetaData>> {

        private final MySQLConnectionConfiguration configuration;

        public OpenServerTask(MySQLConnectionConfiguration configuration) {
            super("打开数据库");
            this.configuration = configuration;
        }

        @Override
        protected List<CatalogMetaData> doBackground() throws Exception {
            long totalWork = dataSourceProperty().get() == null ? 2 : 1;
            long workDone = 0;
            if (dataSourceProperty().get() == null) {
                updateMessage("正在连接数据库");
                updateProgress(workDone, totalWork);
                HikariConfig config = new HikariConfig();
                config.setMaximumPoolSize(10);
                config.setJdbcUrl(configuration.getUrl());
                config.setUsername(configuration.getUsername());
                config.setPassword(configuration.getPassword());
                config.setConnectionTimeout(1000 * 5L);
                HikariDataSource dataSource = new HikariDataSource(config);
                dataSourceProperty().set(dataSource);
                metadataProvider = new MySQLMetadataProvider(dataSource);
                workDone++;
                updateMessage("连接数据库完成");
                updateProgress(workDone, totalWork);
            }
            updateMessage("正在获取数据库信息");
            updateProgress(workDone, totalWork);
            List<CatalogMetaData> catalogs = metadataProvider.getCatalogs();
            workDone++;
            updateMessage("获取数据库信息完成");
            updateProgress(workDone, totalWork);
            return catalogs;
        }
    }


}
