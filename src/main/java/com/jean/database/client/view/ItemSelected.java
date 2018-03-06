package com.jean.database.client.view;

import com.jean.database.client.view.treeitem.AbstractTreeItem;

/**
 * @author jinshubao
 */
public interface ItemSelected {

    /**
     * 当前item被选中
     */
    void setSelected();

    void onSelected(AbstractTreeItem selectedItemValue);

}
