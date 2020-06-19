package com.jean.database.api;

import java.util.Properties;

public interface IConnectionConfiguration {

    String getConnectionId();

    String getConnectionName();

    String getUrl();

    Properties getProperties();

}
