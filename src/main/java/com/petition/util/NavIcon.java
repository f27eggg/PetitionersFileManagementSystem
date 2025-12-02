package com.petition.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 导航图标辅助类
 * 使用Unicode字符和SVG形状替代可能导致乱码的图标字体
 * 
 * 解决侧边栏"系统设置"等菜单项前出现乱码的问题
 */
public class NavIcon {

    // ==================== 导航图标定义 ====================
    
    // 使用通用Unicode符号，这些符号在大多数字体中都有支持
    public static final String DASHBOARD = "▣";      // 仪表盘
    public static final String PERSON = "◉";         // 人员
    public static final String SEARCH = "◎";         // 搜索
    public static final String CHART = "◐";          // 图表/统计
    public static final String DOCUMENT = "▤";       // 文档
    public static final String SETTINGS = "⚙";       // 设置
    public static final String USER = "◈";           // 用户
    public static final String LOGOUT = "◇";         // 退出
    public static final String ADD = "＋";           // 添加
    public static final String EDIT = "✎";           // 编辑
    public static final String DELETE = "✕";         // 删除
    public static final String VIEW = "◉";           // 查看
    public static final String EXPORT = "▲";         // 导出
    public static final String IMPORT = "▼";         // 导入
    public static final String ALERT = "◆";          // 警告
    public static final String INFO = "●";           // 信息
    public static final String SUCCESS = "✓";        // 成功
    public static final String ERROR = "✗";          // 错误
    public static final String PHOTO = "▢";          // 照片
    public static final String CALENDAR = "▦";       // 日历
    public static final String LOCATION = "◎";       // 位置
    public static final String PHONE = "☎";          // 电话
    public static final String MAIL = "✉";           // 邮件
    public static final String LOCK = "▣";           // 锁定
    public static final String UNLOCK = "▢";         // 解锁
    public static final String REFRESH = "↻";        // 刷新
    public static final String FILTER = "▽";         // 筛选
    public static final String SORT = "↕";           // 排序
    public static final String LIST = "≡";           // 列表
    public static final String GRID = "⊞";           // 网格
    public static final String HOME = "⌂";           // 首页
    public static final String BACK = "◀";           // 返回
    public static final String FORWARD = "▶";        // 前进
    public static final String UP = "▲";             // 上
    public static final String DOWN = "▼";           // 下
    public static final String LEFT = "◀";           // 左
    public static final String RIGHT = "▶";          // 右
    public static final String CLOSE = "✕";          // 关闭
    public static final String CHECK = "✓";          // 勾选
    public static final String UNCHECK = "○";        // 未勾选
    public static final String STAR = "★";           // 收藏
    public static final String STAR_EMPTY = "☆";     // 未收藏
    public static final String HEART = "♥";          // 喜欢
    public static final String MENU = "☰";           // 菜单
    public static final String MORE = "⋯";           // 更多
    public static final String EXPAND = "＋";        // 展开
    public static final String COLLAPSE = "－";      // 收起

    // ==================== 导航菜单项图标映射 ====================

    /**
     * 获取导航菜单图标
     * @param menuName 菜单名称
     * @return 对应的图标字符
     */
    public static String getNavIcon(String menuName) {
        return switch (menuName) {
            case "仪表盘", "首页", "概览" -> DASHBOARD;
            case "人员管理", "人员列表", "档案管理" -> PERSON;
            case "高级搜索", "搜索", "查询" -> SEARCH;
            case "数据统计", "统计分析", "报表" -> CHART;
            case "文档管理", "档案", "文件" -> DOCUMENT;
            case "系统设置", "设置", "配置" -> SETTINGS;
            case "用户管理", "账户", "用户" -> USER;
            case "退出登录", "注销", "退出" -> LOGOUT;
            case "新增", "添加", "创建" -> ADD;
            case "编辑", "修改" -> EDIT;
            case "删除", "移除" -> DELETE;
            case "查看", "详情" -> VIEW;
            case "导出", "下载" -> EXPORT;
            case "导入", "上传" -> IMPORT;
            case "预警", "警告", "告警" -> ALERT;
            case "照片", "图片", "相册" -> PHOTO;
            default -> "●"; // 默认圆点
        };
    }

    // ==================== 创建图标组件 ====================

    /**
     * 创建导航图标Label
     */
    public static Label createNavIconLabel(String menuName) {
        Label icon = new Label(getNavIcon(menuName));
        icon.getStyleClass().add("nav-icon");
        icon.setStyle("-fx-font-size: 16px; -fx-min-width: 24px; -fx-alignment: CENTER;");
        return icon;
    }

