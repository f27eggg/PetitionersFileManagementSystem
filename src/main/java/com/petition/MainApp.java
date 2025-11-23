package com.petition;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * 应用程序主入口类
 * 功能：启动JavaFX应用程序，加载主窗口
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class MainApp extends Application {

    /**
     * 应用程序启动方法
     *
     * @param primaryStage 主舞台
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("=== 应用程序启动 ===");
            System.out.println("工作目录: " + System.getProperty("user.dir"));

            // 加载主窗口FXML
            System.out.println("正在加载main.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            System.out.println("main.fxml加载成功！");

            // 创建场景
            System.out.println("正在创建Scene...");
            Scene scene = new Scene(root, 1600, 900);

            // 加载CSS样式
            System.out.println("正在加载CSS样式...");
            String cssPath = getClass().getResource("/css/main.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            System.out.println("CSS加载成功：" + cssPath);

            // 配置舞台
            System.out.println("正在配置Stage...");
            primaryStage.setTitle("上访人员重点监控信息管理系统 - 济南市公安局历下分局刑侦大队");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true); // 最大化窗口

            // 显示窗口
            System.out.println("正在显示窗口...");
            primaryStage.show();
            System.out.println("=== 窗口已显示成功！===");

        } catch (Exception e) {
            System.err.println("!!! 启动失败 !!!");
            e.printStackTrace();

            // 显示错误详情
            System.err.println("\n错误详情：");
            System.err.println("错误类型：" + e.getClass().getName());
            System.err.println("错误消息：" + e.getMessage());

            // 检查资源文件
            System.err.println("\n资源检查：");
            System.err.println("main.fxml存在: " + (getClass().getResource("/fxml/main.fxml") != null));
            System.err.println("main.css存在: " + (getClass().getResource("/css/main.css") != null));
        }
    }

    /**
     * 应用程序主方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        launch(args);
    }
}
