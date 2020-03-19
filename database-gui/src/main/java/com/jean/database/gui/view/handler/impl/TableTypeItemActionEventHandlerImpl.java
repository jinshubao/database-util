package com.jean.database.gui.view.handler.impl;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.core.IConnectionConfiguration;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.constant.TableType;
import com.jean.database.core.meta.TableSummaries;
import com.jean.database.core.meta.TableTypeMetaData;
import com.jean.database.gui.view.handler.AbstractEventHandler;
import com.jean.database.gui.view.handler.ITableTypeItemActionEventHandler;
import com.jean.database.gui.view.treeitem.TableTypeTreeItem;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TableTypeItemActionEventHandlerImpl extends AbstractEventHandler<TableTypeTreeItem> implements ITableTypeItemActionEventHandler {

    private final IConnectionConfiguration connectionConfiguration;
    private final IMetadataProvider metadataProvider;

    private final TabPane tablePane;
    private final TableView<TableSummaries> objectTableView;
    private final TextArea ddlTextArea;


    public TableTypeItemActionEventHandlerImpl(IConnectionConfiguration connectionConfiguration, IMetadataProvider metadataProvider, Node root) {
        super(root);
        this.connectionConfiguration = connectionConfiguration;
        this.metadataProvider = metadataProvider;
        this.tablePane = this.lookup("#tablePane");
        this.objectTableView = this.lookup("#objectTableView");
        this.ddlTextArea = this.lookup("#ddlTextArea");
    }

    @Override
    public void onSelected(TableTypeTreeItem tableTypeTreeItem) {
        TableTypeMetaData tableTypeMetaData = tableTypeTreeItem.getValue();
        this.objectTableView.getItems().clear();
        if (TableType.TABLE.equals(tableTypeMetaData.getTableType())) {
            try (Connection connection = this.metadataProvider.getConnection(this.connectionConfiguration)) {
                List<TableSummaries> tableSummaries = metadataProvider.getTableSummaries(connection, tableTypeMetaData.getTableCat(), tableTypeMetaData.getTableSchem(), null, null);
                if (tableSummaries != null && !tableSummaries.isEmpty()) {
                    this.objectTableView.getItems().addAll(tableSummaries);
                }
                tablePane.getSelectionModel().select(0);
            } catch (SQLException e) {
                DialogUtil.error(e);
            }
        }
        this.ddlTextArea.setText(tableTypeTreeItem.getValue().getTableType());
    }
}
