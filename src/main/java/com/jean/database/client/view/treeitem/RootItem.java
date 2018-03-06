package com.jean.database.client.view.treeitem;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 * @author jinshubao
 */
public class RootItem extends TreeItem<String> {

    public RootItem() {
    }

    public RootItem(String value) {
        super(value);
    }

    public RootItem(String value, Node graphic) {
        super(value, graphic);
    }
}
