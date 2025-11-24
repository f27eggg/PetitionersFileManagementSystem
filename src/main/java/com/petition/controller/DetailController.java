package com.petition.controller;

import com.petition.model.Petitioner;
import com.petition.model.enums.RiskLevel;
import com.petition.service.PetitionerService;
import com.petition.util.DateUtil;
import com.petition.util.IdCardUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 详情页面控制器
 * 功能：显示上访人员的详细信息（四个标签页）
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class DetailController implements Initializable {

    // ==================== FXML 注入组件 ====================

    // 顶部组件
    @FXML private Button backButton;
    @FXML private Label titleLabel;
    @FXML private Label riskBadge;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private TabPane tabPane;

    // 个人信息标签页
    @FXML private Label nameLabel;
    @FXML private Label formerNameLabel;
    @FXML private Label idCardLabel;
    @FXML private Label genderLabel;
    @FXML private Label birthDateLabel;
    @FXML private Label ageLabel;
    @FXML private Label nativePlaceLabel;
    @FXML private Label educationLabel;
    @FXML private Label maritalStatusLabel;
    @FXML private Label spouseLabel;
    @FXML private Label phonesLabel;
    @FXML private Label occupationLabel;
    @FXML private Label workAddressLabel;
    @FXML private Label homeAddressLabel;
    @FXML private Label visitCountLabel;
    @FXML private Label counterMeasuresLabel;
    @FXML private Label consumptionHabitsLabel;
    @FXML private Label photosLabel;

    // 在京关系人标签页
    @FXML private Label contactNameLabel;
    @FXML private Label formerAddressLabel;
    @FXML private Label contactIdCardLabel;
    @FXML private Label relationshipLabel;
    @FXML private TextArea assistDescriptionArea;
    @FXML private Label contactPhotosLabel;

    // 信访案件标签页
    @FXML private TextArea petitionContentArea;
    @FXML private Label canResolveLabel;
    @FXML private Label resolutionMethodLabel;
    @FXML private TextArea visitTrajectoryArea;
    @FXML private Label entryMethodLabel;
    @FXML private Label transportInBeijingLabel;
    @FXML private Label hasReceptionLabel;

    // 评估结果标签页
    @FXML private Label riskLevelLabel;
    @FXML private Label assessmentDateLabel;
    @FXML private Label riskDescriptionLabel;

    // ==================== 业务属性 ====================

    private Petitioner currentPetitioner;
    private final PetitionerService petitionerService = new PetitionerService();

    // 回调函数（用于刷新列表页）
    private Runnable onDataChangedCallback;

    /**
     * 初始化方法
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化时暂不加载数据，等待外部调用setData()
    }

    /**
     * 设置要显示的上访人员数据
     *
     * @param petitioner 上访人员对象
     */
    public void setData(Petitioner petitioner) {
        if (petitioner == null) {
            showAlert(Alert.AlertType.ERROR, "错误", "数据加载失败", "未能加载人员信息");
            return;
        }

        this.currentPetitioner = petitioner;
        loadPersonalInfo();
        loadBeijingContact();
        loadPetitionCase();
        loadRiskAssessment();
    }

    /**
     * 设置数据变更回调函数
     *
     * @param callback 回调函数
     */
    public void setOnDataChangedCallback(Runnable callback) {
        this.onDataChangedCallback = callback;
    }

    /**
     * 加载个人信息到标签页
     */
    private void loadPersonalInfo() {
        if (currentPetitioner == null || currentPetitioner.getPersonalInfo() == null) {
            return;
        }

        var info = currentPetitioner.getPersonalInfo();

        // 基本信息
        nameLabel.setText(info.getName() != null ? info.getName() : "-");
        formerNameLabel.setText(info.getFormerName() != null && !info.getFormerName().isEmpty() ? info.getFormerName() : "-");
        idCardLabel.setText(info.getIdCard() != null ? IdCardUtil.maskIdCard(info.getIdCard()) : "-");
        genderLabel.setText(info.getGender() != null ? info.getGender().getDisplayName() : "-");

        // 出生日期和年龄（从身份证号提取）
        if (info.getIdCard() != null && IdCardUtil.isValid(info.getIdCard())) {
            LocalDate birthDate = IdCardUtil.extractBirthDate(info.getIdCard());
            if (birthDate != null) {
                birthDateLabel.setText(DateUtil.formatDate(birthDate, "yyyy-MM-dd"));
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                ageLabel.setText(age + " 岁");
            } else {
                birthDateLabel.setText("-");
                ageLabel.setText("-");
            }
        } else {
            birthDateLabel.setText("-");
            ageLabel.setText("-");
        }

        // 其他信息
        nativePlaceLabel.setText(info.getNativePlace() != null && !info.getNativePlace().isEmpty() ? info.getNativePlace() : "-");
        educationLabel.setText(info.getEducation() != null ? info.getEducation().getDisplayName() : "-");
        maritalStatusLabel.setText(info.getMaritalStatus() != null ? info.getMaritalStatus().getDisplayName() : "-");
        spouseLabel.setText(info.getSpouse() != null && !info.getSpouse().isEmpty() ? info.getSpouse() : "-");

        // 联系电话列表
        List<String> phones = info.getPhones();
        if (phones != null && !phones.isEmpty()) {
            phonesLabel.setText(String.join(" / ", phones));
        } else {
            phonesLabel.setText("-");
        }

        // 职业和地址
        occupationLabel.setText(info.getOccupation() != null && !info.getOccupation().isEmpty() ? info.getOccupation() : "-");
        workAddressLabel.setText(info.getWorkAddress() != null && !info.getWorkAddress().isEmpty() ? info.getWorkAddress() : "-");
        homeAddressLabel.setText(info.getHomeAddress() != null && !info.getHomeAddress().isEmpty() ? info.getHomeAddress() : "-");

        // 上访相关信息
        visitCountLabel.setText(info.getVisitCount() != null ? String.valueOf(info.getVisitCount()) + " 次" : "-");
        counterMeasuresLabel.setText(info.getCounterMeasures() != null && !info.getCounterMeasures().isEmpty() ? info.getCounterMeasures() : "-");
        consumptionHabitsLabel.setText(info.getConsumptionHabits() != null && !info.getConsumptionHabits().isEmpty() ? info.getConsumptionHabits() : "-");

        // 照片列表
        List<String> photos = info.getPhotos();
        if (photos != null && !photos.isEmpty()) {
            photosLabel.setText(String.join("\n", photos));
        } else {
            photosLabel.setText("-");
        }
    }

    /**
     * 加载在京关系人信息到标签页
     */
    private void loadBeijingContact() {
        if (currentPetitioner == null || currentPetitioner.getBeijingContact() == null) {
            return;
        }

        var contact = currentPetitioner.getBeijingContact();

        contactNameLabel.setText(contact.getContactName() != null && !contact.getContactName().isEmpty() ? contact.getContactName() : "-");
        formerAddressLabel.setText(contact.getFormerAddress() != null && !contact.getFormerAddress().isEmpty() ? contact.getFormerAddress() : "-");
        contactIdCardLabel.setText(contact.getContactIdCard() != null && !contact.getContactIdCard().isEmpty() ? IdCardUtil.maskIdCard(contact.getContactIdCard()) : "-");
        relationshipLabel.setText(contact.getRelationship() != null && !contact.getRelationship().isEmpty() ? contact.getRelationship() : "-");
        assistDescriptionArea.setText(contact.getAssistDescription() != null && !contact.getAssistDescription().isEmpty() ? contact.getAssistDescription() : "-");

        // 照片列表
        List<String> photos = contact.getPhotos();
        if (photos != null && !photos.isEmpty()) {
            contactPhotosLabel.setText(String.join("\n", photos));
        } else {
            contactPhotosLabel.setText("-");
        }
    }

    /**
     * 加载信访案件信息到标签页
     */
    private void loadPetitionCase() {
        if (currentPetitioner == null || currentPetitioner.getPetitionCase() == null) {
            return;
        }

        var petitionCase = currentPetitioner.getPetitionCase();

        // 诉求内容
        petitionContentArea.setText(petitionCase.getPetitionContent() != null && !petitionCase.getPetitionContent().isEmpty() ? petitionCase.getPetitionContent() : "-");

        // 解决相关
        canResolveLabel.setText(petitionCase.getCanResolve() != null && !petitionCase.getCanResolve().isEmpty() ? petitionCase.getCanResolve() : "-");
        resolutionMethodLabel.setText(petitionCase.getResolutionMethod() != null && !petitionCase.getResolutionMethod().isEmpty() ? petitionCase.getResolutionMethod() : "-");

        // 上访轨迹
        visitTrajectoryArea.setText(petitionCase.getVisitTrajectory() != null && !petitionCase.getVisitTrajectory().isEmpty() ? petitionCase.getVisitTrajectory() : "-");

        // 进京和通行方式
        entryMethodLabel.setText(petitionCase.getEntryMethod() != null ? petitionCase.getEntryMethod().getDisplayName() : "-");
        transportInBeijingLabel.setText(petitionCase.getTransportInBeijing() != null ? petitionCase.getTransportInBeijing().getDisplayName() : "-");

        // 接应人
        hasReceptionLabel.setText(petitionCase.getHasReception() != null && !petitionCase.getHasReception().isEmpty() ? petitionCase.getHasReception() : "-");
    }

    /**
     * 加载评估结果到标签页
     */
    private void loadRiskAssessment() {
        if (currentPetitioner == null || currentPetitioner.getRiskAssessment() == null) {
            return;
        }

        var assessment = currentPetitioner.getRiskAssessment();
        RiskLevel riskLevel = assessment.getRiskLevel();

        if (riskLevel != null) {
            // 更新顶部风险等级徽章
            riskBadge.setText("风险等级：" + riskLevel.getDisplayName());
            riskBadge.getStyleClass().removeAll("risk-badge-low", "risk-badge-medium", "risk-badge-high", "risk-badge-extreme");
            riskBadge.getStyleClass().add(getRiskBadgeStyleClass(riskLevel));

            // 更新标签页中的风险等级
            riskLevelLabel.setText(riskLevel.getDisplayName());
            riskLevelLabel.getStyleClass().removeAll("risk-low", "risk-medium", "risk-high", "risk-extreme");
            riskLevelLabel.getStyleClass().add(getRiskLabelStyleClass(riskLevel));

            // 风险等级描述
            riskDescriptionLabel.setText(getRiskDescription(riskLevel));
        } else {
            riskBadge.setText("风险等级：未评估");
            riskLevelLabel.setText("未评估");
            riskDescriptionLabel.setText("-");
        }

        // 评估时间（使用创建时间代替）
        if (currentPetitioner.getCreateTime() != null) {
            assessmentDateLabel.setText(DateUtil.formatDateTime(currentPetitioner.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            assessmentDateLabel.setText("-");
        }
    }

    /**
     * 根据风险等级获取徽章样式类
     */
    private String getRiskBadgeStyleClass(RiskLevel riskLevel) {
        if (riskLevel == RiskLevel.LOW) return "risk-badge-low";
        if (riskLevel == RiskLevel.MEDIUM) return "risk-badge-medium";
        if (riskLevel == RiskLevel.HIGH) return "risk-badge-high";
        if (riskLevel == RiskLevel.CRITICAL) return "risk-badge-extreme";
        return "";
    }

    /**
     * 根据风险等级获取标签样式类
     */
    private String getRiskLabelStyleClass(RiskLevel riskLevel) {
        if (riskLevel == RiskLevel.LOW) return "risk-low";
        if (riskLevel == RiskLevel.MEDIUM) return "risk-medium";
        if (riskLevel == RiskLevel.HIGH) return "risk-high";
        if (riskLevel == RiskLevel.CRITICAL) return "risk-extreme";
        return "";
    }

    /**
     * 获取风险等级描述
     */
    private String getRiskDescription(RiskLevel riskLevel) {
        if (riskLevel == RiskLevel.LOW) return "上访次数较少，诉求合理，情绪稳定，无明显风险倾向";
        if (riskLevel == RiskLevel.MEDIUM) return "多次上访，诉求复杂，情绪波动，需要重点关注和引导";
        if (riskLevel == RiskLevel.HIGH) return "频繁上访，行为激进，存在安全隐患，需要严密监控";
        if (riskLevel == RiskLevel.CRITICAL) return "存在极端倾向，可能危害公共安全，需24小时监控并采取预防措施";
        return "-";
    }

    // ==================== 事件处理方法 ====================

    /**
     * 处理返回按钮点击
     */
    @FXML
    private void handleBack() {
        // 关闭当前窗口
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    /**
     * 处理编辑按钮点击
     */
    @FXML
    private void handleEdit() {
        if (currentPetitioner == null) {
            return;
        }

        try {
            // 加载表单页面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/form.fxml"));
            Parent root = loader.load();

            // 获取控制器并设置为编辑模式
            FormController controller = loader.getController();
            controller.setEditMode(currentPetitioner);

            // 设置数据变更回调
            controller.setOnSaveCallback(() -> {
                // 重新加载数据
                String id = currentPetitioner.getId();
                Optional<Petitioner> updatedPetitioner = petitionerService.getPetitionerById(id);
                updatedPetitioner.ifPresent(this::setData);
                // 通知列表页刷新
                if (onDataChangedCallback != null) {
                    onDataChangedCallback.run();
                }
            });

            // 创建美化的弹窗
            Stage parentStage = (Stage) editButton.getScene().getWindow();
            Stage stage = com.petition.util.StageUtil.createStyledDialog(
                "✏️ 编辑人员信息 - " + currentPetitioner.getPersonalInfo().getName(),
                root, parentStage, 1200, 800
            );
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "错误", "打开编辑页面失败", e.getMessage());
        }
    }

    /**
     * 处理删除按钮点击
     */
    @FXML
    private void handleDelete() {
        if (currentPetitioner == null) {
            return;
        }

        // 确认删除
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("确认删除");
        confirmAlert.setHeaderText("您确定要删除此人员信息吗？");
        confirmAlert.setContentText("姓名：" + currentPetitioner.getPersonalInfo().getName() + "\n" +
                "身份证号：" + IdCardUtil.maskIdCard(currentPetitioner.getPersonalInfo().getIdCard()) + "\n\n" +
                "此操作不可恢复！");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // 执行删除
                boolean success = petitionerService.deletePetitioner(currentPetitioner.getId());

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "成功", "删除成功", "人员信息已删除");

                    // 通知列表页刷新
                    if (onDataChangedCallback != null) {
                        onDataChangedCallback.run();
                    }

                    // 关闭当前窗口
                    handleBack();
                } else {
                    showAlert(Alert.AlertType.ERROR, "失败", "删除失败", "未能删除人员信息");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "错误", "删除失败", e.getMessage());
            }
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
