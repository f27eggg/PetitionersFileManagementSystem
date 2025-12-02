package com.petition.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * 现代化弹窗工具类
 */
public class DialogUtil {

    private static final String MAIN_CSS = "/css/main.css";

    /** 创建表单弹窗 */
    public static <T> Stage createFormDialog(
            Window owner, String fxmlPath, String title,
            double width, double height, Consumer<T> onController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = DialogUtil.class.getResource(fxmlPath);
            if (url == null) throw new IOException("找不到: " + fxmlPath);
            loader.setLocation(url);
            Parent root = loader.load();

            if (onController != null) {
                T ctrl = loader.getController();
                onController.accept(ctrl);
            }

            Scene scene = new Scene(root, width, height);
            scene.setFill(Color.TRANSPARENT);
            
            URL css = DialogUtil.class.getResource(MAIN_CSS);
            if (css != null) scene.getStylesheets().add(css.toExternalForm());

            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinWidth(width * 0.8);
            stage.setMinHeight(height * 0.8);

            if (owner != null) {
                stage.setX(owner.getX() + (owner.getWidth() - width) / 2);
                stage.setY(owner.getY() + (owner.getHeight() - height) / 2);
            }

            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) closeWithAnimation(stage);
            });

            stage.setOnShown(e -> AnimationUtil.dialogOpen(stage));
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("加载失败", e.getMessage());
            return null;
        }
    }

    /** 关闭弹窗(带动画) */
    public static void closeWithAnimation(Stage stage) {
        AnimationUtil.dialogClose(stage, stage::close);
    }

    /** 确认对话框 */
    public static void showConfirmDialog(String title, String msg, Runnable onConfirm, Runnable onCancel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        applyDarkTheme(alert);
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK && onConfirm != null) onConfirm.run();
            else if (onCancel != null) onCancel.run();
        });
    }

    /** 信息提示 */
    public static void showInfoAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        applyDarkTheme(alert);
        alert.showAndWait();
    }

    /** 成功提示 */
    public static void showSuccessAlert(String title, String msg) {
        showInfoAlert("✅ " + title, msg);
    }

    /** 错误提示 */
    public static void showErrorAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ " + title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        applyDarkTheme(alert);
        alert.showAndWait();
    }

    /** 警告提示 */
    public static void showWarningAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("⚠️ " + title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        applyDarkTheme(alert);
        alert.showAndWait();
    }

    /** 删除确认 */
    public static void showDeleteConfirmDialog(String name, Runnable onConfirm) {
        showConfirmDialog("⚠️ 确认删除", 
            "确定要删除「" + name + "」吗？\n此操作不可恢复！", 
            onConfirm, null);
    }

    private static void applyDarkTheme(Alert alert) {
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("-fx-background-color: #1e293b; -fx-border-color: #334155;");
        try {
            URL css = DialogUtil.class.getResource(MAIN_CSS);
            if (css != null) pane.getStylesheets().add(css.toExternalForm());
        } catch (Exception ignored) {}
    }
}
