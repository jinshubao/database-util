package com.jean.database.sql.view;

import com.jean.database.sql.factory.TableCellFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用选择表格，封装序号列、行/列选择、复制等交互行为。
 *
 * @param <T> 行数据类型
 */
public class SelectableTableView<T> extends TableView<T> {

    private static final Logger logger = LoggerFactory.getLogger(SelectableTableView.class);

    private final TableColumn<T, Number> indexColumn;

    // Shift 连续选择的锚点
    private int anchorRow = -1;
    private int anchorCol = -1;

    public SelectableTableView() {
        setEditable(true);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectionModel().setCellSelectionEnabled(true);

        indexColumn = createIndexColumn();
        getColumns().add(indexColumn);

        setupDragSelection();
        setupColumnHeaderClick();
        setupCopyShortcut();
    }

    private TableColumn<T, Number> createIndexColumn() {
        TableColumn<T, Number> col = new TableColumn<>("#");
        col.setSortable(false);
        col.setResizable(false);
        col.setPrefWidth(50);
        col.setEditable(false);
        col.setCellValueFactory(param -> new SimpleObjectProperty<>(0));
        col.setCellFactory(column -> {
            TableCell<T, Number> cell = new TableCell<>() {
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(String.valueOf(getIndex() + 1));
                    }
                }
            };
            return cell;
        });
        return col;
    }

    public TableColumn<T, Number> getIndexColumn() {
        return indexColumn;
    }

    public boolean isIndexColumn(TableColumn<?, ?> column) {
        return column == indexColumn;
    }

    private void setupDragSelection() {
        final TablePosition<T, ?>[] dragStart = new TablePosition[1];
        final boolean[] isIndexDrag = new boolean[1];

        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;
            TablePosition<T, ?> pos = getTablePosition(event);
            dragStart[0] = pos;
            isIndexDrag[0] = pos != null && isIndexColumn(pos.getTableColumn());
            if (isIndexDrag[0]) {
                int row = pos.getRow();
                if (row >= 0 && row < getItems().size()) {
                    if (event.isShiftDown() && anchorRow >= 0) {
                        // Shift+点击序号列：从锚点到当前行连续选择
                        getSelectionModel().clearSelection();
                        int minRow = Math.min(anchorRow, row);
                        int maxRow = Math.max(anchorRow, row);
                        for (int r = minRow; r <= maxRow; r++) {
                            selectRow(r);
                        }
                    } else if (event.isControlDown()) {
                        if (isRowSelected(row)) {
                            deselectRow(row);
                        } else {
                            selectRow(row);
                        }
                        anchorRow = row;
                        anchorCol = -1;
                    } else {
                        getSelectionModel().clearSelection();
                        selectRow(row);
                        anchorRow = row;
                        anchorCol = -1;
                    }
                }
                event.consume();
            }
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.isPrimaryButtonDown() && dragStart[0] != null && isIndexDrag[0]) {
                TablePosition<T, ?> pos = getTablePosition(event);
                if (pos != null) {
                    if (!event.isControlDown()) {
                        getSelectionModel().clearSelection();
                    }
                    int fromRow = dragStart[0].getRow();
                    int toRow = pos.getRow();
                    int minRow = Math.min(fromRow, toRow);
                    int maxRow = Math.max(fromRow, toRow);
                    for (int r = minRow; r <= maxRow; r++) {
                        if (r >= 0 && r < getItems().size()) {
                            selectRow(r);
                        }
                    }
                    event.consume();
                }
            }
        });

        setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                TablePosition<T, ?> pos = getTablePosition(event);
                dragStart[0] = pos;
                isIndexDrag[0] = pos != null && isIndexColumn(pos.getTableColumn());
                if (!isIndexDrag[0]) {
                    if (pos != null && event.isShiftDown()
                            && anchorRow >= 0 && anchorCol >= 0) {
                        // Shift+点击数据列：从锚点到当前位置矩形选择
                        getSelectionModel().clearSelection();
                        int minRow = Math.min(anchorRow, pos.getRow());
                        int maxRow = Math.max(anchorRow, pos.getRow());
                        int minCol = Math.min(anchorCol, getColumns().indexOf(pos.getTableColumn()));
                        int maxCol = Math.max(anchorCol, getColumns().indexOf(pos.getTableColumn()));
                        for (int r = minRow; r <= maxRow; r++) {
                            for (int c = minCol; c <= maxCol; c++) {
                                TableColumn<T, ?> col = getColumns().get(c);
                                if (isIndexColumn(col)) continue;
                                getSelectionModel().select(r, col);
                            }
                        }
                    } else {
                        getSelectionModel().clearSelection();
                        if (pos != null) {
                            getSelectionModel().select(pos.getRow(), pos.getTableColumn());
                            anchorRow = pos.getRow();
                            anchorCol = getColumns().indexOf(pos.getTableColumn());
                        }
                    }
                }
            }
        });

        setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown() && dragStart[0] != null && !isIndexDrag[0]) {
                TablePosition<T, ?> pos = getTablePosition(event);
                if (pos != null) {
                    getSelectionModel().clearSelection();
                    int minRow = Math.min(dragStart[0].getRow(), pos.getRow());
                    int maxRow = Math.max(dragStart[0].getRow(), pos.getRow());
                    int startCol = getColumns().indexOf(dragStart[0].getTableColumn());
                    int endCol = getColumns().indexOf(pos.getTableColumn());
                    int minCol = Math.min(startCol, endCol);
                    int maxCol = Math.max(startCol, endCol);
                    for (int r = minRow; r <= maxRow; r++) {
                        for (int c = minCol; c <= maxCol; c++) {
                            TableColumn<T, ?> col = getColumns().get(c);
                            if (isIndexColumn(col)) continue;
                            getSelectionModel().select(r, col);
                        }
                    }
                }
            }
        });
    }

    private boolean isRowSelected(int row) {
        if (getColumns().size() <= 1) return false;
        return getSelectionModel().isSelected(row, getColumns().get(1));
    }

    private void selectRow(int row) {
        for (int i = 1; i < getColumns().size(); i++) {
            getSelectionModel().select(row, getColumns().get(i));
        }
    }

    private void deselectRow(int row) {
        for (int i = 1; i < getColumns().size(); i++) {
            getSelectionModel().clearSelection(row, getColumns().get(i));
        }
    }

    @SuppressWarnings("unchecked")
    private TablePosition<T, ?> getTablePosition(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        while (node != null && !(node instanceof TableCell)) {
            node = node.getParent();
        }
        if (node instanceof TableCell) {
            TableCell<T, ?> cell = (TableCell<T, ?>) node;
            return new TablePosition<>(this, cell.getIndex(), cell.getTableColumn());
        }
        return null;
    }

    // 表头选择的锚点列索引
    private int anchorHeaderCol = -1;

    private void setupColumnHeaderClick() {
        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;
            Node node = event.getPickResult().getIntersectedNode();
            while (node != null) {
                String className = node.getClass().getName();
                if (className.contains("TableColumnHeader")) {
                    try {
                        Object columnObj = node.getClass().getMethod("getTableColumn").invoke(node);
                        if (columnObj instanceof TableColumn) {
                            @SuppressWarnings("unchecked")
                            TableColumn<T, ?> column = (TableColumn<T, ?>) columnObj;
                            if (isIndexColumn(column)) return;

                            int colIndex = getColumns().indexOf(column);
                            boolean columnSelected = false;
                            if (!getItems().isEmpty()) {
                                columnSelected = getSelectionModel().isSelected(0, column);
                            }

                            if (event.isShiftDown() && anchorHeaderCol >= 0) {
                                // Shift+点击表头：从锚点列到当前列连续选择
                                getSelectionModel().clearSelection();
                                int minCol = Math.min(anchorHeaderCol, colIndex);
                                int maxCol = Math.max(anchorHeaderCol, colIndex);
                                for (int c = minCol; c <= maxCol; c++) {
                                    TableColumn<T, ?> col = getColumns().get(c);
                                    if (isIndexColumn(col)) continue;
                                    for (int r = 0; r < getItems().size(); r++) {
                                        getSelectionModel().select(r, col);
                                    }
                                }
                            } else if (event.isControlDown()) {
                                if (columnSelected) {
                                    for (int r = 0; r < getItems().size(); r++) {
                                        getSelectionModel().clearSelection(r, column);
                                    }
                                } else {
                                    for (int r = 0; r < getItems().size(); r++) {
                                        getSelectionModel().select(r, column);
                                    }
                                }
                                anchorHeaderCol = colIndex;
                            } else {
                                getSelectionModel().clearSelection();
                                for (int r = 0; r < getItems().size(); r++) {
                                    getSelectionModel().select(r, column);
                                }
                                anchorHeaderCol = colIndex;
                            }
                            event.consume();
                        }
                    } catch (Exception e) {
                        logger.error("Failed to handle column header click", e);
                    }
                    return;
                }
                node = node.getParent();
            }
        });
    }

    private void setupCopyShortcut() {
        setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.C) {
                TableCellFactory.copySelectedCellsAsCsv(this);
                event.consume();
            }
        });
    }
}
