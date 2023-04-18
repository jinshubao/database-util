package com.jean.database.mysql.handler;

import com.jean.database.ability.IRefreshable;
import com.jean.database.context.ApplicationContext;
import com.jean.database.handler.AbstractActionHandler;
import com.jean.database.mysql.provider.MySQLMetadataProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;

public class ServerTreeItemActionHandler extends AbstractActionHandler implements IRefreshable {
    private final MySQLMetadataProvider metadataProvider;

    private final TreeItem<String> treeItem;

    private BooleanProperty isOpen = new SimpleBooleanProperty(false);

    public ServerTreeItemActionHandler(ApplicationContext context,
                                       MySQLMetadataProvider metadataProvider,
                                       TreeItem<String> treeItem) {
        super(context);
        this.metadataProvider = metadataProvider;
        this.treeItem = treeItem;
//        isOpen.bind(treeItem.expandedProperty());
    }


    public void openConnection(ActionEvent event) {
//        getContext().getBackgroundTaskManager().execute(new MySQLServerTreeItem.OpenServerTask("打开数据库连接"));
    }

    public void closeConnection(ActionEvent event) {
        this.isOpen.set(false);
    }

    public void copyConnection(ActionEvent event) {

    }

    public void deleteConnection(ActionEvent event) {

    }

    public void connectionProperties(ActionEvent event) {

    }

    public void createDatabase(ActionEvent event) {

    }

    public void commandLine(ActionEvent event) {

    }

    public void dataTransfer(ActionEvent event) {

    }

    @Override
    public void refresh() {

    }

    public void executeSql(ActionEvent event) {

    }


}
