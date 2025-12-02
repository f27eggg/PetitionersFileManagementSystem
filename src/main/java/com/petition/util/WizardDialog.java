package com.petition.util;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 * 向导式弹窗组件
 * 提供引导式的多步骤表单填写体验
 */
public class WizardDialog<T> {

    private Stage stage;
    private BorderPane root;
    private StackPane contentStack;
    private HBox stepsIndicator;
    private Button prevBtn, nextBtn, submitBtn;
    private Label titleLabel, subtitleLabel;

    private List<WizardStep> steps = new ArrayList<>();
    private int currentStepIndex = 0;
    private T dataModel;
    private Consumer<T> onSubmit;
    private Runnable onCancel;

    private static final String CSS_PATH = "/css/main.css";

    /**
     * 向导步骤定义
     */
    public static class WizardStep {
        private String title;
        private String subtitle;
        private String icon;
        private Node content;
        private Runnable onEnter;
        private BooleanProperty valid = new SimpleBooleanProperty(true);

        public WizardStep(String title, String icon) {
            this.title = title;
            this.icon = icon;
        }

        public WizardStep subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public WizardStep content(Node content) {
            this.content = content;
            return this;
        }

        public WizardStep onEnter(Runnable onEnter) {
            this.onEnter = onEnter;
            return this;
        }

        public WizardStep valid(BooleanProperty valid) {
            this.valid = valid;
            return this;
        }

        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public String getIcon() { return icon; }
        public Node getContent() { return content; }
        public BooleanProperty validProperty() { return valid; }
        public boolean isValid() { return valid.get(); }
    }

    public WizardDialog(Window owner, String title, T dataModel) {
        this.dataModel = dataModel;
        createStage(owner, title);
    }

