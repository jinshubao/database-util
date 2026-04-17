package com.jean.database.sql.factory;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
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


    private static final Callback<TableColumn<Map<String, ObjectProperty>, ObjectProperty>, TableCell<Map<String, ObjectProperty>, ObjectProperty>> default_cell_factory = param -> new CustomTableCell<>();


    public static Callback<TableColumn<Map<String, ObjectProperty>, ObjectProperty>, TableCell<Map<String, ObjectProperty>, ObjectProperty>> forTableView() {
        return default_cell_factory;
    }


    public static void copySelectedCellsAsCsv(TableView<?> tableView) {
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        if (selectedCells.isEmpty()) {
            return;
        }

        int minRow = selectedCells.stream().mapToInt(TablePosition::getRow).min().orElse(0);
        int maxRow = selectedCells.stream().mapToInt(TablePosition::getRow).max().orElse(0);
        int minCol = selectedCells.stream().mapToInt(p -> tableView.getColumns().indexOf(p.getTableColumn())).min().orElse(0);
        int maxCol = selectedCells.stream().mapToInt(p -> tableView.getColumns().indexOf(p.getTableColumn())).max().orElse(0);

        StringBuilder csv = new StringBuilder();
        for (int r = minRow; r <= maxRow; r++) {
            for (int c = minCol; c <= maxCol; c++) {
                TableColumn<?, ?> column = tableView.getColumns().get(c);
                Object cellData = column.getCellData(r);
                String text = cellDataToString(cellData);
                text = escapeCsv(text);
                csv.append(text);
                if (c < maxCol) {
                    csv.append(",");
                }
            }
            if (r < maxRow) {
                csv.append("\n");
            }
        }

        ClipboardContent content = new ClipboardContent();
        content.putString(csv.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    private static String cellDataToString(Object cellData) {
        if (cellData == null) {
            return "";
        }
        if (cellData instanceof javafx.beans.value.ObservableValue) {
            Object value = ((javafx.beans.value.ObservableValue<?>) cellData).getValue();
            return value == null ? "" : value.toString();
        }
        return cellData.toString();
    }

    private static String escapeCsv(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        if (text.contains(",") || text.contains("\"") || text.contains("\n") || text.contains("\r")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
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
            MenuItem copySelection = new MenuItem("复制选中区域");
            copySelection.setOnAction(event -> {
                TableView<?> tableView = getTableView();
                if (tableView != null) {
                    copySelectedCellsAsCsv(tableView);
                }
            });
            defaultTextFill = getTextFill();

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
                        setWrapText(false);
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
