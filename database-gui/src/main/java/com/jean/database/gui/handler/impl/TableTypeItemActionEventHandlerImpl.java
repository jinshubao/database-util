package com.jean.database.gui.handler.impl;

import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.constant.TableType;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class TableTypeItemActionEventHandlerImpl implements ITableTypeItemActionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(TableTypeItemActionEventHandlerImpl.class);

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    private final TabPane tabPane;
    private final Tab objectTab;
    private final TableView<TableMetaData> objectTableView;


    public TableTypeItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.tabPane = (TabPane) root.lookup("#tablePane");
        this.objectTab = tabPane.getTabs().get(0);
        this.objectTableView = (TableView<TableMetaData>) objectTab.getContent();
    }

    @Override
    public void onMouseClick(TableTypeTreeItem tableTypeTreeItem) {

    }

    @Override
    public void onMouseDoubleClick(TableTypeTreeItem tableTypeTreeItem) {

    }

    @Override
    public void onSelected(TableTypeTreeItem tableTypeTreeItem) {
        if (!Objects.isNull(objectTab) && !Objects.isNull(objectTableView)) {
            objectTableView.getItems().clear();
            tabPane.getSelectionModel().select(objectTab);
            if (TableType.TABLE.equals(tableTypeTreeItem.getValue())) {
                List<TableMetaData> dataList = tableTypeTreeItem.getTableMetaDataList();
                if (dataList != null && !dataList.isEmpty()) {
                    objectTableView.getItems().addAll(dataList);
                }
            }
        }
    }
}
