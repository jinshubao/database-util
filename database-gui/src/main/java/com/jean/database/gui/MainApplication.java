package com.jean.database.gui;

import com.jean.database.common.resource.EncodingResourceBundleControl;
import com.jean.database.common.utils.DialogUtil;
import com.jean.database.common.utils.StringUtil;
import com.jean.database.gui.constant.Images;
import com.jean.database.gui.factory.ControllerFactory;
import com.jean.database.gui.manager.TaskManger;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
public class MainApplication extends Application implements Callback<Class<?>, Object> {

    private List<String> params;
    private ResourceBundle bundle;

    @Override
    public void init() throws Exception {
        //启动参数
        this.params = getParameters().getRaw();
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_INIT, this));
        bundle = ResourceBundle.getBundle("message.scene", Locale.getDefault(), new EncodingResourceBundleControl());
    }

    @Override
    public void start(Stage stage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START, this));
        DialogUtil.setLogImage(new Image(getClass().getResourceAsStream(Images.LOGO_IMAGE)));
        URL resource = getClass().getResource("/fxml/Scene.fxml");
        FXMLLoader loader = new FXMLLoader(resource, bundle, null, new ControllerFactory());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        String name = "database-gui";
        String version = "0.1-snapshot";
        String title = StringUtil.join(Arrays.asList(name, version), " --");
        stage.setTitle(title);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(Images.LOGO_IMAGE)));
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

    @Override
    public Object call(Class<?> param) {
        return null;
    }
}
