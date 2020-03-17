package com.jean.database.gui.handler.impl;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.KeyValuePairData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.factory.ActionLoggerWrapper;
import com.jean.database.gui.handler.IDataTableActionEventHandler;
import com.jean.database.gui.handler.ITableItemActionEventHandler;
import com.jean.database.gui.view.DataTableView;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TableItemActionEventHandlerImpl implements ITableItemActionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(TableItemActionEventHandlerImpl.class);

    private final IDataTableActionEventHandler dataTableActionEventHandler;
    private final TabPane tablePane;
    private final TabPane infoPane;
    private final TableView<KeyValuePairData> infoTableView;
    private final TextArea ddlTextArea;

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    public TableItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.dataTableActionEventHandler = ActionLoggerWrapper.warp(new DataTableActionEventHandlerImpl(connectionConfiguration, metadataProvider));
        this.tablePane = (TabPane) root.lookup("#tablePane");
        this.infoPane = (TabPane) root.lookup("#infoPane");
        this.infoTableView = (TableView<KeyValuePairData>) root.lookup("#infoTableView");
        this.ddlTextArea = (TextArea) root.lookup("#ddlTextArea");
    }

    @Override
    public void onCreate(TableTreeItem tableTreeItem) {
        //
    }

    @Override
    public void onOpen(TableTreeItem tableTreeItem) {
        TableMetaData tableMetaData = tableTreeItem.getTableMetaData();
        String fullName = tableMetaData.getFullName();
        Optional<Tab> tabOptional = tablePane.getTabs().stream().filter(tab -> fullName.equals(tab.getId())).findFirst();
        if (tabOptional.isPresent()) {
            Tab tab = tabOptional.get();
            tablePane.getSelectionModel().select(tab);
        } else {
            DataTableView tableView = new DataTableView(tableMetaData, dataTableActionEventHandler);
            Tab tab = new Tab(tableMetaData.getTableName(), tableView);
            tab.setTooltip(new Tooltip(fullName));
            tab.setId(fullName);
            tab.setOnClosed(event -> tableTreeItem.setOpen(false));
            tablePane.getTabs().add(tab);
            tablePane.getSelectionModel().select(tab);
            tableTreeItem.setOpen(true);
        }
    }

    @Override
    public void onClose(TableTreeItem tableTreeItem) {
        //
    }

    @Override
    public void onCopy(TableTreeItem tableTreeItem) {

    }

    @Override
    public void onDetails(TableTreeItem tableTreeItem) {
        //
    }

    @Override
    public void onDelete(TableTreeItem tableTreeItem) {
        tableTreeItem.setOpen(false);
        tableTreeItem.getParent().getChildren().remove(tableTreeItem);
    }

    @Override
    public void refresh(TableTreeItem treeItem) {
        TableMetaData tableMetaData = treeItem.getTableMetaData();
        String fullName = tableMetaData.getFullName();
        tablePane.getTabs().stream().filter(tab -> fullName.equals(tab.getId())).findFirst().ifPresent(tab -> {
            DataTableView content = (DataTableView) tab.getContent();
            int pageIndex = content.getPagination().getCurrentPageIndex();
            dataTableActionEventHandler.refresh(content, pageIndex);
        });
    }

    @Override
    public void onMouseClick(TableTreeItem tableTreeItem) {
    }

    @Override
    public void onMouseDoubleClick(TableTreeItem tableTreeItem) {
        this.onOpen(tableTreeItem);
    }

    @Override
    public void onSelected(TableTreeItem tableTreeItem) {
        infoTableView.getItems().clear();
        try (Connection connection = this.metadataProvider.getConnection(this.connectionConfiguration)) {
            TableMetaData tableMetaData = tableTreeItem.getTableMetaData();
            List<KeyValuePairData> tableDetails = metadataProvider.getTableDetails(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName(), new String[]{tableMetaData.getTableType()});
            if (tableDetails != null && !tableDetails.isEmpty()) {
                infoTableView.getItems().addAll(tableDetails);
            }
        } catch (SQLException e) {
            DialogUtil.error(e);
        }
    }
}
