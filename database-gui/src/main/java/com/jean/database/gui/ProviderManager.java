package com.jean.database.gui;

import com.jean.database.api.IDatabaseProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author jinshubao
 */
public final class ProviderManager {


    private ProviderManager() {
    }

    private static final class Holder {
        private static final List<IDatabaseProvider> providers = new ArrayList<>();

        static {
            ServiceLoader<IDatabaseProvider> serviceLoader = ServiceLoader.load(IDatabaseProvider.class);
            for (IDatabaseProvider provider : serviceLoader) {
                providers.add(provider);
            }
        }
    }

    public static List<IDatabaseProvider> getProviders() {
        return new ArrayList<>(Holder.providers);
    }
}
