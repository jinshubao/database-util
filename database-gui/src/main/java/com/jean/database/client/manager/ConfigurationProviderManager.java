package com.jean.database.client.manager;

import com.jean.database.core.IConfigurationProvider;
import com.jean.database.core.constant.DatabaseType;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public final class ConfigurationProviderManager {

    private static final List<IConfigurationProvider> providers = new ArrayList<>();

    public static void load() {
        ServiceLoader<IConfigurationProvider> serviceLoader = ServiceLoader.load(IConfigurationProvider.class);
        for (IConfigurationProvider provider : serviceLoader) {
            providers.add(provider);
        }
    }

    private ConfigurationProviderManager() {
    }

    public static IConfigurationProvider selectProvider(DatabaseType databaseType) {
        //TODO
        return providers.get(0);
    }

}
