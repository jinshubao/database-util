package com.jean.database.gui;

import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.api.utils.StringUtils;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
public class MainApplication extends Application  {

    private Parameters parameters;

    @Override
    public void init() throws Exception {
        //启动参数
        this.parameters = getParameters();
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_INIT, this));
    }

    @Override
    public void start(Stage stage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START, this));
        FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("/fxml/Scene.fxml", "message.scene");
        Scene scene = new Scene(loadFxmlResult.getParent());
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
        launch(MainApplication.class, args);
    }

}
