package com.jean.database.mysql.handler;

import com.jean.database.context.ApplicationContext;
import com.jean.database.handler.AbstractActionHandler;
import com.jean.database.mysql.view.tab.MySQLDataTableTab;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.view.AbstractTreeItem;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DefaultMySQLTableTreeItemActionEventHandler extends AbstractActionHandler implements MySQLTableTreeItemActionEventHandler {

    private final AbstractTreeItem<TableMetaData> treeItem;

    private final SQLMetadataProvider metadataProvider;

    ObjectProperty<MySQLDataTableTab> sqlDataTableTab = new SimpleObjectProperty<>(null);

    private BooleanProperty openMenuDisable = new SimpleBooleanProperty(false);
    private BooleanProperty copyMenuDisable = new SimpleBooleanProperty(false);
    private BooleanProperty deleteMenuDisable = new SimpleBooleanProperty(false);
    private BooleanProperty refreshMenuDisable = new SimpleBooleanProperty(false);

    private BooleanProperty open = new SimpleBooleanProperty(false);
    private BooleanProperty taskRunning = new SimpleBooleanProperty(false);

    public DefaultMySQLTableTreeItemActionEventHandler(ApplicationContext context, AbstractTreeItem<TableMetaData> treeItem, SQLMetadataProvider metadataProvider) {
        super(context);
        this.treeItem = treeItem;
        this.metadataProvider = metadataProvider;

        open.bind(new BooleanBinding() {
            {
                bind(sqlDataTableTabProperty());
            }

            @Override
            protected boolean computeValue() {
                return sqlDataTableTabProperty().get() != null;
            }
        });
        openMenuDisableProperty().bind(taskRunning.or(open));
        copyMenuDisableProperty().bind(taskRunning);
        deleteMenuDisableProperty().bind(taskRunning);
        refreshMenuDisableProperty().bind(taskRunning);
    }

    public AbstractTreeItem<TableMetaData> getTreeItem() {
        return treeItem;
    }

    @Override
    public void refresh() {
        if (getSqlDataTableTab() != null) {
            getSqlDataTableTab().refresh();
        }
//        getContext().execute(new TableGeneralInfoTask("获取表信息"));
    }

    @Override
    public void click() {
//        getContext().getBackgroundTaskManager().execute(new TableGeneralInfoTask("获取表信息"));
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
        final MySQLDataTableTab sqlDataTableTab = getSqlDataTableTab();
        if (sqlDataTableTab == null) {
            TableMetaData tableMetaData = getTreeItem().getValue();
            MySQLDataTableTab tableTab = new MySQLDataTableTab(getContext(), tableMetaData, metadataProvider);
            setSqlDataTableTab(tableTab);
            tableTab.setOnClosed(event -> {
                getSqlDataTableTab().close();
                setSqlDataTableTab(null);
            });
            getContext().addObjectTab(tableTab);
        }
        refresh();
    }

    @Override
    public void copy() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void close() {
        getSqlDataTableTab().close();
        getContext().removeObjectTab(getSqlDataTableTab());
        sqlDataTableTab.set(null);
    }

    @Override
    public BooleanProperty openMenuDisableProperty() {
        return openMenuDisable;
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
    public BooleanProperty refreshMenuDisableProperty() {
        return refreshMenuDisable;
    }

    public MySQLDataTableTab getSqlDataTableTab() {
        return sqlDataTableTab.get();
    }

    public ObjectProperty<MySQLDataTableTab> sqlDataTableTabProperty() {
        return sqlDataTableTab;
    }

    public void setSqlDataTableTab(MySQLDataTableTab sqlDataTableTab) {
        this.sqlDataTableTab.set(sqlDataTableTab);
    }
}
