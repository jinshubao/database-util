package com.jean.database.core;


import java.util.Properties;

/**
 * @author jinshubao
 */
public interface IConnectionConfiguration {

    String getHost();

    Integer getPort();

    String getUser();

    String getPassword();

    String getUrl();

    Properties getProperties();

}
