package com.petition.controller;

import com.petition.model.Petitioner;
import com.petition.model.enums.*;
import com.petition.service.PetitionerService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

/**
 * 人员列表页面控制器
 * 功能:显示上访人员列表、搜索、筛选、分页、CRUD操作
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class PetitionersController implements Initializable {

    // ==================== FXML 注入组件 ====================

    // 顶部操作按钮
    @FXML private Button addButton;
    @FXML private Button importButton;
    @FXML private Button exportButton;

    // 搜索和筛选组件 - 扩展高级查询字段
    @FXML private TextField nameField;
    @FXML private TextField idCardField;
    @FXML private TextField nativePlaceField;
    @FXML private ComboBox<String> riskLevelFilter;
    @FXML private ComboBox<String> genderFilter;
    @FXML private ComboBox<String> educationCombo;
    @FXML private ComboBox<String> maritalStatusCombo;
    @FXML private ComboBox<String> entryMethodCombo;
    @FXML private Button resetButton;
    @FXML private Label countLabel;

    // 表格组件
    @FXML private TableView<Petitioner> petitionersTable;
    @FXML private TableColumn<Petitioner, String> idColumn;
    @FXML private TableColumn<Petitioner, String> nameColumn;
    @FXML private TableColumn<Petitioner, String> genderColumn;
    @FXML private TableColumn<Petitioner, Integer> ageColumn;
    @FXML private TableColumn<Petitioner, String> idCardColumn;
    @FXML private TableColumn<Petitioner, String> phoneColumn;
    @FXML private TableColumn<Petitioner, String> categoryColumn;
    @FXML private TableColumn<Petitioner, String> riskLevelColumn;
    @FXML private TableColumn<Petitioner, Integer> riskScoreColumn;
    @FXML private TableColumn<Petitioner, String> lastVisitDateColumn;
    @FXML private TableColumn<Petitioner, Void> actionsColumn;

    // 分页组件
    @FXML private Button firstPageButton;
    @FXML private Button prevPageButton;
    @FXML private TextField pageField;
    @FXML private Label totalPagesLabel;
    @FXML private Button nextPageButton;
    @FXML private Button lastPageButton;
    @FXML private ComboBox<Integer> pageSizeComboBox;

    // ==================== 业务属性 ====================

    private final PetitionerService petitionerService = new PetitionerService();

    // 数据列表
    private ObservableList<Petitioner> allPetitioners;
    private FilteredList<Petitioner> filteredPetitioners;

    // 分页属性
    private int currentPage = 1;
    private int pageSize = 20;
    private int totalPages = 1;

    // ==================== 初始化方法 ====================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("PetitionersController 初始化...");

        // 初始化筛选器
        initializeFilters();

        // 初始化表格列
        initializeTableColumns();

        // 初始化分页组件
        initializePagination();

        // 加载数据
        loadData();

        System.out.println("PetitionersController 初始化完成！");
    }

    /**
     * 初始化筛选器下拉框
     */
    private void initializeFilters() {
        // 性别筛选器
        java.util.List<String> genders = new java.util.ArrayList<>();
        genders.add("全部");
        for (Gender gender : Gender.values()) {
            genders.add(gender.getDisplayName());
        }
        genderFilter.setItems(FXCollections.observableArrayList(genders));
        genderFilter.getSelectionModel().selectFirst();

        // 文化程度
        java.util.List<String> educations = new java.util.ArrayList<>();
        educations.add("全部");
        for (Education edu : Education.values()) {
            educations.add(edu.getDisplayName());
        }
        educationCombo.setItems(FXCollections.observableArrayList(educations));
        educationCombo.getSelectionModel().selectFirst();

        // 婚姻状况
        java.util.List<String> maritalStatuses = new java.util.ArrayList<>();
        maritalStatuses.add("全部");
        for (MaritalStatus status : MaritalStatus.values()) {
            maritalStatuses.add(status.getDisplayName());
        }
        maritalStatusCombo.setItems(FXCollections.observableArrayList(maritalStatuses));
        maritalStatusCombo.getSelectionModel().selectFirst();

        // 危险等级
        java.util.List<String> riskLevels = new java.util.ArrayList<>();
        riskLevels.add("全部");
        for (RiskLevel level : RiskLevel.values()) {
            riskLevels.add(level.getDisplayName());
        }
        riskLevelFilter.setItems(FXCollections.observableArrayList(riskLevels));
        riskLevelFilter.getSelectionModel().selectFirst();

        // 进京方式
        java.util.List<String> entryMethods = new java.util.ArrayList<>();
        entryMethods.add("全部");
        for (EntryMethod method : EntryMethod.values()) {
            entryMethods.add(method.getDisplayName());
        }
        entryMethodCombo.setItems(FXCollections.observableArrayList(entryMethods));
        entryMethodCombo.getSelectionModel().selectFirst();
    }

    /**
     * 初始化表格列绑定
     */
    private void initializeTableColumns() {
        // 编号列(使用行号)
        idColumn.setCellValueFactory(cellData -> {
            int index = petitionersTable.getItems().indexOf(cellData.getValue()) + 1;
            int globalIndex = (currentPage - 1) * pageSize + index;
            return new SimpleStringProperty(String.valueOf(globalIndex));
        });

        // 姓名列
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        // 性别列
        genderColumn.setCellValueFactory(cellData -> {
            Gender gender = cellData.getValue().getPersonalInfo().getGender();
            return new SimpleStringProperty(gender != null ? gender.getDisplayName() : "");
        });

        // 年龄列(根据身份证号计算)
        ageColumn.setCellValueFactory(cellData -> {
            String idCard = cellData.getValue().getIdCard();
            int age = calculateAgeFromIdCard(idCard);
            return new SimpleIntegerProperty(age).asObject();
        });

        // 身份证号列
        idCardColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdCard()));

        // 联系电话列
        phoneColumn.setCellValueFactory(cellData -> {
            String phone = cellData.getValue().getPersonalInfo().getPrimaryPhone();
            return new SimpleStringProperty(phone != null ? phone : "");
        });

        // 类别列(暂时显示进京方式)
        categoryColumn.setCellValueFactory(cellData -> {
            String method = cellData.getValue().getPetitionCase().getEntryMethod() != null
                ? cellData.getValue().getPetitionCase().getEntryMethod().getDisplayName()
                : "未知";
            return new SimpleStringProperty(method);
        });

        // 风险等级列(带颜色标识)
        riskLevelColumn.setCellValueFactory(cellData -> {
            RiskLevel level = cellData.getValue().getRiskAssessment().getRiskLevel();
            return new SimpleStringProperty(level != null ? level.getDisplayName() : "未评估");
        });
        riskLevelColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // 根据风险等级设置颜色
                    if (item.contains("高危")) {
                        setTextFill(Color.web("#ef4444"));
                        setStyle("-fx-font-weight: bold;");
                    } else if (item.contains("中危")) {
                        setTextFill(Color.web("#f59e0b"));
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setTextFill(Color.web("#10b981"));
                    }
                }
            }
        });

        // 评分列(暂时显示上访次数)
        riskScoreColumn.setCellValueFactory(cellData -> {
            Integer visitCount = cellData.getValue().getPersonalInfo().getVisitCount();
            return new SimpleIntegerProperty(visitCount != null ? visitCount : 0).asObject();
        });

        // 最后上访日期列(暂时显示创建时间)
        lastVisitDateColumn.setCellValueFactory(cellData -> {
            String dateStr = cellData.getValue().getCreateTime().toLocalDate().toString();
            return new SimpleStringProperty(dateStr);
        });

        // 操作列(按钮)
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("查看");
            private final Button editButton = new Button("编辑");
            private final Button deleteButton = new Button("删除");
            private final HBox container = new HBox(8, viewButton, editButton, deleteButton);

            {
                container.setAlignment(Pos.CENTER);
                viewButton.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 4 12;");
                editButton.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 4 12;");
                deleteButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 4 12;");

                viewButton.setOnAction(event -> handleView(getTableRow().getItem()));
                editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    /**
     * 根据身份证号计算年龄
     */
    private int calculateAgeFromIdCard(String idCard) {
        if (idCard == null || idCard.length() < 14) {
            return 0;
        }
        try {
            String birthStr = idCard.substring(6, 14);
            int year = Integer.parseInt(birthStr.substring(0, 4));
            int month = Integer.parseInt(birthStr.substring(4, 6));
            int day = Integer.parseInt(birthStr.substring(6, 8));
            LocalDate birthDate = LocalDate.of(year, month, day);
            return Period.between(birthDate, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 初始化分页组件
     */
    private void initializePagination() {
        // 每页条数选项
        pageSizeComboBox.setItems(FXCollections.observableArrayList(
                10, 20, 50, 100
        ));
        pageSizeComboBox.setValue(pageSize);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        System.out.println("正在加载数据...");

        try {
            // 从服务层获取所有数据
            java.util.List<Petitioner> petitioners = petitionerService.getAllPetitioners();
            allPetitioners = FXCollections.observableArrayList(petitioners);

            // 创建可筛选列表
            filteredPetitioners = new FilteredList<>(allPetitioners, p -> true);

            // 更新显示
            updateTableView();
            updateCountLabel();

            System.out.println("数据加载完成，共 " + allPetitioners.size() + " 条记录");
        } catch (Exception e) {
            System.err.println("加载数据失败: " + e.getMessage());
            e.printStackTrace();
            showAlert("加载失败", "无法加载数据: " + e.getMessage());
        }
    }

    /**
     * 更新表格显示(分页)
     */
    private void updateTableView() {
        int totalRecords = filteredPetitioners.size();

        // 计算总页数
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0) totalPages = 1;

        // 确保当前页在有效范围内
        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        // 计算分页范围
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRecords);

        // 提取当前页数据
        java.util.List<Petitioner> pageData = filteredPetitioners.subList(fromIndex, toIndex);
        petitionersTable.setItems(FXCollections.observableArrayList(pageData));

        // 更新分页控件状态
        updatePaginationControls();
    }

    /**
     * 更新分页控件状态
     */
    private void updatePaginationControls() {
        pageField.setText(String.valueOf(currentPage));
        totalPagesLabel.setText("/ " + totalPages + " 页");

        firstPageButton.setDisable(currentPage == 1);
        prevPageButton.setDisable(currentPage == 1);
        nextPageButton.setDisable(currentPage >= totalPages);
        lastPageButton.setDisable(currentPage >= totalPages);
    }

    /**
     * 更新记录数标签
     */
    private void updateCountLabel() {
        int total = filteredPetitioners.size();
        countLabel.setText("共 " + total + " 条记录");
    }

    // ==================== 搜索和筛选事件 ====================

    /**
     * 处理筛选条件变更
     */
    @FXML
    private void handleFilter() {
        applyFilters();
    }

    /**
     * 应用筛选条件（整合高级查询逻辑）
     */
    private void applyFilters() {
        filteredPetitioners.setPredicate(petitioner -> {
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

            // 性别筛选
            String selectedGender = genderFilter.getValue();
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

            // 危险等级筛选
            String selectedRiskLevel = riskLevelFilter.getValue();
            if (selectedRiskLevel != null && !"全部".equals(selectedRiskLevel)) {
                if (petitioner.getRiskAssessment() == null ||
                    petitioner.getRiskAssessment().getRiskLevel() == null ||
                    !selectedRiskLevel.equals(petitioner.getRiskAssessment().getRiskLevel().getDisplayName())) {
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

            return true;
        });

        // 重置到第一页
        currentPage = 1;
        updateTableView();
        updateCountLabel();
    }

    /**
     * 重置筛选条件
     */
    @FXML
    private void handleReset() {
        // 清空文本框
        nameField.clear();
        idCardField.clear();
        nativePlaceField.clear();

        // 重置下拉框
        riskLevelFilter.getSelectionModel().selectFirst();
        genderFilter.getSelectionModel().selectFirst();
        educationCombo.getSelectionModel().selectFirst();
        maritalStatusCombo.getSelectionModel().selectFirst();
        entryMethodCombo.getSelectionModel().selectFirst();

        // 应用筛选（显示全部）
        applyFilters();
    }

    /**
     * 刷新数据
     */
    @FXML
    private void handleRefresh() {
        loadData();
        updateTableView();
    }

    // ==================== 分页事件 ====================

    @FXML
    private void handleFirstPage() {
        currentPage = 1;
        updateTableView();
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            updateTableView();
        }
    }

    @FXML
    private void handleNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            updateTableView();
        }
    }

    @FXML
    private void handleLastPage() {
        currentPage = totalPages;
        updateTableView();
    }

    @FXML
    private void handleJumpPage() {
        try {
            int page = Integer.parseInt(pageField.getText());
            if (page >= 1 && page <= totalPages) {
                currentPage = page;
                updateTableView();
            } else {
                pageField.setText(String.valueOf(currentPage));
                showAlert("页码超出范围", "请输入 1 到 " + totalPages + " 之间的页码");
            }
        } catch (NumberFormatException e) {
            pageField.setText(String.valueOf(currentPage));
            showAlert("输入错误", "请输入有效的页码");
        }
    }

    @FXML
    private void handlePageSizeChange() {
        pageSize = pageSizeComboBox.getValue();
        currentPage = 1;
        updateTableView();
    }

    // ==================== CRUD 操作事件 ====================

    /**
     * 新增人员
     */
    @FXML
    private void handleAdd() {
        openFormDialog(null);
    }

    /**
     * 查看人员详情
     */
    private void handleView(Petitioner petitioner) {
        if (petitioner == null) {
            return;
        }

        try {
            // 加载详情页面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detail.fxml"));
            Parent root = loader.load();

            // 获取控制器并设置数据
            DetailController controller = loader.getController();
            controller.setData(petitioner);

            // 设置数据变更回调（删除或编辑后刷新列表）
            controller.setOnDataChangedCallback(this::loadData);

            // 创建美化的弹窗
            Stage parentStage = (Stage) petitionersTable.getScene().getWindow();
            Stage stage = com.petition.util.StageUtil.createStyledDialog(
                "👤 人员详细信息 - " + petitioner.getPersonalInfo().getName(),
                root, parentStage, 1100, 750
            );
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("错误", "打开详情页面失败：" + e.getMessage());
        }
    }

    /**
     * 编辑人员
     */
    private void handleEdit(Petitioner petitioner) {
        if (petitioner != null) {
            openFormDialog(petitioner);
        }
    }

    /**
     * 打开表单对话框
     */
    private void openFormDialog(Petitioner petitioner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/form.fxml"));
            Parent root = loader.load();

            FormController controller = loader.getController();

            if (petitioner == null) {
                controller.setAddMode();
            } else {
                controller.setEditMode(petitioner);
            }

            // 设置保存成功回调
            controller.setOnSaveCallback(() -> {
                loadData(); // 刷新列表
            });

            // 创建美化的弹窗
            Stage parentStage = (Stage) petitionersTable.getScene().getWindow();
            String title = petitioner == null ? "🆕 新增人员" : "✏️ 编辑人员";
            Stage stage = com.petition.util.StageUtil.createStyledDialog(
                title, root, parentStage, 1200, 800
            );
            stage.show();

        } catch (Exception e) {
            System.err.println("打开表单失败: " + e.getMessage());
            e.printStackTrace();
            showAlert("错误", "无法打开表单: " + e.getMessage());
        }
    }

    /**
     * 删除人员
     */
    private void handleDelete(Petitioner petitioner) {
        if (petitioner != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("确认删除");
            confirm.setHeaderText("删除上访人员");
            confirm.setContentText("确定要删除 " + petitioner.getName() + " 的信息吗？");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        petitionerService.deletePetitioner(petitioner.getId());
                        loadData(); // 重新加载数据
                        showAlert("删除成功", "已删除人员：" + petitioner.getName());
                    } catch (Exception e) {
                        System.err.println("删除失败: " + e.getMessage());
                        e.printStackTrace();
                        showAlert("删除失败", "无法删除人员: " + e.getMessage());
                    }
                }
            });
        }
    }

    /**
     * 导入数据
     */
    @FXML
    private void handleImport() {
        showAlert("功能提示", "数据导入功能将在数据管理页面中实现");
        // TODO: 实现导入功能
    }

    /**
     * 导出数据
     */
    @FXML
    private void handleExport() {
        showAlert("功能提示", "数据导出功能将在数据管理页面中实现");
        // TODO: 实现导出功能
    }

    // ==================== 工具方法 ====================

    /**
     * 显示提示对话框
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
