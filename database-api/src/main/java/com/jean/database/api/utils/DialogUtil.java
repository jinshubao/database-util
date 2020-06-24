package com.jean.database.api.utils;

import com.jean.database.api.TaskManger;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * 弹框工具类
 *
 * @author jinshubao
 * @date 2017/4/8
 */
public final class DialogUtil {

    public static Optional<ButtonType> information(String title, String headerText, String text) {
        return alert(Alert.AlertType.INFORMATION, title, headerText, text, ButtonType.CLOSE);
    }

    public static Optional<ButtonType> confirmation(String title, String headerText, String contentText) {
        return alert(Alert.AlertType.CONFIRMATION, title, headerText, contentText, ButtonType.CANCEL, ButtonType.OK);
    }

    public static Optional<ButtonType> warning(String title, String headerText, String contentText) {
        return alert(Alert.AlertType.WARNING, title, headerText, contentText, ButtonType.OK);
    }

    public static void error(Throwable ex) {
        error("ERROR", ex.getMessage(), ex.getMessage(), ex, ButtonType.CLOSE);
    }

    public static void error(String title, Throwable ex) {
        error(title, null, ex.getMessage(), ex, ButtonType.CLOSE);
    }

    public static void error(String title, String headerText, Throwable ex) {
        error(title, headerText, ex.getMessage(), ex, ButtonType.CLOSE);
    }

    /**
     * @param title
     * @param headerText
     * @param contentText
     * @param ex
     * @param buttonTypes
     */
    private static void error(String title, String headerText, String contentText, Throwable ex, ButtonType... buttonTypes) {
        System.out.println(ex);
        Alert alert = new Alert(Alert.AlertType.ERROR, contentText, buttonTypes);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(getLogImage());
        if (ex != null) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String exceptionText = sw.toString();
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            alert.getDialogPane().setExpandableContent(textArea);
        }
        alert.showAndWait();
    }


    /**
     * 自定义对话框
     *
     * @param title
     * @param node
     * @param resultConverter
     * @return
     */
    public static <T> Optional<T> customizeDialog(String title,
                                                  Node node,
                                                  Callback<ButtonType, T> resultConverter) {
        return customizeDialog(title, null, null, node, resultConverter, null, ButtonType.OK, ButtonType.CANCEL);
    }

    /**
     * 自定义对话框
     *
     * @param title
     * @param headerText
     * @param node
     * @param resultConverter
     * @return
     */
    public static <T> Optional<T> customizeDialog(String title,
                                                  String headerText,
                                                  Node node,
                                                  Callback<ButtonType, T> resultConverter) {
        return customizeDialog(title, headerText, null, node, resultConverter, null, ButtonType.OK, ButtonType.CANCEL);
    }

    /**
     * 自定义对话框
     *
     * @param title
     * @param headerText
     * @param node
     * @param resultConverter
     * @return
     */
    public static <T> Optional<T> customizeDialog(String title,
                                                  String headerText,
                                                  String contentText,
                                                  Node node,
                                                  Callback<ButtonType, T> resultConverter) {
        return customizeDialog(title, headerText, contentText, node, resultConverter, null, ButtonType.OK, ButtonType.CANCEL);
    }


    private static Optional<ButtonType> alert(Alert.AlertType alertType, String title, String headerText, String contentText, ButtonType... buttonTypes) {
        Alert alert = new Alert(alertType, contentText, buttonTypes);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(getLogImage());
        return alert.showAndWait();
    }

    /**
     * 自定义对话框
     *
     * @param title
     * @param headerText
     * @param node
     * @param resultConverter
     * @param buttonTypes
     * @param <T>
     * @return
     */
    private static <T> Optional<T> customizeDialog(String title,
                                                   String headerText,
                                                   String contentText,
                                                   Node node,
                                                   Callback<ButtonType, T> resultConverter,
                                                   EventHandler<DialogEvent> eventHandler,
                                                   ButtonType... buttonTypes) {
        return createCustomizeDialog(title, headerText, contentText, node, resultConverter, eventHandler, buttonTypes).showAndWait();
    }


    /**
     * 自定义对话框
     *
     * @param title
     * @param headerText
     * @param node
     * @param resultConverter
     * @param buttonTypes
     * @param <T>
     * @return
     */
    private static <T> Dialog<T> createCustomizeDialog(String title,
                                                       String headerText,
                                                       String contentText,
                                                       Node node,
                                                       Callback<ButtonType, T> resultConverter,
                                                       EventHandler<DialogEvent> eventHandler,
                                                       ButtonType... buttonTypes) {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.setOnCloseRequest(eventHandler);
        dialog.getDialogPane().setContent(node);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypes);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(getLogImage());
        if (resultConverter != null) {
            dialog.setResultConverter(resultConverter);
        }
        return dialog;
    }

    public static <T> Optional<T> progressDialog(String title, String headerText, Task<T> task) {
        return progressDialog(title, headerText, task, TaskManger.getExecutor());
    }

    /**
     * 进度条对话框
     *
     * @param title      title
     * @param headerText header
     * @param task       task
     * @param executor   executor
     * @param <T>        任务返回类型
     * @return 任务执行成功，点击完成按钮返回task的执行结果，其他情况返回null
     */
    public static <T> Optional<T> progressDialog(String title, String headerText, Task<T> task, Executor executor) {

        Label msg = new Label();
        msg.textProperty().bind(task.messageProperty());
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(400d);
        progressBar.progressProperty().bind(task.progressProperty());
        VBox box = new VBox(msg, progressBar);
        box.setSpacing(10d);
        Dialog<T> dialog = createCustomizeDialog(title, headerText, null, box, buttonType -> {
            if (buttonType == ButtonType.FINISH) {
                return task.isDone() && !task.isCancelled() ? task.getValue() : null;
            } else {
                if (task.isRunning()) {
                    task.cancel();
                }
            }
            return null;
        }, event -> {
            if (task.isRunning()) {
                task.cancel(true);
            }
        });

        ObservableList<ButtonType> buttonTypes = dialog.getDialogPane().getButtonTypes();
        buttonTypes.add(ButtonType.CANCEL);
        task.stateProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case READY:
                case RUNNING:
                case SCHEDULED:
                    //
                    break;
                case FAILED:
                case CANCELLED:
                    buttonTypes.clear();
                    buttonTypes.add(ButtonType.CLOSE);
                    break;
                case SUCCEEDED:
                    buttonTypes.clear();
                    buttonTypes.add(ButtonType.FINISH);
                    break;
                default:
                    break;
            }

        });
        executor.execute(task);
        return dialog.showAndWait();
    }

    private static Image getLogImage() {
        return ImageUtils.LOGO_IMAGE;
    }

}
