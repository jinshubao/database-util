package com.jean.database.gui.view;

import com.jean.database.core.meta.ColumnMetaData;
import javafx.scene.control.TableColumn;

import java.util.Map;

public class DataColumn extends TableColumn<Map<String, Object>, Object> {

    private final ColumnMetaData columnMetaData;

    public DataColumn(ColumnMetaData columnMetaData) {
        super(columnMetaData.getColumnName());
        this.columnMetaData = columnMetaData;
    }


}
