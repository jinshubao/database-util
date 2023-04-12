package com.jean.database.store;

public interface ConfigurationStorage<C> {

    C getConfiguration(String name);

    void saveConfiguration(C config, String name);
}
