package com.jean.database.redis;

import com.jean.database.api.BaseTask;
import com.jean.database.api.KeyValuePair;
import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.StringUtils;
import io.lettuce.core.api.StatefulRedisConnection;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.util.Callback;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class RedisServerInfoController implements Initializable {

    public SplitPane root;
    public LineChart<String, Long> memoryLineChart;
    public Label serverProperties;
    private Tab serverInfoTab = new Tab("Server Info");
    private RedisConnectionConfiguration connectionConfiguration;

    private XYChart.Series<String, Long> usedMemory = new XYChart.Series<>();
    private XYChart.Series<String, Long> usedMemoryRss = new XYChart.Series<>();
    private XYChart.Series<String, Long> usedMemoryPeek = new XYChart.Series<>();
    private XYChart.Series<String, Long> usedMemoryLua = new XYChart.Series<>();

    public RedisServerInfoController() {
    }

    public RedisServerInfoController(RedisConnectionConfiguration connectionConfiguration) {
        this.connectionConfiguration = connectionConfiguration;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usedMemory.setName("used memory");
        memoryLineChart.getData().add(usedMemory);
        usedMemoryRss.setName("used memory rss");
        memoryLineChart.getData().add(usedMemoryRss);
        usedMemoryPeek.setName("used memory peek");
        memoryLineChart.getData().add(usedMemoryPeek);
        usedMemoryLua.setName("used memory lua");
        memoryLineChart.getData().add(usedMemoryLua);
        serverInfoTab.setContent(root);
    }

    public static Callback<Class<?>, Object> getControllerFactory(RedisConnectionConfiguration connectionConfiguration) {
        return param -> new RedisServerInfoController(connectionConfiguration);
    }

    public Tab getServerInfoTab() {
        return serverInfoTab;
    }

    public void selected() {
        serverInfoTab.getTabPane().getSelectionModel().select(serverInfoTab);
    }

    private Timer timer = null;

    public void startTimerTask() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TaskManger.execute(new RedisServerInfoTask());
            }
        }, 0, 5000);
    }


    private class RedisServerInfoTask extends BaseTask<ServerInfo> {

        @Override
        protected ServerInfo call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                String value = connection.sync().info();
                String[] strings = value.split("\r\n");
                List<KeyValuePair<String, String>> list = new ArrayList<>(strings.length);
                for (String string : strings) {
                    if (!StringUtils.isBlank(string) && !string.startsWith("#")) {
                        String[] split = string.split(":");
                        if (split.length == 1) {
                            list.add(new KeyValuePair<>(split[0], null));
                        } else if (split.length == 2) {
                            list.add(new KeyValuePair<>(split[0], split[1]));
                        }
                    }
                }
                return new ServerInfo(value, list);
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            ServerInfo serverInfo = getValue();
            serverProperties.setText(serverInfo.rowValue);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String date = dateFormat.format(new Date());
            for (KeyValuePair<String, String> pair : serverInfo.keyValuePairs) {
                switch (pair.getKey()) {
                    case "used_memory":
                        usedMemory.getData().add(new XYChart.Data<>(date, Long.valueOf(pair.getValue())));
                        break;
                    case "used_memory_rss":
                        usedMemoryRss.getData().add(new XYChart.Data<>(date, Long.valueOf(pair.getValue())));
                        break;
                    case "used_memory_peek":
                        usedMemoryPeek.getData().add(new XYChart.Data<>(date, Long.valueOf(pair.getValue())));
                        break;
                    case "used_memory_lua":
                        usedMemoryLua.getData().add(new XYChart.Data<>(date, Long.valueOf(pair.getValue())));
                        break;
                }
            }
        }
    }


    private static class ServerInfo {
        String rowValue;
        List<KeyValuePair<String, String>> keyValuePairs;

        public ServerInfo(String rowValue, List<KeyValuePair<String, String>> keyValuePairs) {
            this.rowValue = rowValue;
            this.keyValuePairs = keyValuePairs;
        }
    }
}
