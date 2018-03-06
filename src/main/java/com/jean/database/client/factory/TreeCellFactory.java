package com.jean.database.client.factory;

import com.jean.database.client.view.treeitem.AbstractTreeItem;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * @author jinshubao
 */
public class TreeCellFactory {

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView() {
        return param -> new MyTreeCell<>();
    }


    private static class MyTreeCell<T> extends TreeCell<T> {

        EventHandler<? super MouseEvent> eventHandler;

        @Override
        protected void updateItem(T item, boolean empty) {

            super.updateItem(item, empty);
            setContextMenu(null);
            if (eventHandler != null) {
                removeEventHandler(MouseEvent.ANY, eventHandler);
            }
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                TreeItem<T> treeItem = getTreeItem();
                if (treeItem instanceof AbstractTreeItem) {
                    AbstractTreeItem abstractTreeItem = (AbstractTreeItem) treeItem;
                    ContextMenu contextMenu = abstractTreeItem.createContextMenu();
                    if (contextMenu != null) {
                        setContextMenu(contextMenu);
                    }
                    eventHandler = abstractTreeItem::onMouseClick;
                    setOnMouseClicked(eventHandler);
                }
                setGraphic(treeItem.getGraphic());
                setText(item.toString());
            }
        }
    }
}
