package com.jean.database.common.utils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NodeUtils {

    private NodeUtils() {

    }

    @SuppressWarnings("unchecked")
    public static <N extends Node> N lookup(SplitPane splitPane, String id) {
        ObservableList<Node> items = splitPane.getItems();
        for (Node node : items) {
            if (id.equals(node.getId())) {
                return (N) node;
            }
        }
        throw new RuntimeException("node [id='" + id + "'] not fund");
    }

    @SuppressWarnings("unchecked")
    public static <N extends Node> N lookup(Node parent, String selector) {
        return (N) parent.lookup(selector);
    }

    @SuppressWarnings("unchecked")
    public static <S, T> TableColumn<S, T> lookup(TableView<S> tableView, String columnId) {
        ObservableList<TableColumn<S, ?>> columns = tableView.getColumns();
        for (TableColumn<S, ?> column : columns) {
            if (columnId.equals(column.getId())) {
                return (TableColumn<S, T>) column;
            }
        }
        throw new RuntimeException("column [id='" + columnId + "'] not fund");
    }

}