    /**
     * 创建图标容器（带背景）
     */
    public static StackPane createIconContainer(String iconChar, String bgColor, double size) {
        StackPane container = new StackPane();
        container.setMinSize(size, size);
        container.setMaxSize(size, size);
        container.setStyle(
            "-fx-background-color: " + bgColor + "20; " +
            "-fx-background-radius: " + (size / 2) + "; " +
            "-fx-border-color: " + bgColor + "40; " +
            "-fx-border-radius: " + (size / 2) + "; " +
            "-fx-border-width: 1;"
        );

        Label icon = new Label(iconChar);
        icon.setStyle("-fx-font-size: " + (size * 0.5) + "px; -fx-text-fill: " + bgColor + ";");
        
        container.getChildren().add(icon);
        return container;
    }

    /**
     * 创建彩色圆形图标
     */
    public static StackPane createColoredIcon(String iconChar, Color color, double size) {
        StackPane container = new StackPane();
        container.setMinSize(size, size);
        container.setMaxSize(size, size);

        Circle bg = new Circle(size / 2);
        bg.setFill(color.deriveColor(0, 1, 1, 0.15));
        bg.setStroke(color.deriveColor(0, 1, 1, 0.3));
        bg.setStrokeWidth(1);

        Label icon = new Label(iconChar);
        icon.setTextFill(color);
        icon.setFont(Font.font("System", FontWeight.BOLD, size * 0.5));

        container.getChildren().addAll(bg, icon);
        return container;
    }

    // ==================== SVG图标形状 ====================

    /**
     * 创建仪表盘形状图标
     */
    public static SVGPath createDashboardShape() {
        SVGPath path = new SVGPath();
        path.setContent("M3 3h6v6H3V3zm8 0h6v6h-6V3zm-8 8h6v6H3v-6zm8 2a4 4 0 1 0 8 0 4 4 0 0 0-8 0z");
        path.setFill(Color.web("#00d4ff"));
        return path;
    }

    /**
     * 创建用户形状图标
     */
    public static SVGPath createUserShape() {
        SVGPath path = new SVGPath();
        path.setContent("M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z");
        path.setFill(Color.web("#00d4ff"));
        return path;
    }

    /**
     * 创建设置形状图标
     */
    public static SVGPath createSettingsShape() {
        SVGPath path = new SVGPath();
        path.setContent("M19.14 12.94c.04-.31.06-.63.06-.94 0-.31-.02-.63-.06-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.04.31-.06.63-.06.94s.02.63.06.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z");
        path.setFill(Color.web("#00d4ff"));
        return path;
    }

    // ==================== 状态图标 ====================

    /**
     * 获取风险等级图标
     */
    public static String getRiskIcon(String level) {
        return switch (level) {
            case "极高风险" -> "◆";  // 红色菱形
            case "高风险" -> "▲";    // 三角形
            case "中风险" -> "●";    // 圆形
            case "低风险" -> "○";    // 空心圆
            default -> "○";
        };
    }

    /**
     * 获取状态图标
     */
    public static String getStatusIcon(String status) {
        return switch (status) {
            case "正常", "完成", "通过" -> "✓";
            case "异常", "失败", "拒绝" -> "✗";
            case "警告", "待处理" -> "◆";
            case "进行中", "处理中" -> "◐";
            default -> "●";
        };
    }

    // ==================== 数字徽章 ====================

    /**
     * 创建数字徽章
     */
    public static StackPane createBadge(int count, String color) {
        StackPane badge = new StackPane();
        badge.setMinSize(20, 20);
        badge.setMaxSize(count > 99 ? 28 : 20, 20);

        String text = count > 99 ? "99+" : String.valueOf(count);
        
        badge.setStyle(
            "-fx-background-color: " + color + "; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 2 6 2 6;"
        );

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: white;");

        badge.getChildren().add(label);
        badge.setVisible(count > 0);

        return badge;
    }

    // ==================== 带徽章的导航项 ====================

    /**
     * 创建带徽章的导航图标
     */
    public static StackPane createNavIconWithBadge(String menuName, int badgeCount) {
        StackPane container = new StackPane();
        container.setMinSize(32, 24);
        container.setMaxSize(32, 24);
        container.setAlignment(Pos.CENTER);

        Label icon = createNavIconLabel(menuName);
        
        if (badgeCount > 0) {
            StackPane badge = createBadge(badgeCount, "#ff0066");
            StackPane.setAlignment(badge, Pos.TOP_RIGHT);
            StackPane.setMargin(badge, new javafx.geometry.Insets(-4, -8, 0, 0));
            container.getChildren().addAll(icon, badge);
        } else {
            container.getChildren().add(icon);
        }

        return container;
    }
}

