package com.petition.util;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

/**
 * UI动画工具类 v2.0
 * 提供丰富的科技感动画效果
 */
public class AnimationUtil {

    // 时间常量
    public static final Duration INSTANT = Duration.millis(100);
    public static final Duration FAST = Duration.millis(150);
    public static final Duration NORMAL = Duration.millis(250);
    public static final Duration SLOW = Duration.millis(400);
    public static final Duration SLOWER = Duration.millis(600);

    // ==================== 基础动画 ====================

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
    public static void fadeOut(Node node) {
        fadeOut(node, NORMAL, null);
    }

    public static void fadeOut(Node node, Duration duration, Runnable onFinished) {
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(node.getOpacity());
        ft.setToValue(0);
        ft.setInterpolator(Interpolator.EASE_IN);
        if (onFinished != null) ft.setOnFinished(e -> onFinished.run());
        ft.play();
    }

    // ==================== 缩放动画 ====================

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
        st.setFromX(0.8);
        st.setFromY(0.8);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(st, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 缩放退出 */
    public static void scaleOut(Node node, Duration duration, Runnable onFinished) {
        ParallelTransition pt = new ParallelTransition();
        
        ScaleTransition st = new ScaleTransition(duration, node);
        st.setToX(0.8);
        st.setToY(0.8);
        st.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setToValue(0);

        pt.getChildren().addAll(st, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 弹性放大 */
    public static void popIn(Node node) {
        node.setScaleX(0);
        node.setScaleY(0);
        node.setOpacity(1);

        ScaleTransition st = new ScaleTransition(NORMAL, node);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();
    }

    /** 弹性反馈 */
    public static void bounce(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(0.95);
        st.setToY(0.95);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    /** 脉冲动画 */
    public static void pulse(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.05);
        st.setToY(1.05);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.play();
    }

    // ==================== 滑动动画 ====================

    /** 从右侧滑入 */
    public static void slideInFromRight(Node node) {
        slideInFromRight(node, NORMAL, null);
    }

    public static void slideInFromRight(Node node, Duration duration, Runnable onFinished) {
        node.setTranslateX(50);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromX(50);
        tt.setToX(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 从左侧滑入 */
    public static void slideInFromLeft(Node node, Duration duration, Runnable onFinished) {
        node.setTranslateX(-50);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromX(-50);
        tt.setToX(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 从顶部滑入 */
    public static void slideInFromTop(Node node, Duration duration, Runnable onFinished) {
        node.setTranslateY(-30);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromY(-30);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 从底部滑入 */
    public static void slideInFromBottom(Node node, Duration duration, Runnable onFinished) {
        node.setTranslateY(30);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromY(30);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        if (onFinished != null) pt.setOnFinished(e -> onFinished.run());
        pt.play();
    }

    /** 向右滑出 */
    public static void slideOutToRight(Node node, Duration duration, Runnable onFinished) {
        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setToX(50);
        tt.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setToValue(0);

        pt.getChildren().addAll(tt, ft);
        pt.setOnFinished(e -> {
            node.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    /** 向左滑出 */
    public static void slideOutToLeft(Node node, Duration duration, Runnable onFinished) {
        ParallelTransition pt = new ParallelTransition();
        
        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setToX(-50);
        tt.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setToValue(0);

        pt.getChildren().addAll(tt, ft);
        pt.setOnFinished(e -> {
            node.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    // ==================== 交错动画 ====================

    /** 交错淡入 */
    public static void staggerFadeIn(Node... nodes) {
        staggerFadeIn(50, nodes);
    }

    public static void staggerFadeIn(int delayMs, Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setOpacity(0);
            
            FadeTransition ft = new FadeTransition(NORMAL, node);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setDelay(Duration.millis(i * delayMs));
            ft.play();
        }
    }

    /** 交错滑入(从右) */
    public static void staggerSlideIn(Node... nodes) {
        staggerSlideIn(60, nodes);
    }

    public static void staggerSlideIn(int delayMs, Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setTranslateX(30);
            node.setOpacity(0);

            ParallelTransition pt = new ParallelTransition();
            
            TranslateTransition tt = new TranslateTransition(NORMAL, node);
            tt.setFromX(30);
            tt.setToX(0);
            tt.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition ft = new FadeTransition(NORMAL, node);
            ft.setFromValue(0);
            ft.setToValue(1);

            pt.getChildren().addAll(tt, ft);
            pt.setDelay(Duration.millis(i * delayMs));
            pt.play();
        }
    }

    /** 交错缩放进入 */
    public static void staggerScaleIn(int delayMs, Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setScaleX(0.8);
            node.setScaleY(0.8);
            node.setOpacity(0);

            ParallelTransition pt = new ParallelTransition();
            
            ScaleTransition st = new ScaleTransition(NORMAL, node);
            st.setFromX(0.8);
            st.setFromY(0.8);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition ft = new FadeTransition(NORMAL, node);
            ft.setFromValue(0);
            ft.setToValue(1);

            pt.getChildren().addAll(st, ft);
            pt.setDelay(Duration.millis(i * delayMs));
            pt.play();
        }
    }

    // ==================== 特效动画 ====================

    /** 抖动动画(验证失败) */
    public static void shake(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.setOnFinished(e -> node.setTranslateX(0));
        tt.play();
    }

    /** 闪烁动画 */
    public static void flash(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(100), node);
        ft.setFromValue(1);
        ft.setToValue(0.3);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();
    }

    /** 心跳动画 */
    public static void heartbeat(Node node) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(node.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.ZERO, new KeyValue(node.scaleYProperty(), 1.0)),
            new KeyFrame(Duration.millis(140), new KeyValue(node.scaleXProperty(), 1.1)),
            new KeyFrame(Duration.millis(140), new KeyValue(node.scaleYProperty(), 1.1)),
            new KeyFrame(Duration.millis(280), new KeyValue(node.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.millis(280), new KeyValue(node.scaleYProperty(), 1.0)),
            new KeyFrame(Duration.millis(420), new KeyValue(node.scaleXProperty(), 1.1)),
            new KeyFrame(Duration.millis(420), new KeyValue(node.scaleYProperty(), 1.1)),
            new KeyFrame(Duration.millis(560), new KeyValue(node.scaleXProperty(), 1.0)),
            new KeyFrame(Duration.millis(560), new KeyValue(node.scaleYProperty(), 1.0))
        );
        timeline.play();
    }

    /** 旋转进入 */
    public static void rotateIn(Node node) {
        node.setRotate(-90);
        node.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        RotateTransition rt = new RotateTransition(SLOW, node);
        rt.setFromAngle(-90);
        rt.setToAngle(0);
        rt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(SLOW, node);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(rt, ft);
        pt.play();
    }

    // ==================== 弹窗动画 ====================

    /** 弹窗打开动画 */
    public static void dialogOpen(Stage stage) {
        if (stage.getScene() == null) return;
        Node root = stage.getScene().getRoot();
        
        root.setScaleX(0.9);
        root.setScaleY(0.9);
        root.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();
        
        ScaleTransition st = new ScaleTransition(NORMAL, root);
        st.setFromX(0.9);
        st.setFromY(0.9);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(NORMAL, root);
        ft.setFromValue(0);
        ft.setToValue(1);

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
        st.setToX(0.9);
        st.setToY(0.9);
        st.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(FAST, root);
        ft.setToValue(0);

        pt.getChildren().addAll(st, ft);
        pt.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    /** 向导切换动画 - 下一步 */
    public static void wizardNext(Node currentPane, Node nextPane, Runnable onFinished) {
        nextPane.setTranslateX(100);
        nextPane.setOpacity(0);
        nextPane.setVisible(true);

        ParallelTransition pt = new ParallelTransition();

        // 当前面板滑出
        TranslateTransition ttOut = new TranslateTransition(NORMAL, currentPane);
        ttOut.setToX(-100);
        FadeTransition ftOut = new FadeTransition(NORMAL, currentPane);
        ftOut.setToValue(0);

        // 下一面板滑入
        TranslateTransition ttIn = new TranslateTransition(NORMAL, nextPane);
        ttIn.setFromX(100);
        ttIn.setToX(0);
        FadeTransition ftIn = new FadeTransition(NORMAL, nextPane);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);

        pt.getChildren().addAll(ttOut, ftOut, ttIn, ftIn);
        pt.setOnFinished(e -> {
            currentPane.setVisible(false);
            currentPane.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    /** 向导切换动画 - 上一步 */
    public static void wizardPrev(Node currentPane, Node prevPane, Runnable onFinished) {
        prevPane.setTranslateX(-100);
        prevPane.setOpacity(0);
        prevPane.setVisible(true);

        ParallelTransition pt = new ParallelTransition();

        // 当前面板滑出
        TranslateTransition ttOut = new TranslateTransition(NORMAL, currentPane);
        ttOut.setToX(100);
        FadeTransition ftOut = new FadeTransition(NORMAL, currentPane);
        ftOut.setToValue(0);

        // 上一面板滑入
        TranslateTransition ttIn = new TranslateTransition(NORMAL, prevPane);
        ttIn.setFromX(-100);
        ttIn.setToX(0);
        FadeTransition ftIn = new FadeTransition(NORMAL, prevPane);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);

        pt.getChildren().addAll(ttOut, ftOut, ttIn, ftIn);
        pt.setOnFinished(e -> {
            currentPane.setVisible(false);
            currentPane.setTranslateX(0);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    // ==================== 照片切换动画 ====================

    /** 照片淡入切换 */
    public static void crossFade(Node oldNode, Node newNode, Duration duration, Runnable onFinished) {
        newNode.setOpacity(0);
        
        ParallelTransition pt = new ParallelTransition();
        
        FadeTransition ftOut = new FadeTransition(duration, oldNode);
        ftOut.setToValue(0);
        
        FadeTransition ftIn = new FadeTransition(duration, newNode);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);
        
        pt.getChildren().addAll(ftOut, ftIn);
        pt.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    /** 照片缩放切换 */
    public static void zoomTransition(Node oldNode, Node newNode, Runnable onFinished) {
        newNode.setScaleX(1.2);
        newNode.setScaleY(1.2);
        newNode.setOpacity(0);
        
        ParallelTransition pt = new ParallelTransition();
        
        // 旧图缩小淡出
        ScaleTransition stOut = new ScaleTransition(NORMAL, oldNode);
        stOut.setToX(0.8);
        stOut.setToY(0.8);
        FadeTransition ftOut = new FadeTransition(NORMAL, oldNode);
        ftOut.setToValue(0);
        
        // 新图放大淡入
        ScaleTransition stIn = new ScaleTransition(NORMAL, newNode);
        stIn.setFromX(1.2);
        stIn.setFromY(1.2);
        stIn.setToX(1.0);
        stIn.setToY(1.0);
        FadeTransition ftIn = new FadeTransition(NORMAL, newNode);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);
        
        pt.getChildren().addAll(stOut, ftOut, stIn, ftIn);
        pt.setOnFinished(e -> {
            oldNode.setScaleX(1);
            oldNode.setScaleY(1);
            if (onFinished != null) onFinished.run();
        });
        pt.play();
    }

    // ==================== 悬停效果 ====================

    /** 添加悬停缩放效果 */
    public static void addHoverScale(Node node, double scale) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(FAST, node);
            st.setToX(scale);
            st.setToY(scale);
            st.play();
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(FAST, node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    /** 添加悬停发光效果 */
    public static void addHoverGlow(Node node) {
        node.setOnMouseEntered(e -> {
            node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, #00d4ff60, 16, 0, 0, 0);");
        });
        node.setOnMouseExited(e -> {
            node.setStyle(node.getStyle().replace("-fx-effect: dropshadow(gaussian, #00d4ff60, 16, 0, 0, 0);", ""));
        });
    }

    // ==================== 数字动画 ====================

    /** 数字递增动画 */
    public static void animateNumber(javafx.scene.control.Label label, int from, int to, Duration duration) {
        Timeline timeline = new Timeline();
        int frames = 30;
        double step = (to - from) / (double) frames;
        
        for (int i = 0; i <= frames; i++) {
            final int value = (int) (from + step * i);
            KeyFrame kf = new KeyFrame(
                duration.multiply((double) i / frames),
                e -> label.setText(String.valueOf(value))
            );
            timeline.getKeyFrames().add(kf);
        }
        
        // 确保最终值准确
        timeline.getKeyFrames().add(new KeyFrame(duration, e -> label.setText(String.valueOf(to))));
        timeline.play();
    }

    // ==================== 加载动画 ====================

    /** 旋转加载动画 */
    public static RotateTransition createSpinner(Node node) {
        RotateTransition rt = new RotateTransition(Duration.seconds(1), node);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        return rt;
    }

    /** 脉冲加载动画 */
    public static ScaleTransition createPulseLoader(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(600), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        return st;
    }
}

