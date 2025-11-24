package com.petition.util;

import javafx.animation.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Stage窗口工具类
 * 提供统一的弹窗样式和动画效果
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class StageUtil {

    /**
     * 创建并配置一个美化的模态弹窗
     *
     * @param title 窗口标题
     * @param content 窗口内容
     * @param parentStage 父窗口（用于居中显示）
     * @return 配置好的Stage对象
     */
    public static Stage createStyledDialog(String title, Parent content, Stage parentStage) {
        return createStyledDialog(title, content, parentStage, 1200, 800);
    }

    /**
     * 创建并配置一个美化的模态弹窗（自定义尺寸）
     *
     * @param title 窗口标题
     * @param content 窗口内容
     * @param parentStage 父窗口（用于居中显示）
     * @param width 窗口宽度
     * @param height 窗口高度
     * @return 配置好的Stage对象
     */
    public static Stage createStyledDialog(String title, Parent content, Stage parentStage,
                                          double width, double height) {
        // 创建Stage
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);

        // 创建Scene
        Scene scene = new Scene(content);
        stage.setScene(scene);

        // 设置窗口尺寸
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setMinWidth(width * 0.8);
        stage.setMinHeight(height * 0.8);

        // 居中显示
        if (parentStage != null) {
            stage.setX(parentStage.getX() + (parentStage.getWidth() - width) / 2);
            stage.setY(parentStage.getY() + (parentStage.getHeight() - height) / 2);
        }

        // 添加打开动画
        addOpenAnimation(content);

        // 添加ESC键关闭功能
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                stage.close();
            }
        });

        return stage;
    }

    /**
     * 为窗口内容添加打开动画（淡入+缩放）
     *
     * @param content 窗口内容节点
     */
    public static void addOpenAnimation(Parent content) {
        // 设置初始状态
        content.setOpacity(0);
        content.setScaleX(0.9);
        content.setScaleY(0.9);

        // 创建淡入动画
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), content);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // 创建缩放动画
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), content);
        scaleIn.setFromX(0.9);
        scaleIn.setFromY(0.9);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        // 并行播放动画
        ParallelTransition openAnimation = new ParallelTransition(fadeIn, scaleIn);
        openAnimation.setDelay(Duration.millis(50));
        openAnimation.play();
    }

    /**
     * 为窗口内容添加关闭动画（淡出+缩小）
     *
     * @param content 窗口内容节点
     * @param onFinished 动画完成后的回调
     */
    public static void addCloseAnimation(Parent content, Runnable onFinished) {
        // 创建淡出动画
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), content);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // 创建缩小动画
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), content);
        scaleOut.setFromX(1.0);
        scaleOut.setFromY(1.0);
        scaleOut.setToX(0.9);
        scaleOut.setToY(0.9);

        // 并行播放动画
        ParallelTransition closeAnimation = new ParallelTransition(fadeOut, scaleOut);
        closeAnimation.setOnFinished(e -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });
        closeAnimation.play();
    }

    /**
     * 显示带动画的弹窗
     *
     * @param title 窗口标题
     * @param content 窗口内容
     * @param parentStage 父窗口
     */
    public static void showAnimatedDialog(String title, Parent content, Stage parentStage) {
        Stage stage = createStyledDialog(title, content, parentStage);
        stage.show();
    }

    /**
     * 显示带动画的模态弹窗并等待
     *
     * @param title 窗口标题
     * @param content 窗口内容
     * @param parentStage 父窗口
     */
    public static void showAndWaitAnimatedDialog(String title, Parent content, Stage parentStage) {
        Stage stage = createStyledDialog(title, content, parentStage);
        stage.showAndWait();
    }

    /**
     * 为Stage添加淡入动画
     *
     * @param stage 目标窗口
     */
    public static void addStageFadeIn(Stage stage) {
        stage.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), stage.getScene().getRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        stage.setOnShown(e -> fadeIn.play());
    }
}
