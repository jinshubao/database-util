package com.jean.database.sql.view;

import com.jean.database.sql.meta.ColumnMetaData;
import javafx.scene.control.TableColumn;

import java.util.Map;

public class SelectionIndicatorColumn extends TableColumn<Map<String, String>, String> {

    private final ColumnMetaData columnMetaData;

    public SelectionIndicatorColumn(ColumnMetaData columnMetaData) {
        super(columnMetaData.getColumnName());
        this.columnMetaData = columnMetaData;
    }


}
