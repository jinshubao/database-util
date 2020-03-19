package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.view.action.IMouseAction;
import com.jean.database.gui.view.handler.ITableTypeItemActionEventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

public class TableTypeTreeItem extends TreeItem<TableTypeMetaData> implements IMouseAction {

    private final ITableTypeItemActionEventHandler tableTypeItemActionEventHandler;

    private final TableTypeMetaData tableTypeMetaData;

    public TableTypeTreeItem(TableTypeMetaData tableTypeMetaData, ITableTypeItemActionEventHandler tableTypeItemActionEventHandler) {
        super(tableTypeMetaData);
        this.tableTypeMetaData = tableTypeMetaData;
        this.tableTypeItemActionEventHandler = tableTypeItemActionEventHandler;
    }

    @Override
    public void click(MouseEvent event) {
        if (event.getClickCount() == 1) {
            this.tableTypeItemActionEventHandler.onMouseClick(this);
        } else if (event.getClickCount() == 2) {
            this.tableTypeItemActionEventHandler.onMouseDoubleClick(this);
        }
    }

    @Override
    public void select() {
        this.tableTypeItemActionEventHandler.onSelected(this);
    }

    public TableTypeMetaData getTableTypeMetaData() {
        return this.tableTypeMetaData;
    }
}
