package com.jean.database.gui;

import com.jean.database.api.IDatabaseProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author jinshubao
 */
public final class DatabaseManager {

    private static final List<IDatabaseProvider> providers = new ArrayList<>();

    private DatabaseManager() {

    }

    public static synchronized void init() {
        ServiceLoader<IDatabaseProvider> serviceLoader = ServiceLoader.load(IDatabaseProvider.class);
        for (IDatabaseProvider provider : serviceLoader) {
            providers.add(provider);
        }
    }

    public static List<IDatabaseProvider> getProviders() {
        return Collections.unmodifiableList(providers);
    }
}
