package com.petition.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 现代化弹窗工具类 v2.0
 * 提供统一的弹窗管理和丰富的弹窗类型
 */
public class DialogUtil {

    private static final String CSS_PATH = "/css/main.css";

    // ==================== 表单弹窗 ====================

    /**
     * 创建标准表单弹窗
     */
    public static <T> Stage createFormDialog(
            Window owner, String fxmlPath, String title,
            double width, double height, Consumer<T> onController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = DialogUtil.class.getResource(fxmlPath);
            if (url == null) throw new IOException("找不到FXML文件: " + fxmlPath);
            loader.setLocation(url);
            Parent root = loader.load();

            if (onController != null) {
                T ctrl = loader.getController();
                onController.accept(ctrl);
            }

            Scene scene = new Scene(root, width, height);
            scene.setFill(Color.TRANSPARENT);
            applyTheme(scene);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(owner);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinWidth(width * 0.8);
            stage.setMinHeight(height * 0.8);

            centerOnOwner(stage, owner, width, height);

            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) closeWithAnimation(stage);
            });

            stage.setOnShown(e -> AnimationUtil.dialogOpen(stage));
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("加载失败", "无法加载表单: " + e.getMessage());
            return null;
        }
    }

    /**
     * 创建自定义内容弹窗
     */
    public static Stage createCustomDialog(Window owner, String title, Node content, 
                                           double width, double height) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dialog-pane");
        root.setCenter(content);

        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        applyTheme(scene);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle(title);
        stage.setScene(scene);

        centerOnOwner(stage, owner, width, height);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) closeWithAnimation(stage);
        });

        stage.setOnShown(e -> AnimationUtil.dialogOpen(stage));
        return stage;
    }

    // ==================== 提示框 ====================

    /**
     * 信息提示
     */
    public static void showInfoAlert(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, "ℹ " + title, message);
    }

    /**
     * 成功提示
     */
    public static void showSuccessAlert(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, "✓ " + title, message);
    }

    /**
     * 警告提示
     */
    public static void showWarningAlert(String title, String message) {
        showAlert(Alert.AlertType.WARNING, "⚠ " + title, message);
    }

    /**
     * 错误提示
     */
    public static void showErrorAlert(String title, String message) {
        showAlert(Alert.AlertType.ERROR, "✗ " + title, message);
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyDarkTheme(alert);
        alert.showAndWait();
    }

    // ==================== 确认框 ====================

    /**
     * 确认对话框
     */
    public static void showConfirmDialog(String title, String message, 
                                         Runnable onConfirm, Runnable onCancel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyDarkTheme(alert);
        
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                if (onConfirm != null) onConfirm.run();
            } else {
                if (onCancel != null) onCancel.run();
            }
        });
    }

    /**
     * 删除确认对话框
     */
    public static void showDeleteConfirmDialog(String itemName, Runnable onConfirm) {
        showConfirmDialog(
            "⚠ 确认删除",
            "确定要删除「" + itemName + "」吗？\n此操作不可恢复！",
            onConfirm,
            null
        );
    }

    /**
     * 带输入框的确认对话框
     */
    public static Optional<String> showInputDialog(String title, String header, 
                                                   String defaultValue, String promptText) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(promptText);
        applyDarkTheme(dialog);
        return dialog.showAndWait();
    }

    // ==================== Toast提示 ====================

    /**
     * 显示Toast消息
     */
    public static void showToast(Window owner, String message, ToastType type) {
        showToast(owner, message, type, Duration.seconds(3));
    }

    public static void showToast(Window owner, String message, ToastType type, Duration duration) {
        Stage toast = new Stage();
        toast.initStyle(StageStyle.TRANSPARENT);
        toast.initOwner(owner);
        toast.setAlwaysOnTop(true);

        String icon = switch (type) {
            case SUCCESS -> "✓";
            case ERROR -> "✗";
            case WARNING -> "⚠";
            case INFO -> "ℹ";
        };

        String color = switch (type) {
            case SUCCESS -> "#00ff88";
            case ERROR -> "#ff0066";
            case WARNING -> "#ffaa00";
            case INFO -> "#00d4ff";
        };

        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(16, 24, 16, 24));
        content.setStyle(
            "-fx-background-color: #1a1f35f0; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: " + color + "40; " +
            "-fx-border-radius: 12; " +
            "-fx-border-width: 1; " +
            "-fx-effect: dropshadow(gaussian, #00000080, 16, 0, 0, 4);"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: " + color + ";");

        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e2e8f0;");

        content.getChildren().addAll(iconLabel, msgLabel);

        Scene scene = new Scene(content);
        scene.setFill(Color.TRANSPARENT);

        toast.setScene(scene);

        // 定位到窗口底部中央
        if (owner != null) {
            toast.setX(owner.getX() + (owner.getWidth() - 300) / 2);
            toast.setY(owner.getY() + owner.getHeight() - 100);
        }

        toast.show();

        // 动画
        content.setOpacity(0);
        content.setTranslateY(20);
        
        AnimationUtil.slideInFromBottom(content, AnimationUtil.NORMAL, null);

        // 自动关闭
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(duration);
        pause.setOnFinished(e -> {
            AnimationUtil.fadeOut(content, AnimationUtil.FAST, toast::close);
        });
        pause.play();
    }

    public enum ToastType {
        SUCCESS, ERROR, WARNING, INFO
    }

    // ==================== 图片查看器 ====================

    /**
     * 显示图片查看器
     */
    public static void showImageViewer(Window owner, List<String> imagePaths, int startIndex) {
        if (imagePaths == null || imagePaths.isEmpty()) return;

        Stage viewer = new Stage();
        viewer.initStyle(StageStyle.DECORATED);
        viewer.initModality(Modality.APPLICATION_MODAL);
        viewer.initOwner(owner);
        viewer.setTitle("照片查看器");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0e1a;");

        // 主图显示
        ImageView mainImage = new ImageView();
        mainImage.setPreserveRatio(true);
        mainImage.setFitWidth(700);
        mainImage.setFitHeight(500);

        StackPane imagePane = new StackPane(mainImage);
        imagePane.setStyle("-fx-background-color: #0d1224;");
        imagePane.setPadding(new Insets(20));

        // 导航按钮
        Button prevBtn = new Button("◀");
        prevBtn.getStyleClass().addAll("btn", "btn-ghost");
        prevBtn.setStyle("-fx-font-size: 24px;");

        Button nextBtn = new Button("▶");
        nextBtn.getStyleClass().addAll("btn", "btn-ghost");
        nextBtn.setStyle("-fx-font-size: 24px;");

        // 计数
        Label counter = new Label();
        counter.getStyleClass().addAll("text-secondary");

        final int[] currentIdx = {Math.max(0, Math.min(startIndex, imagePaths.size() - 1))};

        Runnable loadImage = () -> {
            int idx = currentIdx[0];
            try {
                Image img = new Image("file:" + imagePaths.get(idx));
                mainImage.setImage(img);
            } catch (Exception e) {
                mainImage.setImage(null);
            }
            counter.setText((idx + 1) + " / " + imagePaths.size());
            prevBtn.setDisable(idx == 0);
            nextBtn.setDisable(idx == imagePaths.size() - 1);
        };

        prevBtn.setOnAction(e -> {
            if (currentIdx[0] > 0) {
                currentIdx[0]--;
                loadImage.run();
            }
        });

        nextBtn.setOnAction(e -> {
            if (currentIdx[0] < imagePaths.size() - 1) {
                currentIdx[0]++;
                loadImage.run();
            }
        });

        loadImage.run();

        HBox toolbar = new HBox(16);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(16));
        toolbar.setStyle("-fx-background-color: #0d122480;");
        toolbar.getChildren().addAll(prevBtn, counter, nextBtn);

        root.setCenter(imagePane);
        root.setBottom(toolbar);

        Scene scene = new Scene(root, 800, 650);
        applyTheme(scene);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> { if (currentIdx[0] > 0) { currentIdx[0]--; loadImage.run(); } }
                case RIGHT -> { if (currentIdx[0] < imagePaths.size() - 1) { currentIdx[0]++; loadImage.run(); } }
                case ESCAPE -> viewer.close();
            }
        });

        viewer.setScene(scene);
        viewer.show();
    }

    // ==================== 文件选择 ====================

    /**
     * 选择图片文件
     */
    public static List<File> chooseImageFiles(Window owner, boolean multiple) {
        FileChooser fc = new FileChooser();
        fc.setTitle("选择图片");
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("图片文件", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("所有文件", "*.*")
        );

        if (multiple) {
            return fc.showOpenMultipleDialog(owner);
        } else {
            File file = fc.showOpenDialog(owner);
            return file != null ? List.of(file) : null;
        }
    }

    /**
     * 选择保存位置
     */
    public static File chooseSaveFile(Window owner, String title, String defaultName, 
                                       String... extensions) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.setInitialFileName(defaultName);
        
        if (extensions.length > 0) {
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("支持的格式", extensions)
            );
        }

        return fc.showSaveDialog(owner);
    }

    // ==================== 辅助方法 ====================

    /**
     * 关闭弹窗（带动画）
     */
    public static void closeWithAnimation(Stage stage) {
        AnimationUtil.dialogClose(stage, stage::close);
    }

    /**
     * 应用主题样式
     */
    private static void applyTheme(Scene scene) {
        try {
            URL cssUrl = DialogUtil.class.getResource(CSS_PATH);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("无法加载CSS: " + e.getMessage());
        }
    }

    /**
     * 应用暗色主题到Alert
     */
    private static void applyDarkTheme(Dialog<?> dialog) {
        DialogPane pane = dialog.getDialogPane();
        pane.setStyle(
            "-fx-background-color: #1a1f35; " +
            "-fx-border-color: #2d3a5a; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12;"
        );
        
        try {
            URL cssUrl = DialogUtil.class.getResource(CSS_PATH);
            if (cssUrl != null) {
                pane.getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception ignored) {}
    }

    /**
     * 居中显示在父窗口
     */
    private static void centerOnOwner(Stage stage, Window owner, double width, double height) {
        if (owner != null) {
            stage.setX(owner.getX() + (owner.getWidth() - width) / 2);
            stage.setY(owner.getY() + (owner.getHeight() - height) / 2);
        }
    }
}

