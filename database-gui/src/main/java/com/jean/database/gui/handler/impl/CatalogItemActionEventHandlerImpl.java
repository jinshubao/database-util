package com.jean.database.gui.handler.impl;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.factory.ActionLoggerWrapper;
import com.jean.database.gui.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.handler.ITableItemActionEventHandler;
import com.jean.database.gui.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogItemActionEventHandlerImpl implements ICatalogItemActionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(CatalogItemActionEventHandlerImpl.class);

    private final IMetadataProvider metadataProvider;

    private final IConnectionConfiguration connectionConfiguration;

    private final ITableItemActionEventHandler tableItemActionEventHandler;

    private final ITableTypeItemActionEventHandler tableTypeItemActionEventHandler;

    public CatalogItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;

        this.tableItemActionEventHandler = ActionLoggerWrapper.warp(new TableItemActionEventHandlerImpl(connectionConfiguration, metadataProvider, root));
        this.tableTypeItemActionEventHandler = ActionLoggerWrapper.warp(new TableTypeItemActionEventHandlerImpl(connectionConfiguration, metadataProvider, root));
    }


    @Override
    public void onOpen(CatalogTreeItem catalogTreeItem) {
        if (!catalogTreeItem.getOpen()) {
            ObservableList children = catalogTreeItem.getChildren();
            CatalogMetaData catalogMetaData = catalogTreeItem.getCatalogMetaData();
            try (Connection connection = metadataProvider.getConnection(this.connectionConfiguration)) {
                List<String> tableTypes = metadataProvider.getTableTypes(connection);
                List<TableMetaData> tableMataData = metadataProvider.getTableMataData(connection, catalogMetaData.getTableCat(), null, null, null);
                if (tableMataData != null && !tableMataData.isEmpty()) {
                    for (String tableType : tableTypes) {

                        TableTypeMetaData tableTypeMetaData = new TableTypeMetaData(catalogMetaData, tableType);
                        TreeItem typeItem = new TableTypeTreeItem(tableTypeMetaData, tableTypeItemActionEventHandler);

                        List<TableTreeItem> items = tableMataData.stream()
                                .filter(metaData -> metaData.getTableType().equals(tableType))
                                .map(metaData -> new TableTreeItem(metaData, tableItemActionEventHandler))
                                .collect(Collectors.toList());
                        //noinspection unchecked
                        children.add(typeItem);
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
                logger.error(e.getMessage(), e);
                DialogUtil.error(e);
            }
        }
    }

    @Override
    public void onClose(CatalogTreeItem catalogTreeItem) {
        catalogTreeItem.getChildren().clear();
        catalogTreeItem.setExpanded(false);
        catalogTreeItem.setOpen(false);
    }

    @Override
    public void onCreate(CatalogTreeItem catalogTreeItem) {
        //TODO
    }

    @Override
    public void onDelete(CatalogTreeItem catalogTreeItem) {
        catalogTreeItem.getChildren().clear();
        catalogTreeItem.getParent().getChildren().remove(catalogTreeItem);
        catalogTreeItem.setOpen(false);
    }

    @Override
    public void onDetails(CatalogTreeItem catalogTreeItem) {
        //TODO
    }

    @Override
    public void refresh(CatalogTreeItem treeItem) {
        treeItem.getChildren().clear();
        treeItem.setOpen(false);
        this.onOpen(treeItem);
    }

    @Override
    public void onMouseClick(CatalogTreeItem catalogTreeItem) {
    }

    @Override
    public void onMouseDoubleClick(CatalogTreeItem catalogTreeItem) {
        this.refresh(catalogTreeItem);
    }


    @Override
    public void onSelected(CatalogTreeItem catalogTreeItem) {

    }
}
