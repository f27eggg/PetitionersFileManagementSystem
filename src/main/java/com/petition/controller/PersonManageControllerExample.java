package com.petition.controller;

import com.petition.dao.PhotoDao;
import com.petition.model.Petitioner;
import com.petition.model.Photo;
import com.petition.service.PetitionerService;
import com.petition.service.PhotoService;
import com.petition.util.*;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * 人员管理控制器示例
 * 展示如何使用向导式弹窗、下滑式查看和照片管理
 */
public class PersonManageControllerExample implements Initializable {

    @FXML private TableView<Petitioner> personTable;
    @FXML private TableColumn<Petitioner, ImageView> colAvatar;
    @FXML private TableColumn<Petitioner, String> colName;
    @FXML private TableColumn<Petitioner, String> colIdCard;
    @FXML private TableColumn<Petitioner, String> colRiskLevel;
    @FXML private TableColumn<Petitioner, String> colPhone;
    @FXML private TableColumn<Petitioner, Void> colActions;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> riskFilter;
    @FXML private Button addButton;

    private PetitionerService petitionerService;
    private PhotoService photoService;
    private ObservableList<Petitioner> personList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        petitionerService = new PetitionerService();
        photoService = new PhotoService();
        personList = FXCollections.observableArrayList();

        // 初始化照片表
        PhotoService.init();

