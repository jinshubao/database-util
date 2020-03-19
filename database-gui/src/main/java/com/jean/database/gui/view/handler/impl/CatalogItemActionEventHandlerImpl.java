package com.jean.database.gui.view.handler.impl;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.factory.ActionLoggerWrapper;
import com.jean.database.gui.view.handler.AbstractEventHandler;
import com.jean.database.gui.view.handler.ICatalogItemActionEventHandler;
import com.jean.database.gui.view.handler.ITableItemActionEventHandler;
import com.jean.database.gui.view.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.treeitem.CatalogTreeItem;
import com.jean.database.gui.view.treeitem.TableTreeItem;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class CatalogItemActionEventHandlerImpl extends AbstractEventHandler<CatalogTreeItem> implements ICatalogItemActionEventHandler {

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    private final ITableItemActionEventHandler tableItemActionEventHandler;
    private final ITableTypeItemActionEventHandler tableTypeItemActionEventHandler;

    private final TextArea ddlTextArea;

    public CatalogItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        super(root);
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.tableItemActionEventHandler = ActionLoggerWrapper.warp(new TableItemActionEventHandlerImpl(connectionConfiguration, metadataProvider, root));
        this.tableTypeItemActionEventHandler = ActionLoggerWrapper.warp(new TableTypeItemActionEventHandlerImpl(connectionConfiguration, metadataProvider, root));

        this.ddlTextArea = this.lookup("#ddlTextArea");
    }


    @Override
    public void onOpen(CatalogTreeItem catalogTreeItem) {
        if (!catalogTreeItem.getOpen()) {
            ObservableList children = catalogTreeItem.getChildren();
            CatalogMetaData catalogMetaData = catalogTreeItem.getCatalogMetaData();
            try (Connection connection = this.metadataProvider.getConnection(this.connectionConfiguration)) {
                List<String> tableTypes = this.metadataProvider.getTableTypes(connection);
                List<TableMetaData> tableMataData = this.metadataProvider.getTableMataData(connection, catalogMetaData.getTableCat(), null, null, null);
                if (tableMataData != null && !tableMataData.isEmpty()) {
                    for (String tableType : tableTypes) {

                        TableTypeMetaData tableTypeMetaData = new TableTypeMetaData(catalogMetaData, tableType);
                        TreeItem typeItem = new TableTypeTreeItem(tableTypeMetaData, this.tableTypeItemActionEventHandler);

                        List<TableTreeItem> items = tableMataData.stream()
                                .filter(metaData -> metaData.getTableType().equals(tableType))
                                .map(metaData -> new TableTreeItem(metaData, this.tableItemActionEventHandler))
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
    public void onDelete(CatalogTreeItem catalogTreeItem) {
        catalogTreeItem.getChildren().clear();
        catalogTreeItem.getParent().getChildren().remove(catalogTreeItem);
        catalogTreeItem.setOpen(false);
    }

    @Override
    public void refresh(CatalogTreeItem treeItem) {
        treeItem.getChildren().clear();
        treeItem.setOpen(false);
        this.onOpen(treeItem);
    }

    @Override
    public void onMouseDoubleClick(CatalogTreeItem catalogTreeItem) {
        this.refresh(catalogTreeItem);
    }

    @Override
    public void onSelected(CatalogTreeItem catalogTreeItem) {
        this.ddlTextArea.setText(catalogTreeItem.getValue().getTableCat());
    }

    @Override
    public void onOpenCommandLine(CatalogTreeItem serverTreeItem) {
        //TODO 打开命令行界面
    }

    @Override
    public void onExecuteSqlFile(CatalogTreeItem serverTreeItem) {

    }


    @Override
    public void onExportStructAndData(CatalogTreeItem serverTreeItem) {

    }

    @Override
    public void onExportStruct(CatalogTreeItem serverTreeItem) {

    }

    @Override
    public void onPrintDatabase(CatalogTreeItem catalogTreeItem) {

    }

    @Override
    public void onTransformData(CatalogTreeItem catalogTreeItem) {

    }

    @Override
    public void onConvertToMode(CatalogTreeItem catalogTreeItem) {

    }

    @Override
    public void onFind(CatalogTreeItem catalogTreeItem) {

    }
}
