package com.jean.database;

import com.jean.database.client.constant.CommonConstant;
import com.jean.database.client.constant.StageType;
import com.jean.database.client.support.ApplicationSupport;
import com.jean.database.core.utils.StringUtil;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author jinshubao
 * @date 2017/4/8
 */
@ComponentScan
public class MainApplication extends ApplicationSupport {


    @Override
    public void applicationStart(Stage stage) throws Exception {
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        Parent root = loadFxml("/fxml/Scene.fxml", "message.scene", locale);
        CommonConstant.SCENES.put(StageType.MAIN, root);
        Parent databaseConnection = loadFxml("/fxml/Connection.fxml", "message.connection", locale);
        CommonConstant.SCENES.put(StageType.CONNECTION, databaseConnection);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        String name = "mybatis-generator-client";
        String version = "0.1-snapshot";
        String author = "jinshubao";
        String title = StringUtil.join(Arrays.asList(name, version, author), " --");
        stage.setTitle(title);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(CommonConstant.LOGO_IMAGE)));
        stage.setScene(scene);
        stage.show();
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
        launchApplication(MainApplication.class, args);
    }
}
