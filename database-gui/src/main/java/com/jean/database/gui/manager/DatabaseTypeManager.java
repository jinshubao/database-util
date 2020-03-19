package com.jean.database.gui.manager;

import com.jean.database.core.IDatabaseProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author jinshubao
 */
public final class DatabaseTypeManager {

    private static final DatabaseTypeManager providerManager = new DatabaseTypeManager();

    private final List<IDatabaseProvider> providers;

    private DatabaseTypeManager() {
        providers = new ArrayList<>();
        ServiceLoader<IDatabaseProvider> serviceLoader = ServiceLoader.load(IDatabaseProvider.class);
        for (IDatabaseProvider provider : serviceLoader) {
            providers.add(provider);
        }
    }

    public static List<IDatabaseProvider> getProviders() {
        return Collections.unmodifiableList(providerManager.providers);
    }
}
