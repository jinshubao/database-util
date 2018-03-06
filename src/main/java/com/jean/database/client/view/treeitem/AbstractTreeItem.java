package com.jean.database.client.view.treeitem;

import com.jean.database.MainApplication;
import com.jean.database.client.controller.MainController;
import com.jean.database.client.view.IRefresh;
import com.jean.database.client.view.ItemSelected;
import com.jean.database.core.connection.IConnectionConfiguration;
import com.jean.database.core.meta.CatalogMetaData;
import com.jean.database.core.meta.SchemaMetaData;
import com.jean.database.core.meta.TableMetaData;
import com.jean.database.core.provider.IMetadataProvider;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * @author jinshubao
 */
public abstract class AbstractTreeItem extends TreeItem<Object> implements IRefresh, ItemSelected {

    protected IConnectionConfiguration connectionConfiguration;
    protected CatalogMetaData catalogMetaData;
    protected SchemaMetaData schemaMetaData;
    protected TableMetaData tableMetaData;


    public AbstractTreeItem(IConnectionConfiguration connectionConfiguration, Object value) {
        this(connectionConfiguration, null, null, null, value);
    }

    public AbstractTreeItem(IConnectionConfiguration connectionConfiguration,
                            CatalogMetaData catalogMetaData, SchemaMetaData schemaMetaData, TableMetaData tableMetaData, Object value) {
        super(value);
        this.connectionConfiguration = connectionConfiguration;
        this.catalogMetaData = catalogMetaData;
        this.schemaMetaData = schemaMetaData;
        this.tableMetaData = tableMetaData;
        setGraphic(createIcon());
    }


    public final IMetadataProvider getMetadataProvider() {
        return MainApplication.getApplicationContext().getBean(MainController.class).getSupportMetaDataProvider(connectionConfiguration.getDatabaseType());
    }

    public final IConnectionConfiguration getConnectionConfiguration() {
        return MainApplication.getApplicationContext().getBean(MainController.class).getCurrentConnectionConfiguration();
    }

    /**
     * 创建上下文菜单
     *
     * @return
     */
    public ContextMenu createContextMenu() {
        return null;
    }

    public void onMouseClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            if (event.getClickCount() == 1) {

            } else if (event.getClickCount() == 2) {
                onDoubleClick(event);
            }
        }

    }

    /**
     * 鼠标左键双击
     *
     * @param event 事件
     */
    public void onDoubleClick(MouseEvent event) {
        refreshData();
    }

    protected Node createIcon() {
//        return new ImageView(new Image(getClass().getResourceAsStream(CommonConstant.LOGO_IMAGE)));
        return null;
    }

    protected final MainController getController() {
        return MainApplication.getApplicationContext().getBean(MainController.class);
    }


    public CatalogMetaData getCatalogMetaData() {
        return catalogMetaData;
    }

    public void setCatalogMetaData(CatalogMetaData catalogMetaData) {
        this.catalogMetaData = catalogMetaData;
    }

    public SchemaMetaData getSchemaMetaData() {
        return schemaMetaData;
    }

    public void setSchemaMetaData(SchemaMetaData schemaMetaData) {
        this.schemaMetaData = schemaMetaData;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public void setTableMetaData(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    @Override
    public void setSelected() {
        onSelected(this);
    }

    @Override
    public void onSelected(AbstractTreeItem treeItem) {
        System.out.println(treeItem);
    }
}
