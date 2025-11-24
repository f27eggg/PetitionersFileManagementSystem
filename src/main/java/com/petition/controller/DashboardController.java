package com.petition.controller;

import com.petition.model.Petitioner;
import com.petition.model.enums.RiskLevel;
import com.petition.service.PetitionerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 仪表盘控制器
 * 功能：
 * 1. 显示系统统计数据
 * 2. 显示可视化图表（危险等级、上访次数、籍贯分布）
 * 3. 提供快速操作入口
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

    @FXML
    private PieChart riskLevelChart;

    @FXML
    private BarChart<String, Number> visitCountChart;

    @FXML
    private BarChart<String, Number> nativePlaceChart;

    private PetitionerService petitionerService;

    /**
     * FXML加载完成后自动调用
     */
    @FXML
    public void initialize() {
        try {
            petitionerService = new PetitionerService();
            loadStatistics();
            loadCharts();
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
                    .filter(p -> p.getRiskAssessment().getRiskLevel() == RiskLevel.HIGH
                              || p.getRiskAssessment().getRiskLevel() == RiskLevel.CRITICAL)
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
     * 加载图表数据
     */
    private void loadCharts() {
        try {
            List<Petitioner> allPetitioners = petitionerService.getAllPetitioners();

            // 加载危险等级分布饼图
            loadRiskLevelChart(allPetitioners);

            // 加载上访次数分布柱状图
            loadVisitCountChart(allPetitioners);

            // 加载籍贯分布柱状图
            loadNativePlaceChart(allPetitioners);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载危险等级分布饼图
     */
    private void loadRiskLevelChart(List<Petitioner> petitioners) {
        Map<RiskLevel, Long> riskDistribution = petitioners.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getRiskAssessment().getRiskLevel(),
                        Collectors.counting()
                ));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<RiskLevel, Long> entry : riskDistribution.entrySet()) {
            if (entry.getValue() > 0) {
                pieData.add(new PieChart.Data(
                        entry.getKey().getDisplayName(),
                        entry.getValue()
                ));
            }
        }

        riskLevelChart.setData(pieData);
        riskLevelChart.setTitle("");
    }

    /**
     * 加载上访次数分布柱状图
     */
    private void loadVisitCountChart(List<Petitioner> petitioners) {
        // 统计各个范围的人数
        long count1_3 = petitioners.stream()
                .filter(p -> {
                    int count = p.getPersonalInfo().getVisitCount();
                    return count >= 1 && count <= 3;
                })
                .count();

        long count4_6 = petitioners.stream()
                .filter(p -> {
                    int count = p.getPersonalInfo().getVisitCount();
                    return count >= 4 && count <= 6;
                })
                .count();

        long count7_10 = petitioners.stream()
                .filter(p -> {
                    int count = p.getPersonalInfo().getVisitCount();
                    return count >= 7 && count <= 10;
                })
                .count();

        long count10Plus = petitioners.stream()
                .filter(p -> p.getPersonalInfo().getVisitCount() > 10)
                .count();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("人数");
        series.getData().add(new XYChart.Data<>("1-3次", count1_3));
        series.getData().add(new XYChart.Data<>("4-6次", count4_6));
        series.getData().add(new XYChart.Data<>("7-10次", count7_10));
        series.getData().add(new XYChart.Data<>("10次以上", count10Plus));

        visitCountChart.getData().clear();
        visitCountChart.getData().add(series);
        visitCountChart.setLegendVisible(false);
    }

    /**
     * 加载籍贯分布柱状图（前10）
     */
    private void loadNativePlaceChart(List<Petitioner> petitioners) {
        // 按籍贯统计人数
        Map<String, Long> nativePlaceDistribution = petitioners.stream()
                .filter(p -> p.getPersonalInfo().getNativePlace() != null
                          && !p.getPersonalInfo().getNativePlace().isEmpty())
                .collect(Collectors.groupingBy(
                        p -> p.getPersonalInfo().getNativePlace(),
                        Collectors.counting()
                ));

        // 按数量排序，取前10
        List<Map.Entry<String, Long>> sortedEntries = nativePlaceDistribution.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .collect(Collectors.toList());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("人数");

        for (Map.Entry<String, Long> entry : sortedEntries) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        nativePlaceChart.getData().clear();
        nativePlaceChart.getData().add(series);
        nativePlaceChart.setLegendVisible(false);
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
        loadCharts();
        System.out.println("数据已刷新");
    }
}
