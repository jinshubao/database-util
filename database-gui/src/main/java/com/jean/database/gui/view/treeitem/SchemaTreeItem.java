package com.jean.database.gui.view.treeitem;

import com.jean.database.core.meta.SchemaMetaData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;

public class SchemaTreeItem extends TreeItem<SchemaMetaData> {

    private final BooleanProperty open = new SimpleBooleanProperty(this, "open", false);

    private final SchemaMetaData schemaMetaData;

    public SchemaTreeItem(SchemaMetaData schemaMetaData) {
        super(schemaMetaData);
        this.schemaMetaData = schemaMetaData;
    }

    public SchemaMetaData getSchemaMetaData() {
        return schemaMetaData;
    }

    public boolean isOpen() {
        return open.get();
    }

    public BooleanProperty openProperty() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }
}
