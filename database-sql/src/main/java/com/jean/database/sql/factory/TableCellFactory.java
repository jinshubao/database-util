package com.jean.database.sql.factory;

import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.util.Map;

/**
 * @author jinshubao
 */
public class TableCellFactory {


    private static final Callback<TableColumn<Map<String, Object>, Object>, TableCell<Map<String, Object>, Object>> default_cell_factory = param -> new CustomTableCell<>();


    public static Callback<TableColumn<Map<String, Object>, Object>, TableCell<Map<String, Object>, Object>> forTableView() {
        return param -> new CustomTableCell<>();
    }


    private static class CustomTableCell<S, T> extends TableCell<S, T> {

        private final Paint defaultTextFill;

        public CustomTableCell() {
            super();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem copy = new MenuItem("复制");
            copy.setOnAction(event -> {
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(toString(getItem()));
                Clipboard.getSystemClipboard().setContent(clipboardContent);
            });
            contextMenu.getItems().addAll(copy);
            setContextMenu(contextMenu);
            defaultTextFill = getTextFill();
            setWrapText(false);
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (item == null) {
                    setTextFill(Color.LIGHTGRAY);
                    setText("NULL");
                } else {
                    if ("".equals(item)) {
                        setTextFill(Color.LIGHTGRAY);
                        setText("EMPTY");
                    } else {
                        setTextFill(defaultTextFill);
                        setText(toString(item));
                    }
                }
            }
        }

        private String toString(T item) {
            return item == null ? null : item.toString();
        }

        @Override
        public void startEdit() {
            super.startEdit();
        }

        @Override
        public void commitEdit(T newValue) {
            super.commitEdit(newValue);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
        }
    }
}
