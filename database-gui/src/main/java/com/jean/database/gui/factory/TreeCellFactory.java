package com.jean.database.gui.factory;

import com.jean.database.gui.view.action.IContextMenu;
import com.jean.database.gui.view.action.IMouseClickAction;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

/**
 * @author jinshubao
 */
public class TreeCellFactory {

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView() {
        return param -> new MyTreeCell<>();
    }

    private static class MyTreeCell<T> extends TreeCell<T> {

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.toString());
                TreeItem<T> treeItem = getTreeItem();
                setGraphic(treeItem.getGraphic());
                if (treeItem instanceof IContextMenu) {
                    IContextMenu node = (IContextMenu) treeItem;
                    setContextMenu(node.getContextMenu());
                } else {
                    setContextMenu(null);
                }

                setOnMouseClicked(event -> {
                    if (treeItem instanceof IMouseClickAction) {
                        IMouseClickAction node = (IMouseClickAction) treeItem;
                        if (event.getButton() == MouseButton.PRIMARY) {
                            node.click(event);
                        }
                    }
                });
            }
        }
    }
}
