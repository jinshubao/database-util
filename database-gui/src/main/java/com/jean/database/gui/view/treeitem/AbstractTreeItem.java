package com.jean.database.gui.view.treeitem;

import com.jean.database.gui.view.action.IMouseAction;
import com.jean.database.gui.view.handler.IMouseEventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public abstract class AbstractTreeItem<T> extends TreeItem<T> implements IMouseAction {

    private IMouseEventHandler mouseClickEventHandler;

    public AbstractTreeItem(T value, IMouseEventHandler mouseClickEventHandler) {
        super(value);
        this.mouseClickEventHandler = mouseClickEventHandler;
    }


    @Override
    public void click(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            if (event.getClickCount() == 1) {
                this.mouseClickEventHandler.onMouseClick(this);
            } else if (event.getClickCount() == 2) {
                this.mouseClickEventHandler.onMouseDoubleClick(this);
            }
        }
    }

    @Override
    public void select() {
        this.mouseClickEventHandler.onSelected(this);
    }
}
