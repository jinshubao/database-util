package com.jean.database.client.support;

import com.jean.database.client.utils.DialogUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * JavaFX-Spring 适配
 *
 * @author jinshubao
 * @date 2017/4/8
 */
public abstract class ApplicationSupport extends Application {
    static final Logger logger = LoggerFactory.getLogger(ApplicationSupport.class);
    static ApplicationContext applicationContext;
    Stage primaryStage;


    public ApplicationSupport() {
    }

    @Override
    public void init() throws Exception {
        applicationInit();
    }

    /**
     * 应用初始化，界面还没有初始化
     *
     * @throws Exception
     */
    protected void applicationInit() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(this.getClass());
        context.getAutowireCapableBeanFactory().autowireBean(this);
        applicationContext = context;
    }

    /**
     * 初始化界面
     *
     * @param stage
     * @throws Exception
     */
    protected abstract void applicationStart(Stage stage) throws Exception;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        applicationStart(primaryStage);
        primaryStage.setOnCloseRequest(event -> DialogUtil.confirmation("退出提示", null, "确认退出？")
                .ifPresent(button -> {
                    if (button != ButtonType.OK) {
                        event.consume();
                    }
                }));
    }

    @Override
    public void stop() throws IOException {
        if (applicationContext instanceof Closeable) {
            Closeable context = (Closeable) applicationContext;
            context.close();
        }
    }

    protected static void launchApplication(Class<? extends ApplicationSupport> applicationClass, String... args) {
        logger.info("launch with parameters {}", Arrays.toString(args));
        launch(applicationClass, args);
    }

    protected Parent loadFxml(String name) {
        return loadFxml(name, null, null);
    }

    protected Parent loadFxml(String name, String resource, Locale locale) {
        logger.info("loading fxml {}", name);
        FXMLLoader loader = new FXMLLoader();
        if (resource != null) {
            loader.setResources(ResourceBundle.getBundle(resource, locale));
        }
        loader.setControllerFactory(param -> applicationContext.getBean(param));
        try {
            return loader.load(getClass().getResourceAsStream(name));
        } catch (IOException e) {
            logger.error("加载fxml文件{}出错", e);
            throw new RuntimeException(e);
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
