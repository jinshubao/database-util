package com.jean.database.gui.factory;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;

/**
 * @author jinshubao
 */
public class TableCellFactory {

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableView() {
        return param -> new CustomTableCell<>();
    }


    private static class CustomTableCell<S, T> extends TableCell<S, T> {

        public CustomTableCell() {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem copy = new MenuItem("复制");
            copy.setOnAction(event -> {
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(toString(getItem()));
                Clipboard.getSystemClipboard().setContent(clipboardContent);
            });
            contextMenu.getItems().addAll(copy);
            setContextMenu(contextMenu);
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(toString(item));
            }

        }

        private String toString(T item) {
            return item == null ? null : item.toString();
        }
    }
}
