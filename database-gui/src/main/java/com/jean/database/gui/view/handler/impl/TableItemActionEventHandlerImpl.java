package com.jean.database.gui.view.handler.impl;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.KeyValuePairData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.factory.ActionLoggerWrapper;
import com.jean.database.gui.view.handler.AbstractEventHandler;
import com.jean.database.gui.view.handler.IDataTableActionEventHandler;
import com.jean.database.gui.view.handler.ITableItemActionEventHandler;
import com.jean.database.gui.view.DataTableView;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TableItemActionEventHandlerImpl extends AbstractEventHandler<TableTreeItem> implements ITableItemActionEventHandler {


    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    private final IDataTableActionEventHandler dataTableActionEventHandler;
    private final TabPane tablePane;
    private final TableView<KeyValuePairData> infoTableView;
    private final TextArea ddlTextArea;


    public TableItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        super(root);
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.dataTableActionEventHandler = ActionLoggerWrapper.warp(new DataTableActionEventHandlerImpl(connectionConfiguration, metadataProvider, root));
        this.tablePane = this.lookup("#tablePane");
        this.infoTableView = this.lookup("#infoTableView");
        this.ddlTextArea = this.lookup("#ddlTextArea");
    }

    @Override
    public void onOpen(TableTreeItem tableTreeItem) {
        TableMetaData tableMetaData = tableTreeItem.getTableMetaData();
        String fullName = tableMetaData.getFullName();
        Optional<Tab> tabOptional = this.tablePane.getTabs().stream().filter(tab -> fullName.equals(tab.getId())).findFirst();
        if (tabOptional.isPresent()) {
            Tab tab = tabOptional.get();
            this.tablePane.getSelectionModel().select(tab);
        } else {
            DataTableView tableView = new DataTableView(tableMetaData, dataTableActionEventHandler);
            Tab tab = new Tab(tableMetaData.getTableName(), tableView);
            tab.setTooltip(new Tooltip(fullName));
            tab.setId(fullName);
            tab.setOnClosed(event -> tableTreeItem.setOpen(false));
            this.tablePane.getTabs().add(tab);
            this.tablePane.getSelectionModel().select(tab);
            tableTreeItem.setOpen(true);
        }
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
        this.tablePane.getTabs().stream().filter(tab -> fullName.equals(tab.getId())).findFirst().ifPresent(tab -> {
            DataTableView content = (DataTableView) tab.getContent();
            int pageIndex = content.getPagination().getCurrentPageIndex();
            this.dataTableActionEventHandler.refresh(content, pageIndex);
        });
    }

    @Override
    public void onMouseDoubleClick(TableTreeItem tableTreeItem) {
        this.onOpen(tableTreeItem);
    }

    @Override
    public void onSelected(TableTreeItem tableTreeItem) {
        TableMetaData tableMetaData = tableTreeItem.getTableMetaData();
        this.infoTableView.getItems().clear();
        this.ddlTextArea.clear();
        try (Connection connection = this.metadataProvider.getConnection(this.connectionConfiguration)) {
            List<KeyValuePairData> tableDetails = metadataProvider.getTableDetails(connection, tableMetaData.getTableCat(), tableMetaData.getTableSchem(), tableMetaData.getTableName(), new String[]{tableMetaData.getTableType()});
            if (tableDetails != null && !tableDetails.isEmpty()) {
                this.infoTableView.getItems().addAll(tableDetails);
            }
        } catch (SQLException e) {
            DialogUtil.error(e);
        }
        this.ddlTextArea.setText(tableMetaData.getTableName());
    }
}
