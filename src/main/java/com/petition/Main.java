package com.petition;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 上访人员重点监控信息管理系统
 * 主入口类
 *
 * @author 济南市公安局历下分局刑侦大队
 * @version 1.0.0
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("上访人员重点监控信息管理系统");
        primaryStage.setWidth(1280);
        primaryStage.setHeight(800);
        primaryStage.show();

        System.out.println("系统启动成功！");
        System.out.println("JavaFX版本: " + System.getProperty("javafx.version"));
        System.out.println("Java版本: " + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("济南市公安局历下分局刑侦大队");
        System.out.println("上访人员重点监控信息管理系统 v1.0.0");
        System.out.println("=".repeat(60));
        launch(args);
    }
}
