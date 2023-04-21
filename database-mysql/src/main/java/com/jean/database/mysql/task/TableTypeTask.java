package com.jean.database.mysql.task;

import com.jean.database.sql.SQLMetadataFactory;
import com.jean.database.sql.meta.CatalogMetaData;
import com.jean.database.sql.meta.TableMetaData;
import com.jean.database.sql.meta.TableTypeMetaData;
import com.jean.database.task.BackgroundTask;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableTypeTask extends BackgroundTask<List<TableTypeMetaData>> {

    private final CatalogMetaData catalogMetaData;
    private final SQLMetadataFactory metadataProvider;


    public TableTypeTask(SQLMetadataFactory metadataProvider, CatalogMetaData catalogMetaData) {
        super("获取表类型");
        this.metadataProvider = metadataProvider;
        this.catalogMetaData = catalogMetaData;
    }

    @Override
    protected List<TableTypeMetaData> doBackground() throws Exception {
        List<String> tableTypes = metadataProvider.getTableTypes();
        List<TableMetaData> tableMataData = metadataProvider.getTableMataData(catalogMetaData.getTableCat(), null, null, null);

        List<TableTypeMetaData> tableTypeMetaDataList = new ArrayList<>(tableTypes.size());
        for (String tableType : tableTypes) {
            TableTypeMetaData tableTypeMetaData = new TableTypeMetaData();
            tableTypeMetaData.setTableCat(catalogMetaData.getTableCat());
            tableTypeMetaData.setQuoteString(catalogMetaData.getQuoteString());
            tableTypeMetaData.setSeparator(catalogMetaData.getSeparator());
            tableTypeMetaData.setTableType(tableType);
            List<TableMetaData> collect = tableMataData.stream()
                    .filter(metaData -> metaData.getTableType().equals(tableType))
                    .collect(Collectors.toList());
            tableTypeMetaData.setTableMetaDataList(collect);

            tableTypeMetaDataList.add(tableTypeMetaData);
        }
        return tableTypeMetaDataList;
    }
}