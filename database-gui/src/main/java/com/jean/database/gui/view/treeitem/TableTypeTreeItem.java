package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.action.IMouseClickAction;
import com.jean.database.gui.view.action.ISelectAction;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

public class TableTypeTreeItem extends TreeItem<TableTypeMetaData> implements IMouseClickAction, ISelectAction {

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
            tableTypeItemActionEventHandler.onMouseClick(this);
        } else if (event.getClickCount() == 2) {
            tableTypeItemActionEventHandler.onMouseDoubleClick(this);
        }
    }

    @Override
    public void selected() {
        tableTypeItemActionEventHandler.onSelected(this);
    }

    public TableTypeMetaData getTableTypeMetaData() {
        return tableTypeMetaData;
    }
}
