package com.jean.database.api;

import com.jean.database.api.utils.FxmlUtils;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * FXML Controller 工厂类
 * 整合 FXML 加载和 Controller 创建过程
 */
public class FxmlControllerFactory {

    /**
     * FXML 加载结果封装
     */
    public static class LoadResult<T> {
        private final Parent parent;
        private final T controller;

        public LoadResult(Parent parent, T controller) {
            this.parent = parent;
            this.controller = controller;
        }

        public Parent getParent() {
            return parent;
        }

        public T getController() {
            return controller;
        }
    }

    /**
     * 加载 FXML 并创建 Controller
     *
     * @param fxmlPath    FXML 文件路径
     * @param bundleName  资源包名称
     * @param context     Controller 上下文
     * @param factory     Controller 工厂
     * @return 加载结果
     * @throws IOException 加载失败时抛出
     */
    public static <T> LoadResult<T> load(
            String fxmlPath,
            String bundleName,
            ControllerContext context,
            ControllerFactory<T> factory) throws IOException {

        T controller = factory.create(context);
        FxmlUtils.LoadFxmlResult result = FxmlUtils.loadFxml(fxmlPath, bundleName, controller);
        return new LoadResult<>(result.parent(), controller);
    }

    /**
     * 加载 FXML 并创建 Controller（使用默认资源包）
     *
     * @param fxmlPath FXML 文件路径
     * @param context  Controller 上下文
     * @param factory  Controller 工厂
     * @return 加载结果
     * @throws IOException 加载失败时抛出
     */
    public static <T> LoadResult<T> load(
            String fxmlPath,
            ControllerContext context,
            ControllerFactory<T> factory) throws IOException {

        return load(fxmlPath, null, context, factory);
    }
}
