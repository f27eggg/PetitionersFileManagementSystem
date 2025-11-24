package com.petition.controller;

import com.petition.model.*;
import com.petition.model.enums.*;
import com.petition.service.PetitionerService;
import com.petition.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 表单页面控制器
 * 功能：新增和编辑上访人员信息
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class FormController implements Initializable {

    // ==================== FXML 注入组件 ====================

    // 顶部
    @FXML private Label titleLabel;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TabPane tabPane;

    // 个人信息标签页
    @FXML private TextField nameField;
    @FXML private TextField formerNameField;
    @FXML private TextField idCardField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private TextField nativePlaceField;
    @FXML private ComboBox<String> educationCombo;
    @FXML private ComboBox<String> maritalStatusCombo;
    @FXML private TextField spouseField;
    @FXML private javafx.scene.layout.VBox phonesContainer; // 动态电话容器
    private List<TextField> phoneFields = new ArrayList<>(); // 电话字段列表
    @FXML private TextField occupationField;
    @FXML private TextField workAddressField;
    @FXML private TextField homeAddressField;
    @FXML private Spinner<Integer> visitCountSpinner;
    @FXML private TextArea counterMeasuresArea;
    @FXML private TextArea consumptionHabitsArea;

    // 在京关系人标签页
    @FXML private TextField contactNameField;
    @FXML private TextField relationshipField;
    @FXML private TextField contactPhoneField;
    @FXML private TextField contactCompanyField;
    @FXML private TextField contactCompanyAddressField;
    @FXML private TextField contactHomeAddressField;

    // 信访案件标签页
    @FXML private TextArea petitionContentArea;
    @FXML private ComboBox<String> canResolveCombo;
    @FXML private TextArea resolutionMethodArea;
    @FXML private TextArea visitTrajectoryArea;
    @FXML private ComboBox<String> entryMethodCombo;
    @FXML private ComboBox<String> transportMethodCombo;
    @FXML private ComboBox<String> hasReceptionCombo;

    // 风险评估标签页
    @FXML private ComboBox<String> riskLevelCombo;

    // 底部
    @FXML private Label errorLabel;

    // ==================== 业务属性 ====================

    private final PetitionerService petitionerService = new PetitionerService();

    // 编辑模式：true=编辑，false=新增
    private boolean isEditMode = false;

    // 被编辑的人员（编辑模式）
    private Petitioner editingPetitioner;

    // 保存成功回调
    private Runnable onSaveCallback;

    // ==================== 初始化方法 ====================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("FormController 初始化...");

        // 初始化下拉框
        initializeComboBoxes();

        // 初始化Spinner
        initializeSpinner();

        // 添加验证监听
        addValidationListeners();

        // 初始化电话号码容器（添加一个默认的电话输入框）
        addPhoneField();

        System.out.println("FormController 初始化完成！");
    }

    /**
     * 初始化所有下拉框
     */
    private void initializeComboBoxes() {
        // 性别
        genderCombo.getItems().addAll(
                Gender.MALE.getDisplayName(),
                Gender.FEMALE.getDisplayName()
        );

        // 文化程度
        for (Education edu : Education.values()) {
            educationCombo.getItems().add(edu.getDisplayName());
        }

        // 婚姻状况
        for (MaritalStatus status : MaritalStatus.values()) {
            maritalStatusCombo.getItems().add(status.getDisplayName());
        }

        // 进京方式
        for (EntryMethod method : EntryMethod.values()) {
            entryMethodCombo.getItems().add(method.getDisplayName());
        }

        // 在京通行方式
        for (TransportMethod method : TransportMethod.values()) {
            transportMethodCombo.getItems().add(method.getDisplayName());
        }

        // 风险等级
        for (RiskLevel level : RiskLevel.values()) {
            riskLevelCombo.getItems().add(level.getDisplayName());
        }
    }

    /**
     * 初始化Spinner
     */
    private void initializeSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 1);
        visitCountSpinner.setValueFactory(valueFactory);
        visitCountSpinner.setEditable(true);
    }

    /**
     * 添加实时验证监听
     */
    private void addValidationListeners() {
        // 姓名验证
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                if (ValidationUtil.isNotEmpty(newVal) && newVal.length() >= 2 && newVal.length() <= 50) {
                    nameField.setStyle("-fx-border-color: transparent;");
                } else {
                    nameField.setStyle("-fx-border-color: #ef4444; -fx-border-width: 2;");
                }
            }
        });

        // 身份证号验证
        idCardField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                if (ValidationUtil.isValidIdCard(newVal)) {
                    idCardField.setStyle("-fx-border-color: #10b981; -fx-border-width: 2;");
                } else {
                    idCardField.setStyle("-fx-border-color: #ef4444; -fx-border-width: 2;");
                }
            }
        });
    }

    /**
     * 添加电话号码输入框的验证监听
     */
    private void addPhoneValidationListener(TextField phoneField) {
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                if (ValidationUtil.isValidPhone(newVal)) {
                    phoneField.setStyle("-fx-border-color: #10b981; -fx-border-width: 2;");
                } else {
                    phoneField.setStyle("-fx-border-color: #ef4444; -fx-border-width: 2;");
                }
            }
        });
    }

    /**
     * 动态添加电话号码输入框
     */
    @FXML
    private void addPhoneField() {
        // 创建一个水平容器存放输入框和删除按钮
        javafx.scene.layout.HBox phoneBox = new javafx.scene.layout.HBox(10);
        phoneBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // 创建电话输入框
        TextField phoneField = new TextField();
        phoneField.setPromptText("请输入11位手机号");
        phoneField.setPrefWidth(250);
        javafx.scene.layout.HBox.setHgrow(phoneField, javafx.scene.layout.Priority.ALWAYS);

        // 添加验证监听
        addPhoneValidationListener(phoneField);

        // 创建删除按钮
        Button removeButton = new Button("❌");
        removeButton.getStyleClass().add("danger-button");
        removeButton.setOnAction(e -> removePhoneField(phoneBox, phoneField));

        // 如果只有一个电话框，不显示删除按钮
        if (phoneFields.isEmpty()) {
            removeButton.setVisible(false);
            removeButton.setManaged(false);
        }

        phoneBox.getChildren().addAll(phoneField, removeButton);

        // 添加到容器
        phonesContainer.getChildren().add(phoneBox);
        phoneFields.add(phoneField);

        // 如果添加第二个电话框，显示第一个的删除按钮
        if (phoneFields.size() == 2 && phonesContainer.getChildren().size() > 0) {
            javafx.scene.layout.HBox firstBox = (javafx.scene.layout.HBox) phonesContainer.getChildren().get(0);
            if (firstBox.getChildren().size() > 1) {
                Button firstRemoveBtn = (Button) firstBox.getChildren().get(1);
                firstRemoveBtn.setVisible(true);
                firstRemoveBtn.setManaged(true);
            }
        }
    }

    /**
     * 移除电话号码输入框
     */
    private void removePhoneField(javafx.scene.layout.HBox phoneBox, TextField phoneField) {
        phonesContainer.getChildren().remove(phoneBox);
        phoneFields.remove(phoneField);

        // 如果只剩一个电话框，隐藏删除按钮
        if (phoneFields.size() == 1 && phonesContainer.getChildren().size() > 0) {
            javafx.scene.layout.HBox lastBox = (javafx.scene.layout.HBox) phonesContainer.getChildren().get(0);
            if (lastBox.getChildren().size() > 1) {
                Button removeBtn = (Button) lastBox.getChildren().get(1);
                removeBtn.setVisible(false);
                removeBtn.setManaged(false);
            }
        }
    }

    // ==================== 公共方法 ====================

    /**
     * 设置为新增模式
     */
    public void setAddMode() {
        isEditMode = false;
        titleLabel.setText("📝 新增人员信息");
        clearForm();
    }

    /**
     * 设置为编辑模式
     */
    public void setEditMode(Petitioner petitioner) {
        isEditMode = true;
        editingPetitioner = petitioner;
        titleLabel.setText("✏️ 编辑人员信息");
        loadPetitionerData(petitioner);
    }

    /**
     * 设置保存成功回调
     */
    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    // ==================== 数据加载 ====================

    /**
     * 加载人员数据到表单
     */
    private void loadPetitionerData(Petitioner petitioner) {
        // 个人信息
        PersonalInfo info = petitioner.getPersonalInfo();
        nameField.setText(info.getName());
        formerNameField.setText(info.getFormerName());
        idCardField.setText(info.getIdCard());

        if (info.getGender() != null) {
            genderCombo.setValue(info.getGender().getDisplayName());
        }

        nativePlaceField.setText(info.getNativePlace());

        if (info.getEducation() != null) {
            educationCombo.setValue(info.getEducation().getDisplayName());
        }

        if (info.getMaritalStatus() != null) {
            maritalStatusCombo.setValue(info.getMaritalStatus().getDisplayName());
        }

        spouseField.setText(info.getSpouse());

        // 电话 - 动态加载
        List<String> phones = info.getPhones();
        // 先清空现有电话框
        phonesContainer.getChildren().clear();
        phoneFields.clear();

        if (phones != null && !phones.isEmpty()) {
            // 为每个电话号码添加一个输入框
            for (String phone : phones) {
                addPhoneField();
                phoneFields.get(phoneFields.size() - 1).setText(phone);
            }
        } else {
            // 至少添加一个空的电话框
            addPhoneField();
        }

        occupationField.setText(info.getOccupation());
        workAddressField.setText(info.getWorkAddress());
        homeAddressField.setText(info.getHomeAddress());

        if (info.getVisitCount() != null) {
            visitCountSpinner.getValueFactory().setValue(info.getVisitCount());
        }

        counterMeasuresArea.setText(info.getCounterMeasures());
        consumptionHabitsArea.setText(info.getConsumptionHabits());

        // 在京关系人
        BeijingContact contact = petitioner.getBeijingContact();
        contactNameField.setText(contact.getContactName());
        relationshipField.setText(contact.getRelationship());
        contactPhoneField.setText(contact.getContactIdCard());
        contactCompanyField.setText(contact.getFormerAddress());
        contactCompanyAddressField.setText(contact.getAssistDescription());
        contactHomeAddressField.setText("");

        // 信访案件
        PetitionCase petitionCase = petitioner.getPetitionCase();
        petitionContentArea.setText(petitionCase.getPetitionContent());
        canResolveCombo.setValue(petitionCase.getCanResolve());
        resolutionMethodArea.setText(petitionCase.getResolutionMethod());
        visitTrajectoryArea.setText(petitionCase.getVisitTrajectory());

        if (petitionCase.getEntryMethod() != null) {
            entryMethodCombo.setValue(petitionCase.getEntryMethod().getDisplayName());
        }

        if (petitionCase.getTransportInBeijing() != null) {
            transportMethodCombo.setValue(petitionCase.getTransportInBeijing().getDisplayName());
        }

        hasReceptionCombo.setValue(petitionCase.getHasReception());

        // 风险评估
        RiskAssessment assessment = petitioner.getRiskAssessment();
        if (assessment.getRiskLevel() != null) {
            riskLevelCombo.setValue(assessment.getRiskLevel().getDisplayName());
        }
    }

    /**
     * 清空表单
     */
    private void clearForm() {
        // 个人信息
        nameField.clear();
        formerNameField.clear();
        idCardField.clear();
        genderCombo.setValue(null);
        nativePlaceField.clear();
        educationCombo.setValue(null);
        maritalStatusCombo.setValue(null);
        spouseField.clear();

        // 清空电话框，重置为一个空的输入框
        phonesContainer.getChildren().clear();
        phoneFields.clear();
        addPhoneField();

        occupationField.clear();
        workAddressField.clear();
        homeAddressField.clear();
        visitCountSpinner.getValueFactory().setValue(1);
        counterMeasuresArea.clear();
        consumptionHabitsArea.clear();

        // 在京关系人
        contactNameField.clear();
        relationshipField.clear();
        contactPhoneField.clear();
        contactCompanyField.clear();
        contactCompanyAddressField.clear();
        contactHomeAddressField.clear();

        // 信访案件
        petitionContentArea.clear();
        canResolveCombo.setValue(null);
        resolutionMethodArea.clear();
        visitTrajectoryArea.clear();
        entryMethodCombo.setValue(null);
        transportMethodCombo.setValue(null);
        hasReceptionCombo.setValue(null);

        // 风险评估
        riskLevelCombo.setValue(null);

        // 清除错误提示
        hideError();
    }

    // ==================== 表单验证 ====================

    /**
     * 验证表单
     */
    private boolean validateForm() {
        List<String> errors = new ArrayList<>();

        // 必填项验证
        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errors.add("姓名不能为空");
        } else if (!ValidationUtil.isNotEmpty(nameField.getText())) {
            errors.add("姓名格式不正确");
        }

        if (idCardField.getText() == null || idCardField.getText().trim().isEmpty()) {
            errors.add("身份证号不能为空");
        } else if (!ValidationUtil.isValidIdCard(idCardField.getText())) {
            errors.add("身份证号格式不正确");
        }

        if (genderCombo.getValue() == null) {
            errors.add("请选择性别");
        }

        // 验证已填写的电话号码格式（可选，但如果填写了必须正确）
        for (TextField phoneField : phoneFields) {
            String phone = phoneField.getText();
            if (phone != null && !phone.trim().isEmpty()) {
                if (!ValidationUtil.isValidPhone(phone)) {
                    errors.add("电话号码格式不正确：" + phone);
                }
            }
        }

        if (riskLevelCombo.getValue() == null) {
            errors.add("请选择危险等级");
        }

        // 显示错误
        if (!errors.isEmpty()) {
            showError(String.join("; ", errors));
            return false;
        }

        hideError();
        return true;
    }

    /**
     * 显示错误信息
     */
    private void showError(String message) {
        errorLabel.setText("❌ " + message);
        errorLabel.setVisible(true);
    }

    /**
     * 隐藏错误信息
     */
    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    // ==================== 事件处理 ====================

    /**
     * 保存按钮点击
     */
    @FXML
    private void handleSave() {
        // 验证表单
        if (!validateForm()) {
            return;
        }

        try {
            // 构建Petitioner对象
            Petitioner petitioner = isEditMode ? editingPetitioner : new Petitioner();

            // 填充个人信息
            PersonalInfo personalInfo = petitioner.getPersonalInfo();
            personalInfo.setName(nameField.getText().trim());
            personalInfo.setFormerName(formerNameField.getText());
            personalInfo.setIdCard(idCardField.getText().trim());
            personalInfo.setGender(findGenderByDisplayName(genderCombo.getValue()));
            personalInfo.setNativePlace(nativePlaceField.getText());
            personalInfo.setEducation(findEducationByDisplayName(educationCombo.getValue()));
            personalInfo.setMaritalStatus(findMaritalStatusByDisplayName(maritalStatusCombo.getValue()));
            personalInfo.setSpouse(spouseField.getText());

            // 电话列表 - 从动态电话框中收集
            List<String> phones = new ArrayList<>();
            for (TextField phoneField : phoneFields) {
                String phone = phoneField.getText();
                if (phone != null && !phone.trim().isEmpty()) {
                    phones.add(phone.trim());
                }
            }
            personalInfo.setPhones(phones);

            personalInfo.setOccupation(occupationField.getText());
            personalInfo.setWorkAddress(workAddressField.getText());
            personalInfo.setHomeAddress(homeAddressField.getText());
            personalInfo.setVisitCount(visitCountSpinner.getValue());
            personalInfo.setCounterMeasures(counterMeasuresArea.getText());
            personalInfo.setConsumptionHabits(consumptionHabitsArea.getText());

            // 填充在京关系人
            BeijingContact contact = petitioner.getBeijingContact();
            contact.setContactName(contactNameField.getText());
            contact.setRelationship(relationshipField.getText());
            contact.setContactIdCard(contactPhoneField.getText());
            contact.setFormerAddress(contactCompanyField.getText());
            contact.setAssistDescription(contactCompanyAddressField.getText());
            

            // 填充信访案件
            PetitionCase petitionCase = petitioner.getPetitionCase();
            petitionCase.setPetitionContent(petitionContentArea.getText());
            petitionCase.setCanResolve(canResolveCombo.getValue());
            petitionCase.setResolutionMethod(resolutionMethodArea.getText());
            petitionCase.setVisitTrajectory(visitTrajectoryArea.getText());
            petitionCase.setEntryMethod(findEntryMethodByDisplayName(entryMethodCombo.getValue()));
            petitionCase.setTransportInBeijing(findTransportMethodByDisplayName(transportMethodCombo.getValue()));
            petitionCase.setHasReception(hasReceptionCombo.getValue());

            // 填充风险评估
            RiskAssessment assessment = petitioner.getRiskAssessment();
            assessment.setRiskLevel(findRiskLevelByDisplayName(riskLevelCombo.getValue()));

            // 保存到服务层
            if (isEditMode) {
                petitioner.touch(); // 更新时间
                petitionerService.updatePetitioner(petitioner);
                showSuccess("更新成功！");
            } else {
                petitionerService.addPetitioner(petitioner);
                showSuccess("保存成功！");
            }

            // 执行回调
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            // 关闭窗口
            closeWindow();

        } catch (Exception e) {
            System.err.println("保存失败: " + e.getMessage());
            e.printStackTrace();
            showError("保存失败: " + e.getMessage());
        }
    }

    /**
     * 取消按钮点击
     */
    @FXML
    private void handleCancel() {
        // 确认对话框
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("确认取消");
        confirm.setHeaderText("取消编辑");
        confirm.setContentText("确定要取消吗？未保存的更改将丢失。");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                closeWindow();
            }
        });
    }

    // ==================== 工具方法 ====================

    /**
     * 根据显示名称查找性别枚举
     */
    private Gender findGenderByDisplayName(String displayName) {
        if (displayName == null) return null;
        for (Gender gender : Gender.values()) {
            if (gender.getDisplayName().equals(displayName)) {
                return gender;
            }
        }
        return null;
    }

    /**
     * 根据显示名称查找学历枚举
     */
    private Education findEducationByDisplayName(String displayName) {
        if (displayName == null) return null;
        for (Education edu : Education.values()) {
            if (edu.getDisplayName().equals(displayName)) {
                return edu;
            }
        }
        return null;
    }

    /**
     * 根据显示名称查找婚姻状况枚举
     */
    private MaritalStatus findMaritalStatusByDisplayName(String displayName) {
        if (displayName == null) return null;
        for (MaritalStatus status : MaritalStatus.values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 根据显示名称查找进京方式枚举
     */
    private EntryMethod findEntryMethodByDisplayName(String displayName) {
        if (displayName == null) return null;
        for (EntryMethod method : EntryMethod.values()) {
            if (method.getDisplayName().equals(displayName)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 根据显示名称查找通行方式枚举
     */
    private TransportMethod findTransportMethodByDisplayName(String displayName) {
        if (displayName == null) return null;
        for (TransportMethod method : TransportMethod.values()) {
            if (method.getDisplayName().equals(displayName)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 根据显示名称查找风险等级枚举
     */
    private RiskLevel findRiskLevelByDisplayName(String displayName) {
        if (displayName == null) return null;
        for (RiskLevel level : RiskLevel.values()) {
            if (level.getDisplayName().equals(displayName)) {
                return level;
            }
        }
        return null;
    }

    /**
     * 显示成功消息
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("操作成功");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 关闭当前窗口
     */
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
