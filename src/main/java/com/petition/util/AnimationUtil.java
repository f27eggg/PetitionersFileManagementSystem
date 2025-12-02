package com.petition.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * UI动画工具类
 * 提供丰富的动画效果用于增强用户体验
 */
public class AnimationUtil {

    public static final Duration FAST = Duration.millis(150);
    public static final Duration NORMAL = Duration.millis(250);
    public static final Duration SLOW = Duration.millis(400);

    /** 淡入动画 */
    public static void fadeIn(Node node) {
        fadeIn(node, NORMAL, null);
    }

    public static void fadeIn(Node node, Duration duration, Runnable onFinished) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setInterpolator(Interpolator.EASE_OUT);
        if (onFinished != null) ft.setOnFinished(e -> onFinished.run());
        ft.play();
    }

    /** 淡出动画 */
    public static void fadeOut(Node node, Duration duration, Runnable onFinished) {
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(node.getOpacity());
        ft.setToValue(0);
        ft.setInterpolator(Interpolator.EASE_IN);
        if (onFinished != null) ft.setOnFinished(e -> onFinished.run());
        ft.play();
    }

    /** 缩放进入 */
    public static void scaleIn(Node node) {
        scaleIn(node, NORMAL, null);
    }

    public static void scaleIn(Node node, Duration duration, Runnable onFinished) {
        node.setScaleX(0.8);
        node.setScaleY(0.8);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        ScaleTransition st = new ScaleTransition(duration, node);
        st.setFromX(0.8); st.setFromY(0.8);
        st.setToX(1.0); st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0); ft.setToValue(1);

        pt.getChildren().addAll(st, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 弹性反馈 */
    public static void bounce(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setFromX(1.0); st.setFromY(1.0);
        st.setToX(0.95); st.setToY(0.95);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    /** 抖动动画(验证失败) */
    public static void shake(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0); tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.setOnFinished(e -> node.setTranslateX(0));
        tt.play();
    }

    /** 从右侧滑入 */
    public static void slideInFromRight(Node node) {
        node.setTranslateX(50);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        TranslateTransition tt = new TranslateTransition(NORMAL, node);
        tt.setFromX(50); tt.setToX(0);

        FadeTransition ft = new FadeTransition(NORMAL, node);
        ft.setFromValue(0); ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        pt.play();
    }

    /** 交错淡入 */
    public static void staggerFadeIn(Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setOpacity(0);
            FadeTransition ft = new FadeTransition(NORMAL, node);
            ft.setFromValue(0); ft.setToValue(1);
            ft.setDelay(Duration.millis(i * 50));
            ft.play();
        }
    }

    /** 弹窗打开动画 */
    public static void dialogOpen(Stage stage) {
        if (stage.getScene() == null) return;
        Node root = stage.getScene().getRoot();
        root.setScaleX(0.9); root.setScaleY(0.9);
        root.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        ScaleTransition st = new ScaleTransition(NORMAL, root);
        st.setFromX(0.9); st.setFromY(0.9);
        st.setToX(1.0); st.setToY(1.0);

        FadeTransition ft = new FadeTransition(NORMAL, root);
        ft.setFromValue(0); ft.setToValue(1);

        pt.getChildren().addAll(st, ft);
        pt.setDelay(Duration.millis(50));
        pt.play();
    }

    /** 弹窗关闭动画 */
    public static void dialogClose(Stage stage, Runnable onFinished) {
        if (stage.getScene() == null) {
            if (onFinished != null) onFinished.run();
            return;
        }
        Node root = stage.getScene().getRoot();

        ParallelTransition pt = new ParallelTransition();
        ScaleTransition st = new ScaleTransition(FAST, root);
        st.setToX(0.9); st.setToY(0.9);

        FadeTransition ft = new FadeTransition(FAST, root);
        ft.setToValue(0);

        pt.getChildren().addAll(st, ft);
        pt.setOnFinished(e -> { if (onFinished != null) onFinished.run(); });
        pt.play();
    }

    /** 添加悬停缩放 */
    public static void addHoverScale(Node node, double scale) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(FAST, node);
            st.setToX(scale); st.setToY(scale);
            st.play();
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(FAST, node);
            st.setToX(1.0); st.setToY(1.0);
            st.play();
        });
    }
}
