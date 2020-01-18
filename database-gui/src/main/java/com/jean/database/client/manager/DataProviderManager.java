package com.jean.database.client.manager;

import com.jean.database.core.IDataProvider;
import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.constant.DatabaseType;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author jinshubao
 */
public final class DataProviderManager {

    private static List<IDataProvider> providers;

    public static void load() {
        providers = new ArrayList<>();
        ServiceLoader<IDataProvider> serviceLoader = ServiceLoader.load(IDataProvider.class);
        for (IDataProvider provider : serviceLoader) {
            providers.add(provider);
        }
    }

    private DataProviderManager() {
    }

    public static IMetadataProvider getDataProvider(DatabaseType databaseType) {
        throw new RuntimeException("不支持该数据库");
    }
}
