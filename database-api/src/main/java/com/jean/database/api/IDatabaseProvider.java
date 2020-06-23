package com.jean.database.api;

public interface IDatabaseProvider {

    void init(ViewContext viewContext);

    String getIdentifier();

    String getName();

    String getIcon();

    void close();

}