        setupTable();
        setupFilters();
        loadData();
    }

    /**
     * 设置表格
     */
    private void setupTable() {
        // 头像列 - 显示照片缩略图
        colAvatar.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private final StackPane container = new StackPane(imageView);
            
            {
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                container.getStyleClass().add("table-avatar");
                container.setOnMouseClicked(e -> {
                    Petitioner p = getTableRow().getItem();
                    if (p != null && e.getClickCount() == 1) {
                        showPhotoViewer(p);
                    }
                });
            }
            
            @Override
            protected void updateItem(ImageView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Petitioner person = getTableRow().getItem();
                    String photoPath = photoService.getPrimaryPhotoPath(person.getId());
                    if (photoPath != null) {
                        try {
                            Image img = new Image("file:" + photoPath, 40, 40, true, true);
                            imageView.setImage(img);
                        } catch (Exception ex) {
                            imageView.setImage(null);
                        }
                    } else {
                        imageView.setImage(null);
                    }
                    setGraphic(container);
                }
            }
        });

        // 其他列
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colIdCard.setCellValueFactory(new PropertyValueFactory<>("idCard"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // 风险等级列 - 带徽章
        colRiskLevel.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = SlideViewerDialog.createRiskBadge(item);
                    setGraphic(badge);
                }
            }
        });
        colRiskLevel.setCellValueFactory(new PropertyValueFactory<>("riskLevel"));

        // 操作列
        colActions.setCellFactory(col -> new TableCell<>() {
            private final HBox buttons = new HBox(8);
            private final Button viewBtn = new Button("查看");
            private final Button editBtn = new Button("编辑");
            private final Button deleteBtn = new Button("删除");
            
            {
                viewBtn.getStyleClass().addAll("btn", "btn-sm", "btn-secondary");
                editBtn.getStyleClass().addAll("btn", "btn-sm", "btn-primary");
                deleteBtn.getStyleClass().addAll("btn", "btn-sm", "btn-danger");
                
                viewBtn.setOnAction(e -> {
                    Petitioner p = getTableRow().getItem();
                    if (p != null) showViewDialog(p);
                });
                
                editBtn.setOnAction(e -> {
                    Petitioner p = getTableRow().getItem();
                    if (p != null) showEditWizard(p);
                });
                
                deleteBtn.setOnAction(e -> {
                    Petitioner p = getTableRow().getItem();
                    if (p != null) confirmDelete(p);
                });
                
                buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn);
                buttons.setAlignment(Pos.CENTER);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        personTable.setItems(personList);

        // 双击查看详情
        personTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Petitioner selected = personTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showViewDialog(selected);
                }
            }
        });
    }

    /**
     * 设置筛选器
     */
    private void setupFilters() {
        riskFilter.setItems(FXCollections.observableArrayList(
            "全部", "低风险", "中风险", "高风险", "极高风险"
        ));
        riskFilter.setValue("全部");
        riskFilter.setOnAction(e -> filterData());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterData());

        addButton.setOnAction(e -> showAddWizard());
    }

    /**
     * 加载数据
     */
    private void loadData() {
        // 这里应该从service加载数据
        // personList.setAll(petitionerService.getAll());
    }

    /**
     * 筛选数据
     */
    private void filterData() {
        // 实现筛选逻辑
    }

    /**
     * 显示新增向导
     */
    @FXML
    private void showAddWizard() {
        Petitioner newPerson = new Petitioner();
        List<String> photoPaths = new ArrayList<>();

        // 创建表单字段
        TextField nameField = new TextField();
        TextField idCardField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        ComboBox<String> genderBox = new ComboBox<>(FXCollections.observableArrayList("男", "女"));
        DatePicker birthPicker = new DatePicker();
        TextArea reasonArea = new TextArea();
        reasonArea.setPrefRowCount(3);
        StringProperty riskLevel = new SimpleStringProperty("低风险");

        // 验证属性
        BooleanProperty step1Valid = new SimpleBooleanProperty(false);
        BooleanProperty step2Valid = new SimpleBooleanProperty(true);
        BooleanProperty step3Valid = new SimpleBooleanProperty(true);

        // 步骤1验证
        nameField.textProperty().addListener((obs, o, n) -> 
            step1Valid.set(n != null && !n.trim().isEmpty() && 
                          idCardField.getText() != null && !idCardField.getText().trim().isEmpty()));
        idCardField.textProperty().addListener((obs, o, n) -> 
            step1Valid.set(n != null && !n.trim().isEmpty() && 
                          nameField.getText() != null && !nameField.getText().trim().isEmpty()));

        // 照片上传区域
        VBox photoUploader = createPhotoUploader(photoPaths);

        WizardDialog<Petitioner> wizard = new WizardDialog<>(
            getStage(), "新增人员", newPerson
        );

        // 步骤1: 基本信息
        VBox step1Content = new VBox(20);
        step1Content.getChildren().addAll(
            WizardDialog.createSection("基本信息",
                WizardDialog.createTwoColumns(
                    WizardDialog.createField("姓名 *", nameField),
                    WizardDialog.createField("性别", genderBox)
                ),
                WizardDialog.createTwoColumns(
                    WizardDialog.createField("身份证号 *", idCardField),
                    WizardDialog.createField("出生日期", birthPicker)
                ),
                WizardDialog.createField("联系电话", phoneField)
            )
        );

        // 步骤2: 风险评估
        VBox step2Content = new VBox(20);
        step2Content.getChildren().addAll(
            WizardDialog.createSection("风险评估",
                WizardDialog.createField("风险等级", WizardDialog.createRiskLevelSelector(riskLevel)),
                WizardDialog.createField("上访原因", reasonArea, "请详细描述上访原因及诉求")
            )
        );

        // 步骤3: 照片上传
        VBox step3Content = new VBox(20);
        step3Content.getChildren().addAll(
            WizardDialog.createSection("照片信息",
                WizardDialog.createField("人员照片", photoUploader, "支持上传多张照片，第一张将作为头像显示"),
                WizardDialog.createField("家庭住址", addressField)
            )
        );

        wizard.addStep(new WizardDialog.WizardStep("基本信息", "1")
                .subtitle("填写人员基本信息")
                .content(step1Content)
                .valid(step1Valid))
            .addStep(new WizardDialog.WizardStep("风险评估", "2")
                .subtitle("评估风险等级")
                .content(step2Content)
                .valid(step2Valid))
            .addStep(new WizardDialog.WizardStep("照片信息", "3")
                .subtitle("上传照片及地址")
                .content(step3Content)
                .valid(step3Valid))
            .onSubmit(person -> {
                // 保存人员信息
                person.setName(nameField.getText());
                person.setIdCard(idCardField.getText());
                person.setPhone(phoneField.getText());
                person.setAddress(addressField.getText());
                person.setGender(genderBox.getValue());
                person.setRiskLevel(riskLevel.get());
                if (birthPicker.getValue() != null) {
                    person.setBirthDate(birthPicker.getValue().toString());
                }
                person.setReason(reasonArea.getText());

                // 保存到数据库
                // int personId = petitionerService.add(person);

                // 保存照片
                // for (String path : photoPaths) {
                //     photoService.uploadPhoto(personId, new File(path), null, photoPaths.indexOf(path) == 0);
                // }

                DialogUtil.showSuccessAlert("添加成功", "人员信息已保存");
                loadData();
            })
            .onCancel(() -> {
                // 取消时清理临时照片
            });

        wizard.show();
    }

    /**
     * 显示编辑向导
     */
    private void showEditWizard(Petitioner person) {
        List<String> photoPaths = new ArrayList<>(photoService.getPhotoPathsByPerson(person.getId()));

        TextField nameField = new TextField(person.getName());
        TextField idCardField = new TextField(person.getIdCard());
        TextField phoneField = new TextField(person.getPhone());
        TextField addressField = new TextField(person.getAddress());
        ComboBox<String> genderBox = new ComboBox<>(FXCollections.observableArrayList("男", "女"));
        genderBox.setValue(person.getGender());
        TextArea reasonArea = new TextArea(person.getReason());
        reasonArea.setPrefRowCount(3);
        StringProperty riskLevel = new SimpleStringProperty(person.getRiskLevel());

        BooleanProperty valid = new SimpleBooleanProperty(true);

        VBox photoUploader = createPhotoUploader(photoPaths);

        WizardDialog<Petitioner> wizard = new WizardDialog<>(
            getStage(), "编辑人员 - " + person.getName(), person
        );

        // 步骤1
        VBox step1 = new VBox(20);
        step1.getChildren().addAll(
            WizardDialog.createSection("基本信息",
                WizardDialog.createTwoColumns(
                    WizardDialog.createField("姓名", nameField),
                    WizardDialog.createField("性别", genderBox)
                ),
                WizardDialog.createField("身份证号", idCardField),
                WizardDialog.createField("联系电话", phoneField)
            )
        );

        // 步骤2
        VBox step2 = new VBox(20);
        step2.getChildren().addAll(
            WizardDialog.createSection("风险与原因",
                WizardDialog.createField("风险等级", WizardDialog.createRiskLevelSelector(riskLevel)),
                WizardDialog.createField("上访原因", reasonArea)
            )
        );

        // 步骤3
        VBox step3 = new VBox(20);
        step3.getChildren().addAll(
            WizardDialog.createSection("照片与地址",
                WizardDialog.createField("照片管理", photoUploader),
                WizardDialog.createField("家庭住址", addressField)
            )
        );

        wizard.addStep(new WizardDialog.WizardStep("基本信息", "✎").content(step1).valid(valid))
            .addStep(new WizardDialog.WizardStep("风险评估", "⚡").content(step2).valid(valid))
            .addStep(new WizardDialog.WizardStep("照片地址", "📷").content(step3).valid(valid))
            .onSubmit(p -> {
                p.setName(nameField.getText());
                p.setIdCard(idCardField.getText());
                p.setPhone(phoneField.getText());
                p.setAddress(addressField.getText());
                p.setGender(genderBox.getValue());
                p.setRiskLevel(riskLevel.get());
                p.setReason(reasonArea.getText());

                // petitionerService.update(p);
                // 更新照片...

                DialogUtil.showSuccessAlert("保存成功", "人员信息已更新");
                loadData();
            });

        wizard.show();
    }

    /**
     * 显示查看弹窗（下滑式）
     */
    private void showViewDialog(Petitioner person) {
        List<String> photoPaths = photoService.getPhotoPathsByPerson(person.getId());

        SlideViewerDialog viewer = new SlideViewerDialog(getStage(), person.getName() + " - 详细信息");

        // 照片区域
        VBox photoGallery = SlideViewerDialog.createPhotoGallery(photoPaths);

        // 基本信息
        VBox basicInfo = new VBox(12);
        basicInfo.getChildren().addAll(
            SlideViewerDialog.createTwoColumnFields("姓名", person.getName(), "性别", person.getGender()),
            SlideViewerDialog.createTwoColumnFields("身份证号", person.getIdCard(), "联系电话", person.getPhone()),
            SlideViewerDialog.createFieldRow("家庭住址", person.getAddress())
        );

        // 风险信息
        HBox riskInfo = new HBox(16);
        riskInfo.setAlignment(Pos.CENTER_LEFT);
        riskInfo.getChildren().addAll(
            new Label("风险等级:"),
            SlideViewerDialog.createRiskBadge(person.getRiskLevel())
        );

        VBox riskSection = new VBox(12);
        riskSection.getChildren().addAll(
            riskInfo,
            SlideViewerDialog.createFieldRow("上访原因", person.getReason())
        );

        viewer.addSection(new SlideViewerDialog.ViewerSection("照片")
                .icon("📷")
                .content(photoGallery))
            .addSection(new SlideViewerDialog.ViewerSection("基本信息")
                .icon("👤")
                .content(basicInfo))
            .addSection(new SlideViewerDialog.ViewerSection("风险评估")
                .icon("⚠")
                .content(riskSection))
            .onEdit(v -> {
                viewer.close();
                showEditWizard(person);
            });

        viewer.show();
    }

    /**
     * 显示照片查看器
     */
    private void showPhotoViewer(Petitioner person) {
        List<String> paths = photoService.getPhotoPathsByPerson(person.getId());
        if (!paths.isEmpty()) {
            DialogUtil.showImageViewer(getStage(), paths, 0);
        } else {
            DialogUtil.showInfoAlert("提示", "该人员暂无照片");
        }
    }

    /**
     * 确认删除
     */
    private void confirmDelete(Petitioner person) {
        DialogUtil.showDeleteConfirmDialog(person.getName(), () -> {
            // 删除照片
            photoService.deleteAllPhotos(person.getId());
            // 删除人员
            // petitionerService.delete(person.getId());
            
            personList.remove(person);
            DialogUtil.showToast(getStage(), "删除成功", DialogUtil.ToastType.SUCCESS);
        });
    }

    /**
     * 创建照片上传组件
     */
    private VBox createPhotoUploader(List<String> photoPaths) {
        VBox container = new VBox(12);
        FlowPane photosPane = new FlowPane(12, 12);
        photosPane.setPrefWrapLength(450);

        Runnable refresh = () -> {
            photosPane.getChildren().clear();
            for (String path : photoPaths) {
                photosPane.getChildren().add(createPhotoThumb(path, () -> {
                    photoPaths.remove(path);
                    refresh.run();
                }));
            }
            // 添加按钮
            Button addBtn = new Button("+ 添加");
            addBtn.getStyleClass().addAll("btn", "btn-secondary");
            addBtn.setMinSize(80, 80);
            addBtn.setOnAction(e -> {
                List<File> files = DialogUtil.chooseImageFiles(getStage(), true);
                if (files != null) {
                    for (File f : files) {
                        photoPaths.add(f.getAbsolutePath());
                    }
                    refresh.run();
                }
            });
            photosPane.getChildren().add(addBtn);
        };

        refresh.run();
        container.getChildren().add(photosPane);
        return container;
    }

    private StackPane createPhotoThumb(String path, Runnable onRemove) {
        StackPane thumb = new StackPane();
        thumb.getStyleClass().add("photo-thumbnail");
        thumb.setMinSize(80, 80);
        thumb.setMaxSize(80, 80);

        try {
            Image img = new Image("file:" + path, 80, 80, true, true);
            ImageView iv = new ImageView(img);
            iv.setFitWidth(76);
            iv.setFitHeight(76);
            thumb.getChildren().add(iv);
        } catch (Exception e) {
            thumb.getChildren().add(new Label("📷"));
        }

        Button removeBtn = new Button("×");
        removeBtn.setStyle("-fx-background-color: #ff0066; -fx-text-fill: white; -fx-background-radius: 50; -fx-min-width: 20; -fx-min-height: 20; -fx-max-width: 20; -fx-max-height: 20; -fx-padding: 0; -fx-font-size: 12px;");
        removeBtn.setVisible(false);
        StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(removeBtn, new Insets(2));
        removeBtn.setOnAction(e -> onRemove.run());

        thumb.setOnMouseEntered(e -> removeBtn.setVisible(true));
        thumb.setOnMouseExited(e -> removeBtn.setVisible(false));
        thumb.getChildren().add(removeBtn);

        return thumb;
    }

    private Stage getStage() {
        return (Stage) personTable.getScene().getWindow();
    }
}

