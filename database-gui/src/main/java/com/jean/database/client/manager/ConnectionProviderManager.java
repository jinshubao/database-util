package com.jean.database.client.manager;

import com.jean.database.core.IConnectionProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public final class ConnectionProviderManager {

    private static List<IConnectionProvider> connectionProviders;

    public static void load() {
        connectionProviders = new ArrayList<>();
        ServiceLoader<IConnectionProvider> serviceLoader = ServiceLoader.load(IConnectionProvider.class);
        for (IConnectionProvider provider : serviceLoader) {
            connectionProviders.add(provider);
        }
    }

    private ConnectionProviderManager() {
    }

    public static List<IConnectionProvider> getConnectionProviders() {
        return Collections.unmodifiableList(connectionProviders);
    }
}
