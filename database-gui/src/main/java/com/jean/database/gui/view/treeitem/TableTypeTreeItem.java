package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.action.IMouseClickAction;
import com.jean.database.gui.view.action.ISelectAction;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class TableTypeTreeItem extends TreeItem<String> implements IMouseClickAction, ISelectAction {

    private List<TableMetaData> tableMetaDataList;

    private final ITableTypeItemActionEventHandler tableTypeItemActionEventHandler;

    public TableTypeTreeItem(String value, List<TableMetaData> tableMetaDataList, ITableTypeItemActionEventHandler tableTypeItemActionEventHandler) {
        super(value);
        this.tableMetaDataList = tableMetaDataList;
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

    public List<TableMetaData> getTableMetaDataList() {
        return tableMetaDataList;
    }

    @Override
    public void selected() {
        tableTypeItemActionEventHandler.onSelected(this);
    }
}
