package com.petition.controller;

import com.petition.service.PetitionerService;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 主窗口控制器
 * 功能：
 * 1. 管理主窗口布局
 * 2. 处理侧边导航栏的展开/收起
 * 3. 管理页面路由和切换
 * 4. 显示系统信息（版本、记录数等）
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class MainController {

    // ==================== FXML注入组件 ====================

    @FXML
    private Button menuButton;

    @FXML
    private ImageView logoImage;

    @FXML
    private Label currentUserLabel;

    @FXML
    private VBox sidebar;

    @FXML
    private VBox navMenu;

    @FXML
    private Button navDashboard;

    @FXML
    private Button navPetitioners;

    @FXML
    private Button navSettings;

    @FXML
    private Label versionLabel;

    @FXML
    private Label recordCountLabel;

    @FXML
    private StackPane contentArea;

    // ==================== 私有字段 ====================

    /**
     * 侧边栏是否展开
     */
    private boolean sidebarExpanded = true;

    /**
     * 当前激活的导航按钮
     */
    private Button currentActiveNav;

    /**
     * 页面缓存（提高性能，避免重复加载）
     */
    private final Map<String, Parent> pageCache = new HashMap<>();

    /**
     * 业务服务
     */
    private PetitionerService petitionerService;

    // ==================== 初始化方法 ====================

    /**
     * FXML加载完成后自动调用
     */
    @FXML
    public void initialize() {
        try {
            // 初始化业务服务
            petitionerService = new PetitionerService();

            // 加载Logo图片
            loadLogo();

            // 设置当前激活的导航按钮
            currentActiveNav = navDashboard;

            // 更新记录数显示
            updateRecordCount();

            // 默认加载仪表盘页面
            Parent dashboardPage = loadPage("dashboard");
            contentArea.getChildren().add(dashboardPage);

        } catch (Exception e) {
            e.printStackTrace();
            showError("初始化失败：" + e.getMessage());
        }
    }

    /**
     * 加载Logo图片
     */
    private void loadLogo() {
        try {
            // 尝试从项目根目录加载警徽图片
            File logoFile = new File("警徽.png");
            if (logoFile.exists()) {
                Image logo = new Image(logoFile.toURI().toString());
                logoImage.setImage(logo);
            } else {
                System.out.println("警徽.png 文件不存在，跳过加载");
            }
        } catch (Exception e) {
            System.out.println("加载Logo失败：" + e.getMessage());
        }
    }

    /**
     * 更新记录数显示
     */
    private void updateRecordCount() {
        try {
            int count = petitionerService.getAllPetitioners().size();
            recordCountLabel.setText("记录数：" + count);
        } catch (Exception e) {
            recordCountLabel.setText("记录数：0");
        }
    }

    // ==================== 侧边栏控制 ====================

    /**
     * 切换侧边栏展开/收起状态
     */
    @FXML
    private void toggleSidebar() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sidebar);

        if (sidebarExpanded) {
            // 收起侧边栏
            transition.setToX(-200);
            menuButton.setText("☰");
        } else {
            // 展开侧边栏
            transition.setToX(0);
            menuButton.setText("✕");
        }

        transition.play();
        sidebarExpanded = !sidebarExpanded;
    }

    // ==================== 导航按钮事件处理 ====================

    /**
     * 显示仪表盘页面
     */
    @FXML
    private void showDashboard() {
        switchPage("dashboard", navDashboard);
    }

    /**
     * 显示人员管理页面
     */
    @FXML
    private void showPetitioners() {
        switchPage("petitioners", navPetitioners);
    }

    /**
     * 显示系统设置页面
     */
    @FXML
    private void showSettings() {
        switchPage("settings", navSettings);
    }

    /**
     * 退出系统
     */
    @FXML
    private void logout() {
        // TODO: 添加退出确认对话框
        System.exit(0);
    }

    // ==================== 页面路由管理 ====================

    /**
     * 切换页面
     *
     * @param pageName 页面名称（对应fxml文件名）
     * @param navButton 对应的导航按钮
     */
    private void switchPage(String pageName, Button navButton) {
        try {
            // 加载页面
            Parent page = loadPage(pageName);

            // 切换内容区域
            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);

            // 更新导航按钮状态
            updateNavButtonState(navButton);

            // 更新记录数
            updateRecordCount();

        } catch (Exception e) {
            e.printStackTrace();
            showError("页面加载失败：" + e.getMessage());
        }
    }

    /**
     * 加载FXML页面
     *
     * @param pageName 页面名称
     * @return 加载的页面根节点
     */
    private Parent loadPage(String pageName) throws IOException {
        // 检查缓存
        if (pageCache.containsKey(pageName)) {
            return pageCache.get(pageName);
        }

        // 加载FXML文件
        String fxmlPath = "/fxml/" + pageName + ".fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        Parent page = loader.load();

        // 缓存页面
        pageCache.put(pageName, page);

        return page;
    }

    /**
     * 更新导航按钮激活状态
     *
     * @param activeButton 要激活的按钮
     */
    private void updateNavButtonState(Button activeButton) {
        // 移除之前激活按钮的样式
        if (currentActiveNav != null) {
            currentActiveNav.getStyleClass().remove("nav-button-active");
        }

        // 添加新激活按钮的样式
        activeButton.getStyleClass().add("nav-button-active");

        // 更新当前激活按钮引用
        currentActiveNav = activeButton;
    }

    // ==================== 工具方法 ====================

    /**
     * 显示错误信息
     *
     * @param message 错误消息
     */
    private void showError(String message) {
        // TODO: 使用对话框显示错误
        System.err.println("错误：" + message);
    }

    /**
     * 清空页面缓存
     */
    public void clearPageCache() {
        pageCache.clear();
    }

    /**
     * 刷新当前页面
     */
    public void refreshCurrentPage() {
        if (currentActiveNav != null) {
            String currentPage = getCurrentPageName();
            pageCache.remove(currentPage);
            currentActiveNav.fire();
        }
    }

    /**
     * 获取当前页面名称
     *
     * @return 页面名称
     */
    private String getCurrentPageName() {
        if (currentActiveNav == navDashboard) return "dashboard";
        if (currentActiveNav == navPetitioners) return "petitioners";
        if (currentActiveNav == navSettings) return "settings";
        return "dashboard";
    }
}
