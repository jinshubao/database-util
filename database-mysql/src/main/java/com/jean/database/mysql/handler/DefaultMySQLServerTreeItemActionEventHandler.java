package com.jean.database.mysql.handler;

import com.jean.database.context.ApplicationContext;
import com.jean.database.handler.AbstractActionHandler;
import com.jean.database.mysql.config.MySQLConnectionConfiguration;
import com.jean.database.mysql.provider.MySQLMetadataProvider;
import com.jean.database.mysql.task.CreateDataSourceTask;
import com.jean.database.mysql.task.OpenServerTask;
import com.jean.database.mysql.view.item.MySQLCatalogTreeItem;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.task.BackgroundTask;
import com.jean.database.utils.DialogUtil;
import com.jean.database.view.AbstractTreeItem;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class DefaultMySQLServerTreeItemActionEventHandler extends AbstractActionHandler implements MySQLServerTreeItemActionEventHandler {

    private final AbstractTreeItem<MySQLConnectionConfiguration> treeItem;
    private ObjectProperty<DataSource> dataSource = new SimpleObjectProperty<>(null, "dataSource");
    private ObjectProperty<SQLMetadataProvider> metadataProvider = new SimpleObjectProperty<>(null, "metadataProvider");

    private BooleanProperty taskRunning = new SimpleBooleanProperty(false, "taskRunning");
    private BooleanProperty connected = new SimpleBooleanProperty(false, "connected");
    private BooleanProperty openMenuDisable = new SimpleBooleanProperty(false, "openMenuDisable");
    private BooleanProperty closeMenuDisable = new SimpleBooleanProperty(false, "closeMenuDisable");
    private BooleanProperty createMenuDisable = new SimpleBooleanProperty(false, "createMenuDisable");
    private BooleanProperty copyMenuDisable = new SimpleBooleanProperty(false, "copyMenuDisable");
    private BooleanProperty deleteMenuDisable = new SimpleBooleanProperty(false, "deleteMenuDisable");
    private BooleanProperty propertiesMenuDisable = new SimpleBooleanProperty(false, "propertiesMenuDisable");
    private BooleanProperty commandLineMenuDisable = new SimpleBooleanProperty(false, "commandLineMenuDisable");

    private BooleanProperty executeSqlFileMenuDisable = new SimpleBooleanProperty(false, "executeSqlFileMenuDisable");

    private BooleanProperty dataTransformMenuDisable = new SimpleBooleanProperty(false, "dataTransformMenuDisable");
    private BooleanProperty refreshMenuDisable = new SimpleBooleanProperty(false, "refreshMenuDisable");


    public DefaultMySQLServerTreeItemActionEventHandler(ApplicationContext context, AbstractTreeItem<MySQLConnectionConfiguration> treeItem) {
        super(context);
        this.treeItem = treeItem;

        connected.bind(new BooleanBinding() {
            {
                bind(dataSource);
            }

            @Override
            protected boolean computeValue() {
                return dataSource.get() != null;
            }
        });
        openMenuDisable.bind(taskRunning.or(connected));
        closeMenuDisable.bind(taskRunning.or(connected.not()));
        createMenuDisable.bind(taskRunning.or(connected.not()));
        copyMenuDisable.bind(taskRunning.or(connected.not()));
        deleteMenuDisable.bind(taskRunning.or(connected.not()));
        propertiesMenuDisable.bind(taskRunning.or(connected.not()));
        commandLineMenuDisable.bind(taskRunning.or(connected.not()));
        executeSqlFileMenuDisable.bind(taskRunning.or(connected.not()));
        dataTransformMenuDisable.bind(taskRunning.or(connected.not()));
        refreshMenuDisable.bind(taskRunning.or(connected.not()));
    }

    private void executeOpenServerTask() {
        OpenServerTask openServerTask = new OpenServerTask(metadataProvider.get());
        openServerTask.setOnSucceeded(event -> refreshData(openServerTask.getValue()));
        bindTaskState(openServerTask);
        getContext().execute(openServerTask);
    }

    private void refreshData(List<CatalogMetaData> value) {
        TreeItem treeItem = getTreeItem();
        ObservableList<TreeItem> children = treeItem.getChildren();
        children.clear();
        for (CatalogMetaData metaData : value) {
            MySQLCatalogTreeItem item = new MySQLCatalogTreeItem(metaData);
            DefaultMySQLCatalogTreeItemActionEventHandler eventHandler = new DefaultMySQLCatalogTreeItemActionEventHandler(getContext(), item, metadataProvider.get());
            item.setItemActionEventHandler(eventHandler);
            children.add(item);
        }
        treeItem.setExpanded(true);
    }

    private void bindTaskState(BackgroundTask<?> task) {
        if (taskRunning.isBound()) {
            taskRunning.unbind();
        }
        taskRunning.bind(task.runningProperty());
    }

    @Override
    public AbstractTreeItem<MySQLConnectionConfiguration> getTreeItem() {
        return treeItem;
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
            CreateDataSourceTask createDataSourceTask = new CreateDataSourceTask((MySQLConnectionConfiguration) getTreeItem().getValue());
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

    @Override
    public void close() {

        try {
            DataSource source = dataSource.get();
            if (source != null) {
                if (source instanceof Closeable) {
                    ((Closeable) source).close();
                }
                dataSource.set(null);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        AbstractTreeItem<MySQLConnectionConfiguration> treeItem = getTreeItem();
        treeItem.close();
        if (treeItem.isExpanded()) {
            treeItem.setExpanded(false);
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
        TreeItem treeItem1 = getTreeItem();
        treeItem1.getParent().getChildren().remove(treeItem1);
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

    @Override
    public BooleanProperty openMenuDisableProperty() {
        return openMenuDisable;
    }

    @Override
    public BooleanProperty closeMenuDisableProperty() {
        return closeMenuDisable;
    }

    @Override
    public BooleanProperty createMenuDisableProperty() {
        return createMenuDisable;
    }

    @Override
    public BooleanProperty copyMenuDisableProperty() {
        return copyMenuDisable;
    }

    @Override
    public BooleanProperty deleteMenuDisableProperty() {
        return deleteMenuDisable;
    }

    @Override
    public BooleanProperty propertiesMenuDisableProperty() {
        return propertiesMenuDisable;
    }

    @Override
    public BooleanProperty commandLineMenuDisableProperty() {
        return commandLineMenuDisable;
    }

    @Override
    public BooleanProperty executeSqlFileMenuDisableProperty() {
        return executeSqlFileMenuDisable;
    }

    @Override
    public BooleanProperty dataTransformMenuDisableProperty() {
        return dataTransformMenuDisable;
    }

    @Override
    public BooleanProperty refreshMenuDisableProperty() {
        return refreshMenuDisable;
    }


}
