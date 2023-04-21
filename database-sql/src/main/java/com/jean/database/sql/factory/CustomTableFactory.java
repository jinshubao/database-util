package com.jean.database.sql.factory;

import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * @author jinshubao
 */
public class CustomTableFactory {


    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> tableCellFactory() {
        return param -> new CustomTableCell<>();
    }

    public static <T> Callback<TableView<T>, TableRow<T>> tableRowFactory() {
        return param -> new CustomTableRow<>();
    }

    private static class CustomTableCell<S, T> extends TableCell<S, T> {

        private static final String TABLE_CELL_EMPTY_CLASS = "table-cell-empty";
        private static final String TABLE_CELL_NULL_CLASS = "table-cell-null";


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
                        setText(toString(item));
                    }
                }
            }
            updateClass(item, empty);
        }

        private void updateClass(T item, boolean empty) {
            if (empty) {
                getStyleClass().removeAll(TABLE_CELL_EMPTY_CLASS, TABLE_CELL_NULL_CLASS);
            } else {
                if (item == null) {
                    getStyleClass().remove(TABLE_CELL_EMPTY_CLASS);
                    if (!getStyleClass().contains(TABLE_CELL_NULL_CLASS)) {
                        getStyleClass().add(TABLE_CELL_NULL_CLASS);
                    }
                } else if ("".equals(item)) {
                    getStyleClass().remove(TABLE_CELL_NULL_CLASS);
                    if (!getStyleClass().contains(TABLE_CELL_EMPTY_CLASS)) {
                        getStyleClass().add(TABLE_CELL_EMPTY_CLASS);
                    }
                } else {
                    getStyleClass().removeAll(TABLE_CELL_EMPTY_CLASS, TABLE_CELL_NULL_CLASS);
                }
            }

        }

        private String toString(T item) {
            return item == null ? null : item.toString();
        }

    }


    private static class CustomTableRow<T> extends TableRow<T> {

        public CustomTableRow() {
            super();
            setOnMouseDragged(mouseEvent -> {
                Object source = mouseEvent.getSource();
                if (source instanceof TableRow){
                    getTableView().getSelectionModel().select(((TableRow<?>) source).getIndex());
                }
            });
            setOnMouseDragEntered(mouseEvent -> {
                Object source = mouseEvent.getSource();
                if (source instanceof TableRow){
                    getTableView().getSelectionModel().select(((TableRow<?>) source).getIndex());
                }
            });
        }
    }
}
