package com.jean.database.client;

import com.jean.database.client.constant.CommonConstant;
import com.jean.database.client.constant.StageType;
import com.jean.database.client.manager.*;
import com.jean.database.client.utils.DialogUtil;
import com.jean.database.core.utils.StringUtil;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
public class MainApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    private List<String> params;
    private Locale locale = Locale.SIMPLIFIED_CHINESE;
    private EncodingResourceBundleControl bundleControl = new EncodingResourceBundleControl(StandardCharsets.UTF_8);

    @Override
    public void init() throws Exception {
        //启动参数
        this.params = getParameters().getRaw();
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_INIT, this));
        ConfigurationProviderManager.load();
        ConnectionProviderManager.load();
        DatabaseTypeManager.load();
        MetaDataProviderManager.load();
        DataProviderManager.load();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START, this));
        applicationStart(primaryStage);
        primaryStage.setOnCloseRequest(event -> DialogUtil.confirmation("退出提示", null, "确认退出？")
                .ifPresent(button -> {
                    if (button != ButtonType.OK) {
                        event.consume();
                    }
                }));
    }

    public void applicationStart(Stage stage) throws Exception {
        Parent root = loadFxml("/fxml/Scene.fxml", "message.scene", locale, bundleControl);
        CommonConstant.SCENES.put(StageType.MAIN, root);
        Parent databaseConnection = loadFxml("/fxml/Connection.fxml", "message.connection", locale, bundleControl);
        CommonConstant.SCENES.put(StageType.CONNECTION, databaseConnection);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        String name = "database-client";
        String version = "0.1-snapshot";
        String title = StringUtil.join(Arrays.asList(name, version), " --");
        stage.setTitle(title);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(CommonConstant.LOGO_IMAGE)));
        stage.setScene(scene);
        stage.show();

    }

    protected Parent loadFxml(String name, String resource, Locale locale, ResourceBundle.Control control) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        if (resource != null) {
            loader.setResources(ResourceBundle.getBundle(resource, locale, control));
        }
        loader.setControllerFactory(param -> {
            try {
                return param.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return loader.load(getClass().getResourceAsStream(name));
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(MainApplication.class, args);
    }


    /**
     * ResourceBundle 编码
     */
    private static final class EncodingResourceBundleControl extends ResourceBundle.Control {

        private final Charset encoding;

        private EncodingResourceBundleControl(Charset encoding) {
            this.encoding = encoding;
        }

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            URL resourceURL = loader.getResource(resourceName);
            if (resourceURL != null) {
                return new PropertyResourceBundle(new InputStreamReader(resourceURL.openStream(), encoding));
            }
            return super.newBundle(baseName, locale, format, loader, reload);
        }
    }
}
