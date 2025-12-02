package com.petition.controller;

import com.petition.model.Petitioner;
import com.petition.model.enums.*;
import com.petition.service.ExportService;
import com.petition.service.QueryService;
import com.petition.util.IdCardUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 高级查询页面控制器
 * 功能：提供多条件组合查询和结果导出
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class QueryController implements Initializable {

    // ==================== FXML 注入组件 ====================

    // 查询条件
    @FXML private TextField nameField;
    @FXML private TextField idCardField;
    @FXML private TextField nativePlaceField;
    @FXML private ComboBox<String> riskLevelCombo;
    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> educationCombo;
    @FXML private ComboBox<String> maritalStatusCombo;
    @FXML private ComboBox<String> entryMethodCombo;
    @FXML private Spinner<Integer> minVisitSpinner;
    @FXML private Spinner<Integer> maxVisitSpinner;

    // 结果显示
    @FXML private Label resultCountLabel;
    @FXML private TableView<Petitioner> resultTable;

    // 表格列
    @FXML private TableColumn<Petitioner, Integer> indexColumn;
    @FXML private TableColumn<Petitioner, String> nameColumn;
    @FXML private TableColumn<Petitioner, String> idCardColumn;
    @FXML private TableColumn<Petitioner, String> genderColumn;
    @FXML private TableColumn<Petitioner, String> nativePlaceColumn;
    @FXML private TableColumn<Petitioner, Integer> visitCountColumn;
    @FXML private TableColumn<Petitioner, String> riskLevelColumn;
    @FXML private TableColumn<Petitioner, String> phoneColumn;
    @FXML private TableColumn<Petitioner, Void> actionColumn;

    // ==================== 业务属性 ====================

    private final QueryService queryService = new QueryService();
    private final ExportService exportService = new ExportService();
    private final ObservableList<Petitioner> resultData = FXCollections.observableArrayList();

    /**
     * 初始化方法
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComboBoxes();
        initializeSpinners();
        initializeTableColumns();
    }

    /**
     * 初始化下拉框
     */
    private void initializeComboBoxes() {
        // 危险等级
        List<String> riskLevels = new ArrayList<>();
        riskLevels.add("全部");
        for (RiskLevel level : RiskLevel.values()) {
            riskLevels.add(level.getDisplayName());
        }
        riskLevelCombo.setItems(FXCollections.observableArrayList(riskLevels));
        riskLevelCombo.getSelectionModel().selectFirst();

        // 性别
        List<String> genders = new ArrayList<>();
        genders.add("全部");
        for (Gender gender : Gender.values()) {
            genders.add(gender.getDisplayName());
        }
        genderCombo.setItems(FXCollections.observableArrayList(genders));
        genderCombo.getSelectionModel().selectFirst();

        // 文化程度
        List<String> educations = new ArrayList<>();
        educations.add("全部");
        for (Education edu : Education.values()) {
            educations.add(edu.getDisplayName());
        }
        educationCombo.setItems(FXCollections.observableArrayList(educations));
        educationCombo.getSelectionModel().selectFirst();

        // 婚姻状况
        List<String> maritalStatuses = new ArrayList<>();
        maritalStatuses.add("全部");
        for (MaritalStatus status : MaritalStatus.values()) {
            maritalStatuses.add(status.getDisplayName());
        }
        maritalStatusCombo.setItems(FXCollections.observableArrayList(maritalStatuses));
        maritalStatusCombo.getSelectionModel().selectFirst();

        // 进京方式
        List<String> entryMethods = new ArrayList<>();
        entryMethods.add("全部");
        for (EntryMethod method : EntryMethod.values()) {
            entryMethods.add(method.getDisplayName());
        }
        entryMethodCombo.setItems(FXCollections.observableArrayList(entryMethods));
        entryMethodCombo.getSelectionModel().selectFirst();
    }

    /**
     * 初始化数字输入框
     */
    private void initializeSpinners() {
        // 最小上访次数
        SpinnerValueFactory<Integer> minFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0);
        minVisitSpinner.setValueFactory(minFactory);
        minVisitSpinner.setEditable(true);

        // 最大上访次数
        SpinnerValueFactory<Integer> maxFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 100);
        maxVisitSpinner.setValueFactory(maxFactory);
        maxVisitSpinner.setEditable(true);
    }

    /**
     * 初始化表格列
     */
    private void initializeTableColumns() {
        // 序号列
        indexColumn.setCellValueFactory(cellData -> {
            int index = resultTable.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleIntegerProperty(index).asObject();
        });

        // 姓名列
        nameColumn.setCellValueFactory(cellData -> {
            String name = cellData.getValue().getName();
            return new SimpleStringProperty(name != null ? name : "-");
        });

        // 身份证号列（脱敏显示）
        idCardColumn.setCellValueFactory(cellData -> {
            String idCard = cellData.getValue().getIdCard();
            return new SimpleStringProperty(idCard != null ? IdCardUtil.maskIdCard(idCard) : "-");
        });

        // 性别列
        genderColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPersonalInfo() != null &&
                cellData.getValue().getPersonalInfo().getGender() != null) {
                return new SimpleStringProperty(cellData.getValue().getPersonalInfo().getGender().getDisplayName());
            }
            return new SimpleStringProperty("-");
        });

        // 籍贯列
        nativePlaceColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPersonalInfo() != null) {
                String place = cellData.getValue().getPersonalInfo().getNativePlace();
                return new SimpleStringProperty(place != null ? place : "-");
            }
            return new SimpleStringProperty("-");
        });

        // 上访次数列
        visitCountColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPersonalInfo() != null &&
                cellData.getValue().getPersonalInfo().getVisitCount() != null) {
                return new SimpleIntegerProperty(cellData.getValue().getPersonalInfo().getVisitCount()).asObject();
            }
            return new SimpleIntegerProperty(0).asObject();
        });

        // 危险等级列
        riskLevelColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getRiskAssessment() != null &&
                cellData.getValue().getRiskAssessment().getRiskLevel() != null) {
                return new SimpleStringProperty(cellData.getValue().getRiskAssessment().getRiskLevel().getDisplayName());
            }
            return new SimpleStringProperty("-");
        });

        // 联系电话列
        phoneColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPersonalInfo() != null &&
                cellData.getValue().getPersonalInfo().getPhones() != null &&
                !cellData.getValue().getPersonalInfo().getPhones().isEmpty()) {
                return new SimpleStringProperty(cellData.getValue().getPersonalInfo().getPhones().get(0));
            }
            return new SimpleStringProperty("-");
        });

        // 操作列
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("查看");
            private final HBox buttonBox = new HBox(5, viewButton);

            {
                viewButton.getStyleClass().add("table-button");
                viewButton.setOnAction(event -> {
                    Petitioner petitioner = getTableView().getItems().get(getIndex());
                    handleView(petitioner);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });

        // 设置表格数据
        resultTable.setItems(resultData);
    }

    // ==================== 事件处理方法 ====================

    /**
     * 处理查询按钮点击
     */
    @FXML
    private void handleSearch() {
        try {
            // 构建查询条件
            List<Petitioner> allData = queryService.advancedQuery(null);
            List<Petitioner> filteredData = new ArrayList<>();

            for (Petitioner petitioner : allData) {
                if (matchesCriteria(petitioner)) {
                    filteredData.add(petitioner);
                }
            }

            // 更新结果
            resultData.clear();
            resultData.addAll(filteredData);
            resultCountLabel.setText("共 " + filteredData.size() + " 条结果");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "错误", "查询失败", e.getMessage());
        }
    }

    /**
     * 判断人员是否匹配查询条件
     */
    private boolean matchesCriteria(Petitioner petitioner) {
        // 姓名匹配
        String nameKeyword = nameField.getText();
        if (nameKeyword != null && !nameKeyword.trim().isEmpty()) {
            if (petitioner.getName() == null ||
                !petitioner.getName().contains(nameKeyword.trim())) {
                return false;
            }
        }

        // 身份证号匹配
        String idCardKeyword = idCardField.getText();
        if (idCardKeyword != null && !idCardKeyword.trim().isEmpty()) {
            if (petitioner.getIdCard() == null ||
                !petitioner.getIdCard().contains(idCardKeyword.trim())) {
                return false;
            }
        }

        // 籍贯匹配
        String nativePlaceKeyword = nativePlaceField.getText();
        if (nativePlaceKeyword != null && !nativePlaceKeyword.trim().isEmpty()) {
            if (petitioner.getPersonalInfo() == null ||
                petitioner.getPersonalInfo().getNativePlace() == null ||
                !petitioner.getPersonalInfo().getNativePlace().contains(nativePlaceKeyword.trim())) {
                return false;
            }
        }

        // 危险等级匹配
        String selectedRiskLevel = riskLevelCombo.getValue();
        if (selectedRiskLevel != null && !"全部".equals(selectedRiskLevel)) {
            if (petitioner.getRiskAssessment() == null ||
                petitioner.getRiskAssessment().getRiskLevel() == null ||
                !selectedRiskLevel.equals(petitioner.getRiskAssessment().getRiskLevel().getDisplayName())) {
                return false;
            }
        }

        // 性别匹配
        String selectedGender = genderCombo.getValue();
        if (selectedGender != null && !"全部".equals(selectedGender)) {
            if (petitioner.getPersonalInfo() == null ||
                petitioner.getPersonalInfo().getGender() == null ||
                !selectedGender.equals(petitioner.getPersonalInfo().getGender().getDisplayName())) {
                return false;
            }
        }

        // 文化程度匹配
        String selectedEducation = educationCombo.getValue();
        if (selectedEducation != null && !"全部".equals(selectedEducation)) {
            if (petitioner.getPersonalInfo() == null ||
                petitioner.getPersonalInfo().getEducation() == null ||
                !selectedEducation.equals(petitioner.getPersonalInfo().getEducation().getDisplayName())) {
                return false;
            }
        }

        // 婚姻状况匹配
        String selectedMaritalStatus = maritalStatusCombo.getValue();
        if (selectedMaritalStatus != null && !"全部".equals(selectedMaritalStatus)) {
            if (petitioner.getPersonalInfo() == null ||
                petitioner.getPersonalInfo().getMaritalStatus() == null ||
                !selectedMaritalStatus.equals(petitioner.getPersonalInfo().getMaritalStatus().getDisplayName())) {
                return false;
            }
        }

        // 进京方式匹配
        String selectedEntryMethod = entryMethodCombo.getValue();
        if (selectedEntryMethod != null && !"全部".equals(selectedEntryMethod)) {
            if (petitioner.getPetitionCase() == null ||
                petitioner.getPetitionCase().getEntryMethod() == null ||
                !selectedEntryMethod.equals(petitioner.getPetitionCase().getEntryMethod().getDisplayName())) {
                return false;
            }
        }

        // 上访次数范围匹配
        Integer minCount = minVisitSpinner.getValue();
        Integer maxCount = maxVisitSpinner.getValue();

        // 如果设置了范围条件(不是默认的0-100)
        if (minCount != null && maxCount != null && !(minCount == 0 && maxCount == 100)) {
            if (petitioner.getPersonalInfo() != null &&
                petitioner.getPersonalInfo().getVisitCount() != null) {
                int count = petitioner.getPersonalInfo().getVisitCount();
                if (count < minCount || count > maxCount) {
                    return false;
                }
            } else {
                // 如果没有上访次数数据,且设置了非默认范围,则不匹配
                return false;
            }
        }

        return true;
    }

    /**
     * 处理重置按钮点击
     */
    @FXML
    private void handleReset() {
        // 清空文本框
        nameField.clear();
        idCardField.clear();
        nativePlaceField.clear();

        // 重置下拉框
        riskLevelCombo.getSelectionModel().selectFirst();
        genderCombo.getSelectionModel().selectFirst();
        educationCombo.getSelectionModel().selectFirst();
        maritalStatusCombo.getSelectionModel().selectFirst();
        entryMethodCombo.getSelectionModel().selectFirst();

        // 重置数字输入框
        minVisitSpinner.getValueFactory().setValue(0);
        maxVisitSpinner.getValueFactory().setValue(100);

        // 清空结果
        resultData.clear();
        resultCountLabel.setText("共 0 条结果");
    }

    /**
     * 处理导出按钮点击
     */
    @FXML
    private void handleExport() {
        if (resultData.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "警告", "无数据可导出", "请先执行查询获取结果");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出查询结果");
        fileChooser.setInitialFileName("查询结果_" + System.currentTimeMillis());
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Excel文件", "*.xlsx"),
            new FileChooser.ExtensionFilter("CSV文件", "*.csv")
        );

        File file = fileChooser.showSaveDialog(resultTable.getScene().getWindow());
        if (file != null) {
            try {
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(".xlsx")) {
                    List<Petitioner> dataToExport = new ArrayList<>(resultData);
                    exportService.exportToExcel(file.getAbsolutePath(), dataToExport);
                } else if (fileName.endsWith(".csv")) {
                    List<Petitioner> dataToExport = new ArrayList<>(resultData);
                    exportService.exportToCsv(file.getAbsolutePath(), dataToExport);
                }
                showAlert(Alert.AlertType.INFORMATION, "成功", "导出成功",
                    "已导出 " + resultData.size() + " 条记录到：\n" + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "错误", "导出失败", e.getMessage());
            }
        }
    }

    /**
     * 查看人员详情
     */
    private void handleView(Petitioner petitioner) {
        if (petitioner == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detail.fxml"));
            Parent root = loader.load();

            DetailController controller = loader.getController();
            controller.setData(petitioner);
            controller.setOnDataChangedCallback(() -> handleSearch());

            // 创建美化的弹窗
            Stage parentStage = (Stage) resultTable.getScene().getWindow();
            Stage stage = com.petition.util.StageUtil.createStyledDialog(
                "👤 人员详情 - " + petitioner.getName(),
                root, parentStage, 1100, 750
            );
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "错误", "打开详情页面失败", e.getMessage());
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
