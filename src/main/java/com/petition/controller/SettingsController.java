package com.petition.controller;

import com.petition.dao.BackupManager;
import com.petition.dao.ConfigManager;
import com.petition.dao.JsonDataManager;
import com.petition.service.ExportService;
import com.petition.service.ImportService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 设置页面控制器
 * 功能：系统配置、数据备份/恢复、导入导出
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class SettingsController implements Initializable {

    // ==================== FXML 注入组件 ====================

    // 数据管理
    @FXML private TextField dataPathField;
    @FXML private Label lastBackupLabel;
    @FXML private CheckBox autoBackupCheck;
    @FXML private Spinner<Integer> backupIntervalSpinner;

    // 显示设置
    @FXML private ComboBox<String> pageSizeCombo;
    @FXML private CheckBox maskIdCardCheck;

    // 导入导出
    @FXML private ComboBox<String> exportFormatCombo;

    // ==================== 业务属性 ====================

    private final ConfigManager configManager = new ConfigManager();
    private final BackupManager backupManager = new BackupManager(Paths.get("data/petitioners.json"));
    private final JsonDataManager dataManager = new JsonDataManager();
    private final ExportService exportService = new ExportService();
    private final ImportService importService = new ImportService();

    /**
     * 初始化方法
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeControls();
        loadSettings();
    }

    /**
     * 初始化控件
     */
    private void initializeControls() {
        // 每页显示条数
        pageSizeCombo.setItems(FXCollections.observableArrayList("10", "20", "50", "100"));

        // 导出格式
        exportFormatCombo.setItems(FXCollections.observableArrayList("Excel (.xlsx)", "CSV (.csv)"));

        // 备份间隔
        SpinnerValueFactory<Integer> intervalFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24, 6);
        backupIntervalSpinner.setValueFactory(intervalFactory);
    }

    /**
     * 加载设置
     */
    private void loadSettings() {
        try {
            // 数据路径
            Path dataPath = Paths.get("data").toAbsolutePath();
            dataPathField.setText(dataPath.toString());

            // 加载配置
            int pageSize = configManager.getInt("pageSize", 20);
            pageSizeCombo.setValue(String.valueOf(pageSize));

            boolean maskIdCard = configManager.getBoolean("maskIdCard", true);
            maskIdCardCheck.setSelected(maskIdCard);

            String exportFormat = configManager.getString("exportFormat", "Excel (.xlsx)");
            exportFormatCombo.setValue(exportFormat);

            boolean autoBackup = configManager.getBoolean("autoBackup", false);
            autoBackupCheck.setSelected(autoBackup);

            int backupInterval = configManager.getInt("backupInterval", 6);
            backupIntervalSpinner.getValueFactory().setValue(backupInterval);

            // 获取最后备份时间
            updateLastBackupLabel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新最后备份时间标签
     */
    private void updateLastBackupLabel() {
        try {
            List<String> backups = backupManager.listBackups();
            if (!backups.isEmpty()) {
                String lastBackup = backups.get(0);
                lastBackupLabel.setText("上次备份：" + lastBackup);
            } else {
                lastBackupLabel.setText("上次备份：从未");
            }
        } catch (Exception e) {
            lastBackupLabel.setText("上次备份：获取失败");
        }
    }

    // ==================== 事件处理方法 ====================

    /**
     * 打开数据目录
     */
    @FXML
    private void handleOpenDataDir() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            Desktop.getDesktop().open(dataDir);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "错误", "无法打开目录", e.getMessage());
        }
    }

    /**
     * 立即备份
     */
    @FXML
    private void handleBackup() {
        try {
            Path backupFile = backupManager.backup();
            updateLastBackupLabel();
            showAlert(Alert.AlertType.INFORMATION, "成功", "备份成功",
                "数据已备份到：\n" + backupFile.toString());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "错误", "备份失败", e.getMessage());
        }
    }

    /**
     * 恢复备份
     */
    @FXML
    private void handleRestore() {
        try {
            List<String> backups = backupManager.listBackups();
            if (backups.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "警告", "无可用备份", "没有找到任何备份文件");
                return;
            }

            // 选择备份文件
            ChoiceDialog<String> dialog = new ChoiceDialog<>(backups.get(0), backups);
            dialog.setTitle("恢复备份");
            dialog.setHeaderText("请选择要恢复的备份文件");
            dialog.setContentText("备份文件：");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                // 确认恢复
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("确认恢复");
                confirm.setHeaderText("确定要恢复此备份吗？");
                confirm.setContentText("当前数据将被覆盖！\n\n选择的备份：" + result.get());

                if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    backupManager.restore(result.get());
                    showAlert(Alert.AlertType.INFORMATION, "成功", "恢复成功", "数据已从备份恢复");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "错误", "恢复失败", e.getMessage());
        }
    }

    /**
     * 导出全部数据
     */
    @FXML
    private void handleExportAll() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出全部数据");

        String format = exportFormatCombo.getValue();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        if (format.contains("Excel")) {
            fileChooser.setInitialFileName("全部数据_" + timestamp + ".xlsx");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel文件", "*.xlsx"));
        } else {
            fileChooser.setInitialFileName("全部数据_" + timestamp + ".csv");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV文件", "*.csv"));
        }

        File file = fileChooser.showSaveDialog(dataPathField.getScene().getWindow());
        if (file != null) {
            try {
                int count;
                if (format.contains("Excel")) {
                    count = exportService.exportToExcel(file.getAbsolutePath());
                } else {
                    count = exportService.exportToCsv(file.getAbsolutePath());
                }
                showAlert(Alert.AlertType.INFORMATION, "成功", "导出成功",
                    "已导出 " + count + " 条记录到：\n" + file.getAbsolutePath());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "错误", "导出失败", e.getMessage());
            }
        }
    }

    /**
     * 导入数据
     */
    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导入数据");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel文件", "*.xlsx", "*.xls")
        );

        File file = fileChooser.showOpenDialog(dataPathField.getScene().getWindow());
        if (file != null) {
            try {
                ImportService.ImportResult result = importService.importFromExcel(file.getAbsolutePath(), true);
                StringBuilder message = new StringBuilder();
                message.append("导入完成！\n\n");
                message.append("成功：").append(result.getSuccessCount()).append(" 条\n");
                message.append("失败：").append(result.getErrorCount()).append(" 条\n");
                message.append("跳过（重复）：").append(result.getSkippedCount()).append(" 条");

                if (!result.getErrors().isEmpty()) {
                    message.append("\n\n错误详情：\n");
                    int errorCount = 0;
                    for (ImportService.ImportError error : result.getErrors()) {
                        if (errorCount >= 5) {
                            message.append("...(更多错误请查看日志)");
                            break;
                        }
                        message.append("第").append(error.getRowNumber()).append("行：")
                               .append(error.getMessage()).append("\n");
                        errorCount++;
                    }
                }

                showAlert(Alert.AlertType.INFORMATION, "导入结果", "数据导入完成", message.toString());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "错误", "导入失败", e.getMessage());
            }
        }
    }

    /**
     * 清空所有数据
     */
    @FXML
    private void handleClearAll() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("确认清空");
        confirm.setHeaderText("确定要清空所有数据吗？");
        confirm.setContentText("此操作不可恢复！建议先进行备份。");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // 二次确认
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("最终确认");
            dialog.setHeaderText("请输入 CONFIRM 确认清空");
            dialog.setContentText("确认文字：");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && "CONFIRM".equals(result.get())) {
                try {
                    dataManager.clear();
                    showAlert(Alert.AlertType.INFORMATION, "成功", "清空成功", "所有数据已清空");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "错误", "清空失败", e.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "取消", "操作已取消", "数据未被清空");
            }
        }
    }

    /**
     * 恢复默认设置
     */
    @FXML
    private void handleResetDefaults() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("确认恢复");
        confirm.setHeaderText("确定要恢复默认设置吗？");
        confirm.setContentText("所有设置将恢复为默认值。");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            pageSizeCombo.setValue("20");
            maskIdCardCheck.setSelected(true);
            exportFormatCombo.setValue("Excel (.xlsx)");
            autoBackupCheck.setSelected(false);
            backupIntervalSpinner.getValueFactory().setValue(6);

            showAlert(Alert.AlertType.INFORMATION, "成功", "已恢复默认设置", "请点击保存按钮保存设置");
        }
    }

    /**
     * 保存设置
     */
    @FXML
    private void handleSaveSettings() {
        try {
            // 保存配置
            configManager.set("pageSize", Integer.parseInt(pageSizeCombo.getValue()));
            configManager.set("maskIdCard", maskIdCardCheck.isSelected());
            configManager.set("exportFormat", exportFormatCombo.getValue());
            configManager.set("autoBackup", autoBackupCheck.isSelected());
            configManager.set("backupInterval", backupIntervalSpinner.getValue());

            showAlert(Alert.AlertType.INFORMATION, "成功", "设置已保存", "配置已成功保存");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "错误", "保存失败", e.getMessage());
        }
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
