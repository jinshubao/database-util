package com.jean.database.gui.handler.impl;

import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.gui.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.handler.ITableItemActionEventHandler;
import com.jean.database.core.utils.DialogUtil;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogItemActionEventHandlerImpl implements ICatalogItemActionEventHandler {

    private final IMetadataProvider metadataProvider;

    private final Connection connection;

    private final ITableItemActionEventHandler eventHandler;

    public CatalogItemActionEventHandlerImpl(IMetadataProvider metadataProvider, Connection connection, ITableItemActionEventHandler eventHandler) {
        this.metadataProvider = metadataProvider;
        this.connection = connection;
        this.eventHandler = eventHandler;
    }


    @Override
    public void openCatalog(CatalogTreeItem catalogTreeItem) {
        if (!catalogTreeItem.getOpen()) {
            ObservableList children = catalogTreeItem.getChildren();
            CatalogMetaData catalogMetaData = catalogTreeItem.getCatalogMetaData();
            try {
                List<String> tableTypes = metadataProvider.getTableTypes(connection);
                List<TableMetaData> tableMataData = metadataProvider.getTableMataData(connection, catalogMetaData.getTableCat(), null, null, null);
                if (tableMataData != null && !tableMataData.isEmpty()) {
                    for (String tableType : tableTypes) {
                        TreeItem typeItem = new TreeItem<>(tableType);
                        //noinspection unchecked
                        children.add(typeItem);
                        List<TableTreeItem> items = tableMataData.stream()
                                .filter(metaData -> metaData.getTableType().equals(tableType))
                                .map(metaData -> new TableTreeItem( metaData, eventHandler))
                                .collect(Collectors.toList());
                        if (!items.isEmpty()) {
                            //noinspection unchecked
                            typeItem.getChildren().addAll(items);
                            typeItem.setExpanded(true);
                        }
                    }
                    catalogTreeItem.setExpanded(true);
                    catalogTreeItem.setOpen(true);
                }
            } catch (SQLException e) {
                DialogUtil.error("ERROR", e.getMessage(), e);
            }
        }
    }

    @Override
    public void closeCatalog(CatalogTreeItem catalogTreeItem) {
        catalogTreeItem.getChildren().clear();
        catalogTreeItem.setExpanded(false);
        catalogTreeItem.setOpen(false);
    }

    @Override
    public void createCatalog(CatalogTreeItem catalogTreeItem) {
        //TODO
    }

    @Override
    public void deleteCatalog(CatalogTreeItem catalogTreeItem) {
        catalogTreeItem.getChildren().clear();
        catalogTreeItem.getParent().getChildren().remove(catalogTreeItem);
        catalogTreeItem.setOpen(false);
    }

    @Override
    public void catalogProperties(CatalogTreeItem catalogTreeItem) {
        //TODO
    }

    @Override
    public void refresh(CatalogTreeItem treeItem) {
        treeItem.getChildren().clear();
        treeItem.setOpen(false);
        this.openCatalog(treeItem);
    }
}
