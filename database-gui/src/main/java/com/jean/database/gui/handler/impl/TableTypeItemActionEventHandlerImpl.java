package com.jean.database.gui.handler.impl;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.constant.TableType;
import com.jean.database.core.meta.TableSummaries;
import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TableTypeItemActionEventHandlerImpl implements ITableTypeItemActionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(TableTypeItemActionEventHandlerImpl.class);

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    private final TabPane tabPane;
    private final Tab objectTab;
    private final TableView<TableSummaries> objectTableView;


    public TableTypeItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.tabPane = (TabPane) root.lookup("#tablePane");
        this.objectTab = tabPane.getTabs().get(0);
        this.objectTableView = (TableView<TableSummaries>) objectTab.getContent();
    }

    @Override
    public void onMouseClick(TableTypeTreeItem tableTypeTreeItem) {

    }

    @Override
    public void onMouseDoubleClick(TableTypeTreeItem tableTypeTreeItem) {

    }

    @Override
    public void onSelected(TableTypeTreeItem tableTypeTreeItem) {
        objectTableView.getItems().clear();
        TableTypeMetaData tableTypeMetaData = tableTypeTreeItem.getValue();
        if (TableType.TABLE.equals(tableTypeMetaData.getTableType())) {
            try (Connection connection = this.metadataProvider.getConnection(this.connectionConfiguration)) {
                List<TableSummaries> tableSummaries = metadataProvider.getTableSummaries(connection, tableTypeMetaData.getTableCat(), tableTypeMetaData.getTableSchem(), null, null);
                if (tableSummaries != null && !tableSummaries.isEmpty()) {
                    objectTableView.getItems().addAll(tableSummaries);
                }
                tabPane.getSelectionModel().select(objectTab);
            } catch (SQLException e) {
                DialogUtil.error(e);
            }
        }
    }
}
