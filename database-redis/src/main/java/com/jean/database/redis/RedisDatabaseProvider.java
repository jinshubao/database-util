package com.jean.database.redis;

import com.jean.database.api.DefaultDatabaseProvider;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.redis.view.cluster.RedisClusterServerItem;
import com.jean.database.redis.view.single.RedisServerItem;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

import java.time.Duration;
import java.util.Collections;

public class RedisDatabaseProvider extends DefaultDatabaseProvider {

    private static final String NAME = "Redis";

    private final RedisConnectionConfiguration defaultCollectConfiguration;

    public RedisDatabaseProvider() {
        String host = "127.0.0.1";
        this.defaultCollectConfiguration
                = new RedisConnectionConfiguration("Redis[" + host + "]", host, 6379, null);
    }

    @Override
    public void init() {
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/redis/redis.png"));
        menuItem.setOnAction(event -> {
            RedisConnectionConfiguration configuration = getConnectionConfiguration();
            if (configuration != null) {
                if (configuration.isClusterMode()) {
                    //区分是否为集群模式
                    ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                            //开启自适应刷新
                            .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
                            .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(10))
                            .enablePeriodicRefresh(Duration.ofSeconds(10L))
                            .build();
                    ClusterClientOptions options = ClusterClientOptions.builder()
                            .autoReconnect(true)
                            .maxRedirects(1)
                            .topologyRefreshOptions(topologyRefreshOptions)
                            .build();
                    RedisURI.Builder builder = RedisURI.builder()
                            .withHost(configuration.getHost())
                            .withPort(configuration.getPort());
                    if (configuration.getPassword() != null) {
                        builder.withPassword(configuration.getPassword());
                    }
                    RedisClusterClient redisClusterClient = RedisClusterClient.create(Collections.singletonList(builder.build()));
                    redisClusterClient.setOptions(options);
                    getViewContext().addDatabaseItem(new RedisClusterServerItem(getViewContext(), configuration.getConnectionName(), redisClusterClient));
                } else {
                    getViewContext().addDatabaseItem(new RedisServerItem(getViewContext(), configuration));
                }
            }
        });
        getViewContext().addConnectionMenus(menuItem);
    }

    @Override
    public String getIdentifier() {
        return NAME;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public void close() {

    }

    private RedisConnectionConfiguration getConnectionConfiguration() {
        try {
            FxmlUtils.LoadFxmlResult loadFxmlResult =
                    FxmlUtils.loadFxml("/fxml/redis-conn-cfg.fxml", null, new RedisConnectionConfigurationController(getViewContext(), defaultCollectConfiguration));
            RedisConnectionConfigurationController cfgController = (RedisConnectionConfigurationController) loadFxmlResult.getController();
            Callback<ButtonType, RedisConnectionConfiguration> callback = buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return cfgController.getValue();
                }
                return null;
            };
            return DialogUtil.customizeDialog("New Redis connection", loadFxmlResult.getParent(), callback).orElse(null);
        } catch (Exception e) {
            DialogUtil.error(e);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 30000;
    }
}
