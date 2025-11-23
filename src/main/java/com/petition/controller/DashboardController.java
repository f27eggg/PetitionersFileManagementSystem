package com.petition.controller;

import com.petition.model.Petitioner;
import com.petition.model.enums.RiskLevel;
import com.petition.service.PetitionerService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

/**
 * 仪表盘控制器
 * 功能：
 * 1. 显示系统统计数据
 * 2. 提供快速操作入口
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class DashboardController {

    @FXML
    private Label totalCountLabel;

    @FXML
    private Label highRiskCountLabel;

    @FXML
    private Label mediumRiskCountLabel;

    @FXML
    private Label lowRiskCountLabel;

    private PetitionerService petitionerService;

    /**
     * FXML加载完成后自动调用
     */
    @FXML
    public void initialize() {
        try {
            petitionerService = new PetitionerService();
            loadStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载统计数据
     */
    private void loadStatistics() {
        try {
            List<Petitioner> allPetitioners = petitionerService.getAllPetitioners();

            // 总人数
            totalCountLabel.setText(String.valueOf(allPetitioners.size()));

            // 按危险等级统计
            long highRiskCount = allPetitioners.stream()
                    .filter(p -> p.getRiskAssessment().getRiskLevel() == RiskLevel.HIGH)
                    .count();

            long mediumRiskCount = allPetitioners.stream()
                    .filter(p -> p.getRiskAssessment().getRiskLevel() == RiskLevel.MEDIUM)
                    .count();

            long lowRiskCount = allPetitioners.stream()
                    .filter(p -> p.getRiskAssessment().getRiskLevel() == RiskLevel.LOW)
                    .count();

            highRiskCountLabel.setText(String.valueOf(highRiskCount));
            mediumRiskCountLabel.setText(String.valueOf(mediumRiskCount));
            lowRiskCountLabel.setText(String.valueOf(lowRiskCount));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增人员
     */
    @FXML
    private void addPetitioner() {
        // TODO: 打开新增人员表单
        System.out.println("新增人员");
    }

    /**
     * 导入数据
     */
    @FXML
    private void importData() {
        // TODO: 打开导入数据对话框
        System.out.println("导入数据");
    }

    /**
     * 导出数据
     */
    @FXML
    private void exportData() {
        // TODO: 打开导出数据对话框
        System.out.println("导出数据");
    }

    /**
     * 刷新数据
     */
    @FXML
    private void refreshData() {
        loadStatistics();
        System.out.println("数据已刷新");
    }
}
