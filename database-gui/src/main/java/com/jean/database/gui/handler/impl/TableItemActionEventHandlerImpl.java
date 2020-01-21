package com.jean.database.gui.handler.impl;

import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.handler.IDataTableActionEventHandler;
import com.jean.database.gui.handler.ITableItemActionEventHandler;
import com.jean.database.gui.view.CustomTableView;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;

import java.util.Optional;

public class TableItemActionEventHandlerImpl implements ITableItemActionEventHandler {

    private final TabPane tabPane;

    private final IDataTableActionEventHandler dataTableActionEventHandler;

    public TableItemActionEventHandlerImpl(TabPane tabPane, IDataTableActionEventHandler dataTableActionEventHandler) {
        this.tabPane = tabPane;
        this.dataTableActionEventHandler = dataTableActionEventHandler;
    }


    @Override
    public void openTable(TableTreeItem tableTreeItem) {
        TableMetaData tableMetaData = tableTreeItem.getTableMetaData();
        String fullName = tableMetaData.getFullName();
        Optional<Tab> tabOptional = tabPane.getTabs().stream().filter(tab -> fullName.equals(tab.getId())).findFirst();
        if (tabOptional.isPresent()) {
            Tab tab = tabOptional.get();
            tabPane.getSelectionModel().select(tab);
        } else {
            CustomTableView tableView = new CustomTableView(tableMetaData, dataTableActionEventHandler);
            Tab tab = new Tab(tableMetaData.getTableName(), tableView);
            tab.setTooltip(new Tooltip(fullName));
            tab.setId(fullName);
            tab.setOnClosed(event -> tableTreeItem.setOpen(false));
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            tableTreeItem.setOpen(true);
        }
    }

    @Override
    public void copyTable(TableTreeItem tableTreeItem) {

    }

    @Override
    public void deleteTable(TableTreeItem tableTreeItem) {
        tableTreeItem.setOpen(false);
        tableTreeItem.getParent().getChildren().remove(tableTreeItem);
    }

    @Override
    public void refresh(TableTreeItem treeItem) {
        TableMetaData tableMetaData = treeItem.getTableMetaData();
        String fullName = tableMetaData.getFullName();
        tabPane.getTabs().stream().filter(tab -> fullName.equals(tab.getId())).findFirst().ifPresent(tab -> {
            CustomTableView content = (CustomTableView) tab.getContent();
            int pageIndex = content.getPagination().getCurrentPageIndex();
            dataTableActionEventHandler.refresh(content, pageIndex);
        });
    }
}
