package com.jean.database.gui;

import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.api.utils.StringUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author jinshubao
 * &#064;date  2017/4/8
 */
public class MainApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    private Parameters parameters;

    @Override
    public void init() throws Exception {
        //启动参数
        logger.debug("application init");
        this.parameters = getParameters();
    }

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug("application start");
        FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("fxml/Scene.fxml", "message.scene");
        Scene scene = new Scene(loadFxmlResult.parent());
        scene.getStylesheets().add("/styles/Styles.css");
        String name = parameters.getNamed().get("name");
        String version = parameters.getNamed().get("version");
        String title = StringUtils.join(Arrays.asList(name, version), " ");
        stage.setTitle(title);
        stage.getIcons().add(ImageUtils.LOGO_IMAGE);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> DialogUtil.confirmation("退出提示", null, "确认退出？")
                .ifPresent(button -> {
                    if (button != ButtonType.OK) {
                        event.consume();
                    }
                }));
    }

    @Override
    public void stop() throws Exception {
        logger.debug("application stop");
        TaskManger.shutdown();
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
