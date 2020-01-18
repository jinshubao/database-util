package com.jean.database.client.manager;

import com.jean.database.core.IMetadataProvider;
import com.jean.database.core.constant.DatabaseType;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author jinshubao
 */
public final class MetaDataProviderManager {

    private static List<IMetadataProvider> providers;

    public static void load() {
        providers = new ArrayList<>();
        ServiceLoader<IMetadataProvider> serviceLoader = ServiceLoader.load(IMetadataProvider.class);
        for (IMetadataProvider provider : serviceLoader) {
            providers.add(provider);
        }
    }

    private MetaDataProviderManager() {
    }

    public static IMetadataProvider getMetadataProvider(DatabaseType databaseType) {
        throw new RuntimeException("不支持该数据库");
    }
}