    private void createStage(Window owner, String title) {
        stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle(title);
        stage.setMinWidth(700);
        stage.setMinHeight(600);

        root = new BorderPane();
        root.getStyleClass().add("wizard-dialog");

        // 顶部标题区
        VBox header = createHeader(title);
        root.setTop(header);

        // 内容区域
        contentStack = new StackPane();
        contentStack.getStyleClass().add("wizard-content");
        root.setCenter(contentStack);

        // 底部按钮区
        HBox footer = createFooter();
        root.setBottom(footer);

        Scene scene = new Scene(root, 800, 700);
        scene.setFill(Color.TRANSPARENT);
        
        try {
            scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        } catch (Exception e) {
            System.err.println("无法加载CSS: " + e.getMessage());
        }

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                handleCancel();
            }
        });

        stage.setScene(scene);
        stage.setOnShown(e -> AnimationUtil.dialogOpen(stage));
    }

    private VBox createHeader(String title) {
        VBox header = new VBox(8);
        header.getStyleClass().add("wizard-header");

        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("wizard-title");

        subtitleLabel = new Label();
        subtitleLabel.getStyleClass().add("wizard-subtitle");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // 步骤指示器
        stepsIndicator = new HBox();
        stepsIndicator.getStyleClass().add("wizard-steps");
        stepsIndicator.setAlignment(Pos.CENTER);

        VBox headerContainer = new VBox(0);
        headerContainer.getChildren().addAll(header, stepsIndicator);

        return headerContainer;
    }

    private HBox createFooter() {
        HBox footer = new HBox(12);
        footer.getStyleClass().add("wizard-footer");
        footer.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("取消");
        cancelBtn.getStyleClass().addAll("btn", "btn-ghost");
        cancelBtn.setOnAction(e -> handleCancel());

        prevBtn = new Button("◀ 上一步");
        prevBtn.getStyleClass().addAll("btn", "btn-secondary");
        prevBtn.setOnAction(e -> goToPreviousStep());

        nextBtn = new Button("下一步 ▶");
        nextBtn.getStyleClass().addAll("btn", "btn-primary");
        nextBtn.setOnAction(e -> goToNextStep());

        submitBtn = new Button("✓ 提交保存");
        submitBtn.getStyleClass().addAll("btn", "btn-success", "btn-lg");
        submitBtn.setOnAction(e -> handleSubmit());
        submitBtn.setVisible(false);
        submitBtn.setManaged(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        footer.getChildren().addAll(cancelBtn, spacer, prevBtn, nextBtn, submitBtn);

        return footer;
    }

    public WizardDialog<T> addStep(WizardStep step) {
        steps.add(step);
        return this;
    }

    public WizardDialog<T> onSubmit(Consumer<T> onSubmit) {
        this.onSubmit = onSubmit;
        return this;
    }

    public WizardDialog<T> onCancel(Runnable onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    public void show() {
        if (steps.isEmpty()) {
            throw new IllegalStateException("向导至少需要一个步骤");
        }

        buildStepsIndicator();
        showStep(0, false);
        stage.show();
    }

    private void buildStepsIndicator() {
        stepsIndicator.getChildren().clear();

        for (int i = 0; i < steps.size(); i++) {
            WizardStep step = steps.get(i);
            
            // 步骤圆圈
            VBox stepBox = new VBox(6);
            stepBox.setAlignment(Pos.CENTER);
            stepBox.getStyleClass().add("wizard-step");

            StackPane circle = new StackPane();
            circle.getStyleClass().add("wizard-step-circle");
            circle.setMinSize(40, 40);
            circle.setMaxSize(40, 40);

            Label numLabel = new Label(step.getIcon() != null ? step.getIcon() : String.valueOf(i + 1));
            numLabel.getStyleClass().add("wizard-step-number");
            circle.getChildren().add(numLabel);

            Label textLabel = new Label(step.getTitle());
            textLabel.getStyleClass().add("wizard-step-label");

            stepBox.getChildren().addAll(circle, textLabel);
            stepsIndicator.getChildren().add(stepBox);

            // 连接线
            if (i < steps.size() - 1) {
                Region line = new Region();
                line.getStyleClass().add("wizard-step-line");
                line.setMinWidth(60);
                line.setMaxHeight(2);
                HBox lineBox = new HBox(line);
                lineBox.setAlignment(Pos.CENTER);
                lineBox.setPadding(new Insets(0, 8, 16, 8));
                stepsIndicator.getChildren().add(lineBox);
            }
        }

        updateStepsIndicator();
    }

    private void updateStepsIndicator() {
        int nodeIndex = 0;
        for (int i = 0; i < steps.size(); i++) {
            Node stepNode = stepsIndicator.getChildren().get(nodeIndex);
            if (stepNode instanceof VBox) {
                VBox stepBox = (VBox) stepNode;
                StackPane circle = (StackPane) stepBox.getChildren().get(0);
                Label numLabel = (Label) circle.getChildren().get(0);
                Label textLabel = (Label) stepBox.getChildren().get(1);

                circle.getStyleClass().removeAll("wizard-step-circle-active", "wizard-step-circle-completed");
                numLabel.getStyleClass().removeAll("wizard-step-number-active");
                textLabel.getStyleClass().removeAll("wizard-step-label-active");

                if (i < currentStepIndex) {
                    circle.getStyleClass().add("wizard-step-circle-completed");
                    numLabel.setText("✓");
                } else if (i == currentStepIndex) {
                    circle.getStyleClass().add("wizard-step-circle-active");
                    numLabel.getStyleClass().add("wizard-step-number-active");
                    textLabel.getStyleClass().add("wizard-step-label-active");
                } else {
                    numLabel.setText(steps.get(i).getIcon() != null ? steps.get(i).getIcon() : String.valueOf(i + 1));
                }
            }

            nodeIndex++;
            // 更新连接线
            if (nodeIndex < stepsIndicator.getChildren().size()) {
                Node lineNode = stepsIndicator.getChildren().get(nodeIndex);
                if (lineNode instanceof HBox) {
                    Region line = (Region) ((HBox) lineNode).getChildren().get(0);
                    line.getStyleClass().remove("wizard-step-line-completed");
                    if (i < currentStepIndex) {
                        line.getStyleClass().add("wizard-step-line-completed");
                    }
                }
                nodeIndex++;
            }
        }
    }

    private void showStep(int index, boolean animate) {
        if (index < 0 || index >= steps.size()) return;

        WizardStep step = steps.get(index);
        Node newContent = step.getContent();

        // 更新标题
        subtitleLabel.setText(step.getSubtitle() != null ? step.getSubtitle() : "步骤 " + (index + 1) + " / " + steps.size());

        if (animate && !contentStack.getChildren().isEmpty()) {
            Node oldContent = contentStack.getChildren().get(0);
            contentStack.getChildren().add(newContent);
            
            if (index > currentStepIndex) {
                AnimationUtil.wizardNext(oldContent, newContent, () -> {
                    contentStack.getChildren().remove(oldContent);
                });
            } else {
                AnimationUtil.wizardPrev(oldContent, newContent, () -> {
                    contentStack.getChildren().remove(oldContent);
                });
            }
        } else {
            contentStack.getChildren().setAll(newContent);
            AnimationUtil.fadeIn(newContent);
        }

        currentStepIndex = index;
        updateStepsIndicator();
        updateButtons();

        if (step.onEnter != null) {
            step.onEnter.run();
        }
    }

    private void updateButtons() {
        boolean isFirst = currentStepIndex == 0;
        boolean isLast = currentStepIndex == steps.size() - 1;

        prevBtn.setDisable(isFirst);
        prevBtn.setVisible(!isFirst);
        prevBtn.setManaged(!isFirst);

        nextBtn.setVisible(!isLast);
        nextBtn.setManaged(!isLast);

        submitBtn.setVisible(isLast);
        submitBtn.setManaged(isLast);

        // 绑定验证状态
        WizardStep currentStep = steps.get(currentStepIndex);
        nextBtn.disableProperty().bind(currentStep.validProperty().not());
        submitBtn.disableProperty().bind(currentStep.validProperty().not());
    }

    private void goToNextStep() {
        if (currentStepIndex < steps.size() - 1) {
            WizardStep current = steps.get(currentStepIndex);
            if (current.isValid()) {
                showStep(currentStepIndex + 1, true);
            } else {
                AnimationUtil.shake(contentStack);
            }
        }
    }

    private void goToPreviousStep() {
        if (currentStepIndex > 0) {
            showStep(currentStepIndex - 1, true);
        }
    }

    private void handleSubmit() {
        WizardStep last = steps.get(steps.size() - 1);
        if (!last.isValid()) {
            AnimationUtil.shake(submitBtn);
            return;
        }

        if (onSubmit != null) {
            onSubmit.accept(dataModel);
        }
        close();
    }

    private void handleCancel() {
        if (onCancel != null) {
            onCancel.run();
        }
        close();
    }

    public void close() {
        AnimationUtil.dialogClose(stage, stage::close);
    }

    public T getDataModel() {
        return dataModel;
    }

    public Stage getStage() {
        return stage;
    }

    // ==================== 表单构建辅助方法 ====================

    /**
     * 创建表单区域
     */
    public static VBox createSection(String title, Node... children) {
        VBox section = new VBox(16);
        section.getStyleClass().add("wizard-section");

        if (title != null && !title.isEmpty()) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("wizard-section-title");
            section.getChildren().add(titleLabel);
        }

        section.getChildren().addAll(children);
        return section;
    }

    /**
     * 创建表单字段
     */
    public static VBox createField(String label, Node input) {
        return createField(label, input, null);
    }

    public static VBox createField(String label, Node input, String hint) {
        VBox field = new VBox(6);

        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("wizard-field-label");
        field.getChildren().add(labelNode);

        if (input instanceof TextField) {
            ((TextField) input).getStyleClass().add("text-field");
        } else if (input instanceof ComboBox) {
            ((ComboBox<?>) input).getStyleClass().add("combo-box");
        } else if (input instanceof TextArea) {
            ((TextArea) input).getStyleClass().add("text-area");
        }
        field.getChildren().add(input);

        if (hint != null && !hint.isEmpty()) {
            Label hintLabel = new Label(hint);
            hintLabel.getStyleClass().add("wizard-field-hint");
            field.getChildren().add(hintLabel);
        }

        return field;
    }

    /**
     * 创建必填字段
     */
    public static VBox createRequiredField(String label, TextField input, BooleanProperty valid) {
        VBox field = createField(label + " *", input, null);
        
        input.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean isValid = newVal != null && !newVal.trim().isEmpty();
            valid.set(isValid);
            
            input.getStyleClass().removeAll("text-field-error", "text-field-success");
            if (newVal != null && !newVal.isEmpty()) {
                input.getStyleClass().add(isValid ? "text-field-success" : "text-field-error");
            }
        });

        return field;
    }

    /**
     * 创建两列布局
     */
    public static HBox createTwoColumns(Node left, Node right) {
        HBox row = new HBox(20);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        
        if (left instanceof Region) ((Region) left).setMaxWidth(Double.MAX_VALUE);
        if (right instanceof Region) ((Region) right).setMaxWidth(Double.MAX_VALUE);
        
        row.getChildren().addAll(left, right);
        return row;
    }

    /**
     * 创建三列布局
     */
    public static HBox createThreeColumns(Node col1, Node col2, Node col3) {
        HBox row = new HBox(16);
        HBox.setHgrow(col1, Priority.ALWAYS);
        HBox.setHgrow(col2, Priority.ALWAYS);
        HBox.setHgrow(col3, Priority.ALWAYS);
        
        if (col1 instanceof Region) ((Region) col1).setMaxWidth(Double.MAX_VALUE);
        if (col2 instanceof Region) ((Region) col2).setMaxWidth(Double.MAX_VALUE);
        if (col3 instanceof Region) ((Region) col3).setMaxWidth(Double.MAX_VALUE);
        
        row.getChildren().addAll(col1, col2, col3);
        return row;
    }

    /**
     * 创建风险等级选择器
     */
    public static HBox createRiskLevelSelector(StringProperty selectedLevel) {
        HBox container = new HBox(12);
        container.setAlignment(Pos.CENTER_LEFT);

        String[] levels = {"低风险", "中风险", "高风险", "极高风险"};
        String[] styles = {"low", "medium", "high", "extreme"};
        String[] colors = {"#00ff88", "#ffaa00", "#ff4444", "#ff0066"};

        ToggleGroup group = new ToggleGroup();

        for (int i = 0; i < levels.length; i++) {
            final int index = i;
            ToggleButton btn = new ToggleButton(levels[i]);
            btn.setToggleGroup(group);
            btn.getStyleClass().addAll("risk-card", "risk-card-" + styles[i]);
            btn.setMinWidth(90);

            btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                btn.getStyleClass().remove("selected");
                if (isSelected) {
                    btn.getStyleClass().add("selected");
                    selectedLevel.set(levels[index]);
                }
            });

            if (selectedLevel.get() != null && selectedLevel.get().equals(levels[i])) {
                btn.setSelected(true);
            }

            container.getChildren().add(btn);
        }

        return container;
    }

    /**
     * 创建照片上传区域
     */
    public static VBox createPhotoUploader(List<String> photoPaths, Runnable onPhotosChanged) {
        VBox container = new VBox(12);
        container.getStyleClass().add("photo-gallery");

        FlowPane photosPane = new FlowPane(12, 12);
        photosPane.setPrefWrapLength(500);

        // 添加已有照片
        for (String path : photoPaths) {
            photosPane.getChildren().add(createPhotoThumbnail(path, () -> {
                photoPaths.remove(path);
                if (onPhotosChanged != null) onPhotosChanged.run();
            }));
        }

        // 添加上传按钮
        Button addBtn = new Button("+ 添加照片");
        addBtn.getStyleClass().addAll("btn", "btn-secondary");
        addBtn.setMinSize(100, 100);
        addBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("选择照片");
            fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp")
            );
            List<File> files = fc.showOpenMultipleDialog(container.getScene().getWindow());
            if (files != null) {
                for (File file : files) {
                    photoPaths.add(file.getAbsolutePath());
                }
                if (onPhotosChanged != null) onPhotosChanged.run();
            }
        });

        photosPane.getChildren().add(addBtn);
        container.getChildren().add(photosPane);

        return container;
    }

    private static StackPane createPhotoThumbnail(String path, Runnable onRemove) {
        StackPane thumb = new StackPane();
        thumb.getStyleClass().add("photo-thumbnail");
        thumb.setMinSize(100, 100);
        thumb.setMaxSize(100, 100);

        try {
            Image img = new Image("file:" + path, 100, 100, true, true);
            ImageView iv = new ImageView(img);
            iv.setFitWidth(94);
            iv.setFitHeight(94);
            thumb.getChildren().add(iv);
        } catch (Exception e) {
            Label placeholder = new Label("📷");
            placeholder.setStyle("-fx-font-size: 24px; -fx-text-fill: #64748b;");
            thumb.getChildren().add(placeholder);
        }

        // 删除按钮
        Button removeBtn = new Button("×");
        removeBtn.getStyleClass().addAll("btn", "btn-danger", "btn-sm");
        removeBtn.setStyle("-fx-background-radius: 50; -fx-min-width: 24; -fx-min-height: 24; -fx-max-width: 24; -fx-max-height: 24; -fx-padding: 0;");
        removeBtn.setVisible(false);
        StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(removeBtn, new Insets(4));

        removeBtn.setOnAction(e -> onRemove.run());

        thumb.setOnMouseEntered(e -> removeBtn.setVisible(true));
        thumb.setOnMouseExited(e -> removeBtn.setVisible(false));

        thumb.getChildren().add(removeBtn);
        
        return thumb;
    }
}

