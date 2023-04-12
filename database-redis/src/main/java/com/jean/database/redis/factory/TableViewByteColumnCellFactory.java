package com.jean.database.redis.factory;


import com.jean.database.utils.StringUtils;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * @author jinshubao
 * @date 2016/11/25
 */
public class TableViewByteColumnCellFactory<T> implements Callback<TableColumn<T, byte[]>, TableCell<T, byte[]>> {

    @Override
    public TableCell<T, byte[]> call(TableColumn<T, byte[]> p) {
        return new KeyTableCell<>();
    }

    private static class KeyTableCell<T> extends TableCell<T, byte[]> {

        @Override
        protected void updateItem(byte[] item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(StringUtils.byteArrayToString(item));
            }
        }
    }
}
