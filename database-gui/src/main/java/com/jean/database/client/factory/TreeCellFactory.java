package com.jean.database.client.factory;

import com.jean.database.client.view.IContextMenu;
import com.jean.database.client.view.IDoubleClick;
import com.jean.database.client.view.IIcon;
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

        public MyTreeCell() {
            TreeItem<T> treeItem = getTreeItem();
            if (treeItem instanceof IContextMenu) {
                IContextMenu node = (IContextMenu) treeItem;
                setContextMenu(node.getContextMenu());
            }
            if (treeItem instanceof IDoubleClick) {
                IDoubleClick node = (IDoubleClick) treeItem;
                setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        node.doubleClick(event);
                    }
                });
            }
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.toString());
                TreeItem<T> treeItem = getTreeItem();
                if (treeItem instanceof IIcon) {
                    setGraphic(((IIcon) treeItem).getIcon());
                } else {
                    setGraphic(null);
                }
            }
        }
    }
}
