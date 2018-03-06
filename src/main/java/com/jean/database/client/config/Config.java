package com.jean.database.client.config;


import com.jean.database.client.factory.MetaDataProviderManager;
import com.jean.database.core.provider.IMetadataProvider;
import com.jean.database.core.provider.MySqlMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jinshubao
 */
@Configuration
public class Config {


    public List<IMetadataProvider> metadataProviders() {
        return Collections.singletonList(new MySqlMetadataProvider());
    }

    @Bean
    MetaDataProviderManager providerManager() {
        return new MetaDataProviderManager(metadataProviders());

    }


    @Bean("generate-executor")
    Executor executor() {
        return new ThreadPoolExecutor(2, 5, 600, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new DefaultManagedAwareThreadFactory());
    }

}
