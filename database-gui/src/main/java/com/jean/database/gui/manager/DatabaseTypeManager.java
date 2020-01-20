package com.jean.database.gui.manager;

import com.jean.database.core.IDatabaseTypeProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public final class DatabaseTypeManager {

    private static final DatabaseTypeManager providerManager = new DatabaseTypeManager();

    private final List<IDatabaseTypeProvider> providers;

    private DatabaseTypeManager() {
        providers = new ArrayList<>();
        ServiceLoader<IDatabaseTypeProvider> serviceLoader = ServiceLoader.load(IDatabaseTypeProvider.class);
        for (IDatabaseTypeProvider provider : serviceLoader) {
            providers.add(provider);
        }
    }

    public static List<IDatabaseTypeProvider> getProviders() {
        return Collections.unmodifiableList(providerManager.providers);
    }
}
