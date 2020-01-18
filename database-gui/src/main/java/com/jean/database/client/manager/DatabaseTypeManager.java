package com.jean.database.client.manager;

import com.jean.database.core.IDatabaseTypeProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public final class DatabaseTypeManager {

    private static List<IDatabaseTypeProvider> databaseTypes;

    public static void load() {
        databaseTypes = new ArrayList<>();
        ServiceLoader<IDatabaseTypeProvider> types = ServiceLoader.load(IDatabaseTypeProvider.class);
        for (IDatabaseTypeProvider provider : types) {
            databaseTypes.add(provider);
        }
    }

    public static List<IDatabaseTypeProvider> getDatabaseTypes() {
        return Collections.unmodifiableList(databaseTypes);
    }
}
