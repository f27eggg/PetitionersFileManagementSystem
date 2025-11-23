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
    public void start(Stage primaryStage) throws Exception {
        // 加载主窗口FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        // 创建场景
        Scene scene = new Scene(root, 1600, 900);

        // 加载CSS样式
        String cssPath = getClass().getResource("/css/main.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        // 配置舞台
        primaryStage.setTitle("上访人员重点监控信息管理系统 - 济南市公安局历下分局刑侦大队");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // 最大化窗口

        // 显示窗口
        primaryStage.show();
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
