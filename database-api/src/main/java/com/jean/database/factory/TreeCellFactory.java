package com.jean.database.factory;

import com.jean.database.action.IContextMenu;
import com.jean.database.action.IMouseAction;
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
                setOnMouseClicked(null);
                setContextMenu(null);
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
                if (treeItem instanceof IMouseAction) {
                    IMouseAction node = (IMouseAction) treeItem;
                    this.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (event.getClickCount() == 1) {
                                node.click();
                            } else if (event.getClickCount() == 2) {
                                node.doubleClick();
                            }
                        }
                    });
                } else {
                    setOnMouseClicked(null);
                }
            }
        }
    }
}
