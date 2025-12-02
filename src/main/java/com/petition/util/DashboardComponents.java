package com.petition.util;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 仪表盘组件库
 * 提供科技感强的数据可视化组件
 */
public class DashboardComponents {

    // ==================== 数据指标卡片 ====================

    /**
     * 创建数据指标卡片
     */
    public static VBox createMetricCard(String title, String value, String trend, String type) {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("metric-card", "metric-card-" + type);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(24));
        card.setMinWidth(200);
        card.setMinHeight(130);

        // 标题行
        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("metric-label");
        
        // 脉冲指示器
        Circle pulse = new Circle(4);
        pulse.getStyleClass().add("pulse-indicator");
        if ("danger".equals(type) || "warning".equals(type)) {
            pulse.getStyleClass().add("pulse-indicator-" + type);
        }
        startPulseAnimation(pulse);
        
        header.getChildren().addAll(titleLabel, pulse);

        // 数值
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().addAll("metric-value", "metric-value-" + type);

        // 趋势
        HBox trendBox = new HBox(8);
        trendBox.setAlignment(Pos.CENTER_LEFT);
        
        if (trend != null && !trend.isEmpty()) {
            boolean isUp = trend.startsWith("+") || trend.startsWith("↑");
            Label trendLabel = new Label(trend);
            trendLabel.getStyleClass().addAll("metric-trend", isUp ? "metric-trend-up" : "metric-trend-down");
            trendBox.getChildren().add(trendLabel);
        }

        card.getChildren().addAll(header, valueLabel, trendBox);

        // 悬停效果
        AnimationUtil.addHoverScale(card, 1.03);

