package com.petition.controller;

import com.petition.model.enums.*;
import com.petition.service.StatisticsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.*;

/**
 * 统计图表页面控制器
 * 功能：展示各类统计数据的图表
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class StatisticsController implements Initializable {

    // ==================== FXML 注入组件 ====================

    // 统计概览标签
    @FXML private Label totalCountLabel;
    @FXML private Label highRiskCountLabel;
    @FXML private Label mediumRiskCountLabel;
    @FXML private Label lowRiskCountLabel;

    // 图表
    @FXML private PieChart riskLevelChart;
    @FXML private PieChart genderChart;
    @FXML private BarChart<String, Number> visitCountChart;
    @FXML private BarChart<String, Number> entryMethodChart;
    @FXML private BarChart<String, Number> educationChart;
    @FXML private BarChart<String, Number> nativePlaceChart;

    // ==================== 业务属性 ====================

    private final StatisticsService statisticsService = new StatisticsService();

    /**
     * 初始化方法
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadStatistics();
    }

    /**
     * 加载统计数据
     */
    private void loadStatistics() {
        try {
            loadOverviewStats();
            loadRiskLevelChart();
            loadGenderChart();
            loadVisitCountChart();
            loadEntryMethodChart();
            loadEducationChart();
            loadNativePlaceChart();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "错误", "加载统计数据失败", e.getMessage());
        }
    }

    /**
     * 加载概览统计
     */
    private void loadOverviewStats() throws Exception {
        // 获取风险等级分布
        Map<RiskLevel, Integer> riskDistribution = statisticsService.getRiskLevelDistribution();

        int total = 0;
        int highRisk = 0;
        int mediumRisk = 0;
        int lowRisk = 0;

        for (Map.Entry<RiskLevel, Integer> entry : riskDistribution.entrySet()) {
            int count = entry.getValue();
            total += count;

            if (entry.getKey() == RiskLevel.CRITICAL || entry.getKey() == RiskLevel.HIGH) {
                highRisk += count;
            } else if (entry.getKey() == RiskLevel.MEDIUM) {
                mediumRisk = count;
            } else if (entry.getKey() == RiskLevel.LOW) {
                lowRisk = count;
            }
        }

        totalCountLabel.setText(String.valueOf(total));
        highRiskCountLabel.setText(String.valueOf(highRisk));
        mediumRiskCountLabel.setText(String.valueOf(mediumRisk));
        lowRiskCountLabel.setText(String.valueOf(lowRisk));
    }

    /**
     * 加载危险等级分布饼图
     */
    private void loadRiskLevelChart() throws Exception {
        Map<RiskLevel, Integer> distribution = statisticsService.getRiskLevelDistribution();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<RiskLevel, Integer> entry : distribution.entrySet()) {
            if (entry.getValue() > 0) {
                pieData.add(new PieChart.Data(entry.getKey().getDisplayName(), entry.getValue()));
            }
        }

        riskLevelChart.setData(pieData);
        riskLevelChart.setTitle("");
    }

    /**
     * 加载性别分布饼图
     */
    private void loadGenderChart() throws Exception {
        Map<Gender, Integer> distribution = statisticsService.getGenderDistribution();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<Gender, Integer> entry : distribution.entrySet()) {
            if (entry.getValue() > 0) {
                pieData.add(new PieChart.Data(entry.getKey().getDisplayName(), entry.getValue()));
            }
        }

        genderChart.setData(pieData);
        genderChart.setTitle("");
    }

    /**
     * 加载上访次数分布柱状图
     */
    private void loadVisitCountChart() throws Exception {
        Map<String, Integer> distribution = statisticsService.getVisitCountDistribution();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("人数");

        // 按照特定顺序排列
        String[] order = {"1-3次", "4-6次", "7-10次", "10次以上"};
        for (String key : order) {
            if (distribution.containsKey(key)) {
                series.getData().add(new XYChart.Data<>(key, distribution.get(key)));
            }
        }

        visitCountChart.getData().clear();
        visitCountChart.getData().add(series);
        visitCountChart.setLegendVisible(false);
    }

    /**
     * 加载进京方式分布柱状图
     */
    private void loadEntryMethodChart() throws Exception {
        Map<EntryMethod, Integer> distribution = statisticsService.getEntryMethodDistribution();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("人数");

        for (Map.Entry<EntryMethod, Integer> entry : distribution.entrySet()) {
            if (entry.getValue() > 0) {
                series.getData().add(new XYChart.Data<>(entry.getKey().getDisplayName(), entry.getValue()));
            }
        }

        entryMethodChart.getData().clear();
        entryMethodChart.getData().add(series);
        entryMethodChart.setLegendVisible(false);
    }

    /**
     * 加载文化程度分布柱状图
     */
    private void loadEducationChart() throws Exception {
        Map<Education, Integer> distribution = statisticsService.getEducationDistribution();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("人数");

        for (Map.Entry<Education, Integer> entry : distribution.entrySet()) {
            if (entry.getValue() > 0) {
                series.getData().add(new XYChart.Data<>(entry.getKey().getDisplayName(), entry.getValue()));
            }
        }

        educationChart.getData().clear();
        educationChart.getData().add(series);
        educationChart.setLegendVisible(false);
    }

    /**
     * 加载籍贯分布柱状图
     */
    private void loadNativePlaceChart() throws Exception {
        Map<String, Integer> distribution = statisticsService.getNativePlaceDistribution();

        // 按数量排序，取前10
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(distribution.entrySet());
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("人数");

        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            if (count >= 10) break;
            if (entry.getValue() > 0 && entry.getKey() != null && !entry.getKey().isEmpty()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                count++;
            }
        }

        nativePlaceChart.getData().clear();
        nativePlaceChart.getData().add(series);
        nativePlaceChart.setLegendVisible(false);
    }

    // ==================== 事件处理方法 ====================

    /**
     * 处理刷新按钮点击
     */
    @FXML
    private void handleRefresh() {
        loadStatistics();
    }

    /**
     * 显示提示对话框
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
