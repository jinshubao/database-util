package com.jean.database.sql.view.treeitem;


import com.jean.database.api.LoggerWrapper;
import com.jean.database.sql.SQLConnectionConfiguration;
import com.jean.database.sql.SQLMetadataProvider;
import com.jean.database.sql.meta.TableTypeMetaData;
import com.jean.database.sql.view.TreeItemViewContext;
import com.jean.database.sql.view.handler.ITableTypeItemActionEventHandler;
import com.jean.database.sql.view.handler.impl.TableTypeItemActionEventHandlerImpl;

public class TableTypeTreeItem extends BaseDatabaseItem<TableTypeMetaData> {

    private final ITableTypeItemActionEventHandler actionEventHandler;

    public TableTypeTreeItem(TableTypeMetaData value, TreeItemViewContext viewContext,
                             SQLConnectionConfiguration connectionConfiguration, SQLMetadataProvider metadataProvider) {
        super(value, viewContext, connectionConfiguration, metadataProvider);
        this.actionEventHandler = LoggerWrapper.warp(new TableTypeItemActionEventHandlerImpl());
    }

    @Override
    public void click() {
        this.actionEventHandler.onClick(this);
    }

    @Override
    public void doubleClick() {
        this.actionEventHandler.onDoubleClick(this);
    }

    @Override
    public void select() {
        this.actionEventHandler.onSelected(this);
    }

    @Override
    public void close() {
        actionEventHandler.onClose(this);
    }

}