        return card;
    }

    /**
     * 启动脉冲动画
     */
    private static void startPulseAnimation(Circle circle) {
        ScaleTransition st = new ScaleTransition(Duration.millis(1000), circle);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.5);
        st.setToY(1.5);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        
        FadeTransition ft = new FadeTransition(Duration.millis(1000), circle);
        ft.setFromValue(1.0);
        ft.setToValue(0.5);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);

        ParallelTransition pt = new ParallelTransition(st, ft);
        pt.play();
    }

    // ==================== 概览统计条 ====================

    /**
     * 创建顶部概览统计条
     */
    public static HBox createOverviewBar(int total, int highRisk, int thisMonth, int pending) {
        HBox bar = new HBox(32);
        bar.getStyleClass().add("dashboard-overview-bar");
        bar.setAlignment(Pos.CENTER);

        bar.getChildren().addAll(
            createMetricCard("总人数", String.valueOf(total), null, "primary"),
            createMetricCard("高风险", String.valueOf(highRisk), highRisk > 10 ? "↑ 需关注" : null, "danger"),
            createMetricCard("本月新增", String.valueOf(thisMonth), "+" + thisMonth + " 本月", "warning"),
            createMetricCard("待处理", String.valueOf(pending), pending > 0 ? "需跟进" : "已完成", "success")
        );

        // 交错动画
        AnimationUtil.staggerScaleIn(60, bar.getChildren().toArray(new Node[0]));

        return bar;
    }

    // ==================== 数据面板 ====================

    /**
     * 创建数据面板
     */
    public static VBox createDataPanel(String title, String subtitle, Node content) {
        VBox panel = new VBox(16);
        panel.getStyleClass().add("data-panel");

        // 头部
        VBox header = new VBox(4);
        header.getStyleClass().add("data-panel-header");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("data-panel-title");

        if (subtitle != null) {
            Label subtitleLabel = new Label(subtitle);
            subtitleLabel.getStyleClass().add("data-panel-subtitle");
            header.getChildren().addAll(titleLabel, subtitleLabel);
        } else {
            header.getChildren().add(titleLabel);
        }

        panel.getChildren().addAll(header, content);

        return panel;
    }

    // ==================== 快捷操作卡片 ====================

    /**
     * 创建快捷操作卡片
     */
    public static VBox createQuickActionCard(String icon, String label, Runnable action) {
        VBox card = new VBox(8);
        card.getStyleClass().add("quick-action-card");
        card.setAlignment(Pos.CENTER);
        card.setMinSize(120, 100);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("quick-action-icon");

        Label textLabel = new Label(label);
        textLabel.getStyleClass().add("quick-action-label");

        card.getChildren().addAll(iconLabel, textLabel);
        
        card.setOnMouseClicked(e -> {
            AnimationUtil.bounce(card);
            if (action != null) action.run();
        });

        AnimationUtil.addHoverScale(card, 1.05);

        return card;
    }

    /**
     * 创建快捷操作网格
     */
    public static FlowPane createQuickActionsGrid(Map<String, Runnable> actions) {
        FlowPane grid = new FlowPane(16, 16);
        grid.setAlignment(Pos.CENTER);

        // 预定义图标
        String[] icons = {"➕", "🔍", "📊", "📋", "⚙️", "📤", "📥", "🔔"};
        int iconIndex = 0;

        for (Map.Entry<String, Runnable> entry : actions.entrySet()) {
            String icon = iconIndex < icons.length ? icons[iconIndex++] : "▪";
            grid.getChildren().add(createQuickActionCard(icon, entry.getKey(), entry.getValue()));
        }

        return grid;
    }

    // ==================== 实时时钟 ====================

    /**
     * 创建实时时钟组件
     */
    public static VBox createRealtimeClock() {
        VBox clock = new VBox(4);
        clock.setAlignment(Pos.CENTER_RIGHT);

        Label timeLabel = new Label();
        timeLabel.getStyleClass().addAll("text-2xl", "font-bold", "text-accent");

        Label dateLabel = new Label();
        dateLabel.getStyleClass().addAll("text-sm", "text-muted");

        clock.getChildren().addAll(timeLabel, dateLabel);

        // 更新时间
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 EEEE", Locale.CHINESE);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(timeFormatter));
            dateLabel.setText(now.format(dateFormatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // 立即更新一次
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(now.format(timeFormatter));
        dateLabel.setText(now.format(dateFormatter));

        return clock;
    }

    // ==================== 风险等级分布 ====================

    /**
     * 创建风险等级分布图
     */
    public static VBox createRiskDistributionChart(int low, int medium, int high, int extreme) {
        VBox container = new VBox(16);

        int total = low + medium + high + extreme;
        if (total == 0) total = 1;

        // 进度条形式
        HBox bars = new HBox(4);
        bars.setMinHeight(24);
        bars.setMaxHeight(24);
        bars.setStyle("-fx-background-radius: 12;");

        Region lowBar = createColorBar("#00ff88", (double) low / total);
        Region mediumBar = createColorBar("#ffaa00", (double) medium / total);
        Region highBar = createColorBar("#ff4444", (double) high / total);
        Region extremeBar = createColorBar("#ff0066", (double) extreme / total);

        bars.getChildren().addAll(lowBar, mediumBar, highBar, extremeBar);

        // 图例
        HBox legend = new HBox(24);
        legend.setAlignment(Pos.CENTER);

        legend.getChildren().addAll(
            createLegendItem("低风险", "#00ff88", low),
            createLegendItem("中风险", "#ffaa00", medium),
            createLegendItem("高风险", "#ff4444", high),
            createLegendItem("极高风险", "#ff0066", extreme)
        );

        container.getChildren().addAll(bars, legend);

        // 动画效果
        animateBar(lowBar, (double) low / total);
        animateBar(mediumBar, (double) medium / total);
        animateBar(highBar, (double) high / total);
        animateBar(extremeBar, (double) extreme / total);

        return container;
    }

    private static Region createColorBar(String color, double ratio) {
        Region bar = new Region();
        bar.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 12;");
        bar.setMinHeight(24);
        bar.setMaxHeight(24);
        HBox.setHgrow(bar, Priority.ALWAYS);
        bar.setMaxWidth(0); // 初始宽度为0，用于动画
        return bar;
    }

    private static void animateBar(Region bar, double targetRatio) {
        // 延迟动画让各条依次出现
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(bar.maxWidthProperty(), 0)),
            new KeyFrame(Duration.millis(800), new KeyValue(bar.maxWidthProperty(), 2000 * targetRatio, Interpolator.EASE_OUT))
        );
        timeline.setDelay(Duration.millis(300));
        timeline.play();
    }

    private static HBox createLegendItem(String label, String color, int count) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);

        Circle dot = new Circle(6);
        dot.setFill(Color.web(color));
        dot.setEffect(new DropShadow(8, Color.web(color + "80")));

        Label text = new Label(label + " (" + count + ")");
        text.getStyleClass().addAll("text-sm", "text-secondary");

        item.getChildren().addAll(dot, text);
        return item;
    }

    // ==================== 活动时间线 ====================

    /**
     * 创建活动时间线
     */
    public static VBox createActivityTimeline(List<ActivityItem> items) {
        VBox timeline = new VBox(0);

        for (int i = 0; i < items.size(); i++) {
            ActivityItem item = items.get(i);
            boolean isLast = i == items.size() - 1;
            timeline.getChildren().add(createTimelineItem(item, isLast));
        }

        return timeline;
    }

    public static class ActivityItem {
        public String time;
        public String title;
        public String description;
        public String type; // info, success, warning, danger

        public ActivityItem(String time, String title, String description, String type) {
            this.time = time;
            this.title = title;
            this.description = description;
            this.type = type;
        }
    }

    private static HBox createTimelineItem(ActivityItem item, boolean isLast) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(0, 0, 16, 0));

        // 时间线指示器
        VBox indicator = new VBox(0);
        indicator.setAlignment(Pos.TOP_CENTER);
        indicator.setMinWidth(24);

        Circle dot = new Circle(6);
        String color = switch (item.type) {
            case "success" -> "#00ff88";
            case "warning" -> "#ffaa00";
            case "danger" -> "#ff0066";
            default -> "#00d4ff";
        };
        dot.setFill(Color.web(color));
        dot.setEffect(new DropShadow(6, Color.web(color + "60")));

        indicator.getChildren().add(dot);

        if (!isLast) {
            Region line = new Region();
            line.setMinWidth(2);
            line.setMaxWidth(2);
            line.setMinHeight(40);
            line.setStyle("-fx-background-color: #2d3a5a;");
            VBox.setMargin(line, new Insets(8, 0, 0, 0));
            indicator.getChildren().add(line);
        }

        // 内容
        VBox content = new VBox(4);
        HBox.setHgrow(content, Priority.ALWAYS);

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(item.title);
        titleLabel.getStyleClass().addAll("text-base", "text-primary");

        Label timeLabel = new Label(item.time);
        timeLabel.getStyleClass().addAll("text-xs", "text-muted");

        titleRow.getChildren().addAll(titleLabel, timeLabel);

        Label descLabel = new Label(item.description);
        descLabel.getStyleClass().addAll("text-sm", "text-secondary");
        descLabel.setWrapText(true);

        content.getChildren().addAll(titleRow, descLabel);

        row.getChildren().addAll(indicator, content);
        return row;
    }

    // ==================== 迷你图表 ====================

    /**
     * 创建迷你折线图
     */
    public static StackPane createMiniLineChart(List<Double> data, String color) {
        StackPane container = new StackPane();
        container.setMinSize(120, 40);
        container.setMaxSize(120, 40);

        if (data == null || data.isEmpty()) return container;

        // 归一化数据
        double max = data.stream().mapToDouble(d -> d).max().orElse(1);
        double min = data.stream().mapToDouble(d -> d).min().orElse(0);
        double range = max - min;
        if (range == 0) range = 1;

        Polyline line = new Polyline();
        line.setStroke(Color.web(color));
        line.setStrokeWidth(2);
        line.setFill(Color.TRANSPARENT);

        double width = 120;
        double height = 40;
        double stepX = width / (data.size() - 1);

        for (int i = 0; i < data.size(); i++) {
            double x = i * stepX;
            double y = height - ((data.get(i) - min) / range) * height * 0.8 - height * 0.1;
            line.getPoints().addAll(x, y);
        }

        // 渐变填充
        Polygon fill = new Polygon();
        fill.setFill(Color.web(color + "20"));
        fill.getPoints().addAll(0.0, height);
        for (int i = 0; i < data.size(); i++) {
            double x = i * stepX;
            double y = height - ((data.get(i) - min) / range) * height * 0.8 - height * 0.1;
            fill.getPoints().addAll(x, y);
        }
        fill.getPoints().addAll(width, height);

        container.getChildren().addAll(fill, line);

        return container;
    }

    // ==================== 系统状态指示器 ====================

    /**
     * 创建系统状态指示器
     */
    public static HBox createSystemStatus(boolean online, int activeUsers, String lastSync) {
        HBox status = new HBox(24);
        status.setAlignment(Pos.CENTER_LEFT);

        // 在线状态
        HBox onlineStatus = new HBox(8);
        onlineStatus.setAlignment(Pos.CENTER_LEFT);
        
        Circle statusDot = new Circle(5);
        statusDot.setFill(online ? Color.web("#00ff88") : Color.web("#ff0066"));
        statusDot.setEffect(new DropShadow(6, online ? Color.web("#00ff8860") : Color.web("#ff006660")));
        if (online) startPulseAnimation(statusDot);
        
        Label statusLabel = new Label(online ? "系统在线" : "系统离线");
        statusLabel.getStyleClass().addAll("text-sm", online ? "text-success" : "text-danger");
        
        onlineStatus.getChildren().addAll(statusDot, statusLabel);

        // 活跃用户
        Label usersLabel = new Label("👤 " + activeUsers + " 人在线");
        usersLabel.getStyleClass().addAll("text-sm", "text-secondary");

        // 最后同步
        Label syncLabel = new Label("🔄 " + lastSync);
        syncLabel.getStyleClass().addAll("text-sm", "text-muted");

        status.getChildren().addAll(onlineStatus, usersLabel, syncLabel);

        return status;
    }
}

