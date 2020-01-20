package com.jean.database.core;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author jinshubao
 */
public interface IConnectionConfiguration extends Serializable {

    String getConnectionName();

    String getUrl();

    Properties getProperties();

}
