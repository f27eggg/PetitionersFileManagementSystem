package com.petition.controller;

import com.petition.model.Petitioner;
import com.petition.model.enums.RiskLevel;
import com.petition.service.ExportService;
import com.petition.service.ImportService;
import com.petition.service.PetitionerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
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

        // 设置Y轴显示整数（强制）
        if (visitCountChart.getYAxis() instanceof javafx.scene.chart.NumberAxis numberAxis) {
            numberAxis.setAutoRanging(true);
            numberAxis.setTickUnit(1.0);
            numberAxis.setMinorTickVisible(false);
            numberAxis.setMinorTickCount(0);
            numberAxis.setForceZeroInRange(true);
            // 设置标签格式化器，只显示整数刻度
            numberAxis.setTickLabelFormatter(new javafx.util.StringConverter<Number>() {
                @Override
                public String toString(Number object) {
                    // 只显示整数刻度，过滤掉小数刻度
                    double value = object.doubleValue();
                    if (Math.abs(value - Math.round(value)) < 0.01) {
                        return String.format("%.0f", value);
                    }
                    return "";
                }
                @Override
                public Number fromString(String string) {
                    return Double.parseDouble(string);
                }
            });
        }
    }

    /**
     * 加载籍贯分布柱状图（前10）
     */
    private void loadNativePlaceChart(List<Petitioner> petitioners) {
        // 提取省份（从籍贯中截取省名）
        Map<String, Long> nativePlaceDistribution = petitioners.stream()
                .filter(p -> p.getPersonalInfo().getNativePlace() != null
                          && !p.getPersonalInfo().getNativePlace().isEmpty())
                .collect(Collectors.groupingBy(
                        p -> extractProvinceName(p.getPersonalInfo().getNativePlace()),
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

        // 设置Y轴显示整数（强制）
        if (nativePlaceChart.getYAxis() instanceof javafx.scene.chart.NumberAxis numberAxis) {
            numberAxis.setAutoRanging(true);
            numberAxis.setTickUnit(1.0);
            numberAxis.setMinorTickVisible(false);
            numberAxis.setMinorTickCount(0);
            numberAxis.setForceZeroInRange(true);
            // 设置标签格式化器，只显示整数刻度
            numberAxis.setTickLabelFormatter(new javafx.util.StringConverter<Number>() {
                @Override
                public String toString(Number object) {
                    // 只显示整数刻度，过滤掉小数刻度
                    double value = object.doubleValue();
                    if (Math.abs(value - Math.round(value)) < 0.01) {
                        return String.format("%.0f", value);
                    }
                    return "";
                }
                @Override
                public Number fromString(String string) {
                    return Double.parseDouble(string);
                }
            });
        }
    }

    /**
     * 从籍贯字符串中提取省份名称
     * 例如："河北省保定市" -> "河北省"
     */
    private String extractProvinceName(String nativePlace) {
        if (nativePlace == null || nativePlace.isEmpty()) {
            return "未知";
        }

        // 查找"省"的位置
        int provinceIndex = nativePlace.indexOf("省");
        if (provinceIndex > 0) {
            return nativePlace.substring(0, provinceIndex + 1);
        }

        // 处理直辖市和特别行政区（北京市、上海市、天津市、重庆市、香港、澳门）
        if (nativePlace.startsWith("北京")) return "北京市";
        if (nativePlace.startsWith("上海")) return "上海市";
        if (nativePlace.startsWith("天津")) return "天津市";
        if (nativePlace.startsWith("重庆")) return "重庆市";
        if (nativePlace.startsWith("香港")) return "香港";
        if (nativePlace.startsWith("澳门")) return "澳门";

        // 处理自治区
        int regionIndex = nativePlace.indexOf("自治区");
        if (regionIndex > 0) {
            return nativePlace.substring(0, regionIndex + 3);
        }

        // 如果都不符合，返回前两个字或整个字符串
        return nativePlace.length() > 2 ? nativePlace.substring(0, 2) : nativePlace;
    }

    /**
     * 新增人员
     */
    @FXML
    private void addPetitioner() {
        try {
            // 加载表单页面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/form.fxml"));
            Parent formRoot = loader.load();

            // 创建美化的弹窗
            Stage parentStage = (Stage) totalCountLabel.getScene().getWindow();
            Stage formStage = com.petition.util.StageUtil.createStyledDialog(
                "🆕 新增上访人员", formRoot, parentStage, 1200, 800
            );

            // 获取FormController并设置回调
            FormController formController = loader.getController();
            formController.setOnSaveCallback(() -> {
                // 添加关闭动画
                com.petition.util.StageUtil.addCloseAnimation(formRoot, () -> {
                    // 保存成功后刷新数据
                    loadStatistics();
                    loadCharts();
                    formStage.close();
                });
            });

            formStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showError("打开表单失败：" + e.getMessage());
        }
    }

    /**
     * 导入数据
     */
    @FXML
    private void importData() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择要导入的Excel文件");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel文件", "*.xlsx", "*.xls")
            );

            // 显示文件选择对话框
            Stage stage = (Stage) totalCountLabel.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                // 执行导入
                ImportService importService = new ImportService();
                var result = importService.importFromExcel(file.getAbsolutePath(), true);

                // 显示导入结果
                showInfo(String.format("导入完成！\n成功：%d 条\n失败：%d 条\n跳过：%d 条",
                        result.getSuccessCount(),
                        result.getErrorCount(),
                        result.getSkippedCount()));

                // 刷新数据
                loadStatistics();
                loadCharts();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出数据
     */
    @FXML
    private void exportData() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择导出位置");
            fileChooser.setInitialFileName("上访人员数据.xlsx");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel文件", "*.xlsx")
            );

            // 显示文件保存对话框
            Stage stage = (Stage) totalCountLabel.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                // 执行导出
                ExportService exportService = new ExportService();
                List<Petitioner> allPetitioners = petitionerService.getAllPetitioners();
                exportService.exportToExcel(file.getAbsolutePath(), allPetitioners);

                showInfo("导出成功！\n文件：" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("导出失败：" + e.getMessage());
        }
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

    /**
     * 显示信息对话框
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 显示错误对话框
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
