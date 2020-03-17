package com.jean.database.gui;

import com.jean.database.common.utils.DialogUtil;
import com.jean.database.common.utils.FxmlUtils;
import com.jean.database.common.utils.StringUtil;
import com.jean.database.gui.constant.Images;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
public class MainApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    private List<String> params;

    @Override
    public void init() throws Exception {
        //启动参数
        this.params = getParameters().getRaw();
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_INIT, this));
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

    private void applicationStart(Stage stage) throws Exception {
        Parent root = FxmlUtils.loadFxml("/fxml/Scene.fxml", "message.scene", Locale.SIMPLIFIED_CHINESE);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        String name = "database-gui";
        String version = "0.1-snapshot";
        String title = StringUtil.join(Arrays.asList(name, version), " --");
        stage.setTitle(title);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(Images.LOGO_IMAGE)));
        stage.setScene(scene);
        stage.show();
        DialogUtil.setLogImage(new Image(getClass().getResourceAsStream(Images.LOGO_IMAGE)));
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
}
