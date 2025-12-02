package com.petition.controller;

import com.petition.util.AnimationUtil;
import com.petition.util.DashboardComponents;
import com.petition.util.DashboardComponents.ActivityItem;
import com.petition.util.NavIcon;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.*;

/**
 * 仪表盘控制器示例
 * 展示如何使用科技感仪表盘组件
 */
public class DashboardControllerExample implements Initializable {

    @FXML private VBox dashboardContainer;
    @FXML private VBox sidebarNav;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSidebar();
        buildDashboard();
    }

    /**
     * 设置侧边栏导航（解决乱码问题）
     */
    private void setupSidebar() {
        if (sidebarNav == null) return;

        sidebarNav.getChildren().clear();
        sidebarNav.setSpacing(4);
        sidebarNav.setPadding(new Insets(8, 0, 8, 0));

        // 主要功能
        Label mainLabel = new Label("主要功能");
        mainLabel.getStyleClass().add("nav-section-title");
        sidebarNav.getChildren().add(mainLabel);

        addNavItem(sidebarNav, "仪表盘", true);
        addNavItem(sidebarNav, "人员管理", false);
        addNavItem(sidebarNav, "高级搜索", false);
        addNavItem(sidebarNav, "数据统计", false);

        // 系统管理
        Label systemLabel = new Label("系统管理");
        systemLabel.getStyleClass().add("nav-section-title");
        systemLabel.setPadding(new Insets(16, 0, 0, 0));
        sidebarNav.getChildren().add(systemLabel);

        addNavItem(sidebarNav, "用户管理", false);
        addNavItem(sidebarNav, "系统设置", false);  // 使用NavIcon避免乱码
        addNavItem(sidebarNav, "退出登录", false);
    }

    /**
     * 添加导航项（使用NavIcon解决乱码）
     */
    private void addNavItem(VBox container, String name, boolean active) {
        HBox navItem = new HBox(14);
        navItem.setAlignment(Pos.CENTER_LEFT);
        navItem.setPadding(new Insets(14, 20, 14, 20));
        navItem.getStyleClass().add("nav-button");
        if (active) {
            navItem.getStyleClass().add("nav-button-active");
        }
        navItem.setCursor(javafx.scene.Cursor.HAND);

        // 使用NavIcon获取图标（避免乱码）
        Label icon = NavIcon.createNavIconLabel(name);
        Label text = new Label(name);
        text.getStyleClass().add(active ? "text-accent" : "text-secondary");

        navItem.getChildren().addAll(icon, text);

        // 悬停效果
        navItem.setOnMouseEntered(e -> {
            if (!navItem.getStyleClass().contains("nav-button-active")) {
                navItem.setStyle("-fx-background-color: linear-gradient(from 0% 50% to 100% 50%, #00d4ff15, transparent);");
            }
        });
        navItem.setOnMouseExited(e -> {
            if (!navItem.getStyleClass().contains("nav-button-active")) {
                navItem.setStyle("");
            }
        });

        container.getChildren().add(navItem);
    }

    /**
     * 构建仪表盘
     */
    private void buildDashboard() {
        if (dashboardContainer == null) return;

        dashboardContainer.getChildren().clear();
        dashboardContainer.setSpacing(24);
        dashboardContainer.setPadding(new Insets(0));

        // 1. 顶部欢迎区域和实时时钟
        HBox welcomeBar = createWelcomeBar();
        dashboardContainer.getChildren().add(welcomeBar);

        // 2. 数据概览卡片
        HBox overviewBar = DashboardComponents.createOverviewBar(1234, 56, 23, 12);
        dashboardContainer.getChildren().add(overviewBar);

        // 3. 主内容区（两列布局）
        HBox mainContent = new HBox(24);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // 左侧 - 风险分布和快捷操作
        VBox leftColumn = new VBox(24);
        leftColumn.setMinWidth(400);
        HBox.setHgrow(leftColumn, Priority.ALWAYS);

        // 风险分布面板
        VBox riskDistribution = DashboardComponents.createRiskDistributionChart(456, 234, 89, 23);
        VBox riskPanel = DashboardComponents.createDataPanel("风险等级分布", "实时数据统计", riskDistribution);
        leftColumn.getChildren().add(riskPanel);

        // 快捷操作面板
        Map<String, Runnable> quickActions = new LinkedHashMap<>();
        quickActions.put("新增人员", () -> System.out.println("新增人员"));
        quickActions.put("快速搜索", () -> System.out.println("快速搜索"));
        quickActions.put("数据导出", () -> System.out.println("数据导出"));
        quickActions.put("生成报表", () -> System.out.println("生成报表"));
        
        FlowPane actionsGrid = DashboardComponents.createQuickActionsGrid(quickActions);
        VBox actionsPanel = DashboardComponents.createDataPanel("快捷操作", null, actionsGrid);
        leftColumn.getChildren().add(actionsPanel);

        // 右侧 - 最近活动
        VBox rightColumn = new VBox(24);
        rightColumn.setMinWidth(350);
        HBox.setHgrow(rightColumn, Priority.SOMETIMES);

        // 最近活动时间线
        List<ActivityItem> activities = Arrays.asList(
            new ActivityItem("10:30", "新增人员", "王某某 被添加到系统", "success"),
            new ActivityItem("09:45", "风险升级", "李某某 风险等级升至高风险", "danger"),
            new ActivityItem("09:15", "信息更新", "张某某 联系方式已更新", "info"),
            new ActivityItem("08:30", "处理完成", "赵某某 案件已处理完毕", "success"),
            new ActivityItem("昨天", "系统备份", "数据库自动备份完成", "info")
        );

        VBox timeline = DashboardComponents.createActivityTimeline(activities);
        ScrollPane timelineScroll = new ScrollPane(timeline);
        timelineScroll.setFitToWidth(true);
        timelineScroll.setMaxHeight(350);
        timelineScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        VBox activityPanel = DashboardComponents.createDataPanel("最近活动", "实时更新", timelineScroll);
        rightColumn.getChildren().add(activityPanel);

        // 系统状态
        HBox systemStatus = DashboardComponents.createSystemStatus(true, 3, "刚刚");
        VBox statusPanel = DashboardComponents.createDataPanel("系统状态", null, systemStatus);
        rightColumn.getChildren().add(statusPanel);

        mainContent.getChildren().addAll(leftColumn, rightColumn);
        dashboardContainer.getChildren().add(mainContent);

        // 4. 入场动画
        playEntranceAnimation();
    }

    /**
     * 创建欢迎栏
     */
    private HBox createWelcomeBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 0, 8, 0));

        VBox welcomeText = new VBox(4);
        Label greeting = new Label("欢迎回来，管理员");
        greeting.getStyleClass().addAll("text-2xl", "font-bold", "text-primary");
        
        Label subtitle = new Label("以下是系统概览数据");
        subtitle.getStyleClass().addAll("text-sm", "text-muted");
        
        welcomeText.getChildren().addAll(greeting, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox clock = DashboardComponents.createRealtimeClock();

        bar.getChildren().addAll(welcomeText, spacer, clock);
        return bar;
    }

    /**
     * 播放入场动画
     */
    private void playEntranceAnimation() {
        if (dashboardContainer == null) return;

        int delay = 0;
        for (Node child : dashboardContainer.getChildren()) {
            child.setOpacity(0);
            child.setTranslateY(20);

            javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(
                javafx.util.Duration.millis(400), child);
            tt.setFromY(20);
            tt.setToY(0);
            tt.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(
                javafx.util.Duration.millis(400), child);
            ft.setFromValue(0);
            ft.setToValue(1);

            javafx.animation.ParallelTransition pt = new javafx.animation.ParallelTransition(tt, ft);
            pt.setDelay(javafx.util.Duration.millis(delay));
            pt.play();

            delay += 100;
        }
    }

    /**
     * 刷新数据
     */
    @FXML
    private void refreshData() {
        AnimationUtil.pulse(dashboardContainer);
        // 重新加载数据...
    }
}

