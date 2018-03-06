package com.jean.database.client.factory;

import com.jean.database.core.constant.DatabaseType;
import com.jean.database.core.provider.IMetadataProvider;

import java.util.List;

/**
 * @author jinshubao
 */
public class MetaDataProviderManager {

    List<IMetadataProvider> metadataProviders;

    public MetaDataProviderManager(List<IMetadataProvider> metadataProviders) {
        this.metadataProviders = metadataProviders;
    }

    public IMetadataProvider getMetadataProvider(DatabaseType databaseType) {

        for (IMetadataProvider metadataProvider : metadataProviders) {
            if (metadataProvider.support(databaseType)) {
                return metadataProvider;
            }
        }
        throw new RuntimeException("不支持该数据库");
    }
}
