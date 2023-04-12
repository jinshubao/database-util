package com.jean.database.gui;

import com.jean.database.context.ApplicationContext;
import com.jean.database.context.DefaultApplicationContext;
import com.jean.database.gui.controller.MainController;
import com.jean.database.gui.factory.MainControllerFactory;
import com.jean.database.provider.IDatabaseProvider;
import com.jean.database.provider.ProviderManager;
import com.jean.database.task.DefaultBackgroundTaskManager;
import com.jean.database.utils.DialogUtil;
import com.jean.database.utils.FxmlUtils;
import com.jean.database.utils.ImageUtils;
import com.jean.database.utils.StringUtils;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
public class MainApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    private Parameters parameters;

    private ApplicationContext applicationContext;

    public MainApplication() {

    }

    @Override
    public void init() throws Exception {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        //启动参数
        logger.debug("application init");
        this.parameters = getParameters();
//        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_INIT, this));
    }

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug("application start");
//        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START, this));
        String minThreads = parameters.getNamed().getOrDefault("minthreads", "1");
        String maxThreads = parameters.getNamed().getOrDefault("maxthreads", Runtime.getRuntime().availableProcessors() + "");
        String name = parameters.getNamed().get("name");
        String version = parameters.getNamed().get("version");
        DefaultBackgroundTaskManager backgroundTaskManager = new DefaultBackgroundTaskManager(Integer.parseInt(minThreads), Integer.parseInt(maxThreads));

        DefaultApplicationContext context = new DefaultApplicationContext();
        context.setBackgroundTaskManager(backgroundTaskManager);

        MainControllerFactory factory = new MainControllerFactory(applicationContext);
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("fxml/Scene.fxml", "message.scene", locale, factory);
        MainController controller = (MainController) loadFxmlResult.getController();
        context.setRootContext(controller);
        Scene scene = new Scene(loadFxmlResult.getParent());
        scene.getStylesheets().add("styles/Styles.css");
        String title = StringUtils.join(Arrays.asList(name, version), " ");
        stage.setTitle(title);
        stage.getIcons().add(ImageUtils.LOGO_IMAGE);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> DialogUtil.confirmation("退出提示", null, "确认退出？").ifPresent(button -> {
            if (button != ButtonType.OK) {
                event.consume();
            }
        }));
        this.applicationContext = context;

        //初始化provider
        List<IDatabaseProvider> providers = ProviderManager.getProviders();
        providers.sort(Comparator.comparingInt(IDatabaseProvider::getOrder));
        for (IDatabaseProvider provider : providers) {
            provider.init(context);
        }
    }

    @Override
    public void stop() throws Exception {
        logger.debug("application stop");
        if (this.applicationContext != null && this.applicationContext.getBackgroundTaskManager() != null) {
            this.applicationContext.getBackgroundTaskManager().shutdown();
        }
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
        logger.debug("application launch");
        launch(MainApplication.class, args);
    }

}
