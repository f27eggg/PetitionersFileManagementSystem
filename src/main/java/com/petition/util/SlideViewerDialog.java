package com.petition.util;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 下滑式详情查看弹窗
 * 提供流畅的下滑浏览体验，适合查看详细信息
 */
public class SlideViewerDialog {

    private Stage stage;
    private BorderPane root;
    private ScrollPane scrollPane;
    private VBox contentBox;
    private HBox headerBar;
    private Label titleLabel;

    private List<ViewerSection> sections = new ArrayList<>();
    private Runnable onClose;
    private Consumer<Void> onEdit;

    private static final String CSS_PATH = "/css/main.css";

    /**
     * 查看区块定义
     */
    public static class ViewerSection {
        private String title;
        private String icon;
        private Node content;
        private boolean expanded = true;

        public ViewerSection(String title) {
            this.title = title;
        }

        public ViewerSection icon(String icon) {
            this.icon = icon;
            return this;
        }

        public ViewerSection content(Node content) {
            this.content = content;
            return this;
        }

        public ViewerSection expanded(boolean expanded) {
            this.expanded = expanded;
            return this;
        }

        public String getTitle() { return title; }
        public String getIcon() { return icon; }
        public Node getContent() { return content; }
        public boolean isExpanded() { return expanded; }
    }

    public SlideViewerDialog(Window owner, String title) {
        createStage(owner, title);
    }

    private void createStage(Window owner, String title) {
        stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle(title);
        stage.setMinWidth(700);
        stage.setMinHeight(500);

        root = new BorderPane();
        root.getStyleClass().add("slide-viewer");

        // 顶部悬浮工具栏
        headerBar = createHeaderBar(title);
        root.setTop(headerBar);

        // 滚动内容区
        contentBox = new VBox(24);
        contentBox.getStyleClass().add("slide-viewer-content");
        contentBox.setPadding(new Insets(0, 0, 40, 0));

        scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("bg-transparent");
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 800);
        scene.setFill(Color.web("#0a0e1a"));

        try {
            scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
        } catch (Exception e) {
            System.err.println("无法加载CSS: " + e.getMessage());
        }

        // 键盘事件
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });

        // 滚动视差效果
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            double opacity = Math.min(1.0, newVal.doubleValue() * 3);
            headerBar.setStyle("-fx-background-color: rgba(10, 14, 26, " + (0.7 + opacity * 0.25) + ");");
        });

        stage.setScene(scene);
        stage.setOnShown(e -> playOpenAnimation());
    }

    private HBox createHeaderBar(String title) {
        HBox header = new HBox(16);
        header.getStyleClass().add("slide-viewer-header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setStyle("-fx-background-color: rgba(10, 14, 26, 0.7);");

        // 关闭按钮
        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().addAll("slide-viewer-close");
        closeBtn.setOnAction(e -> close());

        // 标题
        titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("text-xl", "font-bold", "text-primary");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 编辑按钮
        Button editBtn = new Button("✎ 编辑");
        editBtn.getStyleClass().addAll("btn", "btn-primary");
        editBtn.setOnAction(e -> {
            if (onEdit != null) {
                onEdit.accept(null);
            }
        });

        header.getChildren().addAll(closeBtn, titleLabel, spacer, editBtn);

        return header;
    }

    public SlideViewerDialog addSection(ViewerSection section) {
        sections.add(section);
        return this;
    }

    public SlideViewerDialog onClose(Runnable onClose) {
        this.onClose = onClose;
        return this;
    }

    public SlideViewerDialog onEdit(Consumer<Void> onEdit) {
        this.onEdit = onEdit;
        return this;
    }

    public void show() {
        buildContent();
        stage.show();
    }

    private void buildContent() {
        contentBox.getChildren().clear();

        for (int i = 0; i < sections.size(); i++) {
            ViewerSection section = sections.get(i);
            Node sectionNode = createSectionNode(section);
            contentBox.getChildren().add(sectionNode);
        }
    }

    private Node createSectionNode(ViewerSection section) {
        VBox sectionBox = new VBox(16);
        sectionBox.getStyleClass().add("slide-viewer-section");

        // 区块标题
        HBox titleBar = new HBox(12);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.getStyleClass().add("slide-viewer-section-title");

        if (section.getIcon() != null) {
            Label iconLabel = new Label(section.getIcon());
            iconLabel.setStyle("-fx-font-size: 18px;");
            titleBar.getChildren().add(iconLabel);
        }

        Label titleLabel = new Label(section.getTitle());
        titleLabel.getStyleClass().addAll("font-bold");
        titleBar.getChildren().add(titleLabel);

        sectionBox.getChildren().add(titleBar);

        if (section.getContent() != null) {
            sectionBox.getChildren().add(section.getContent());
        }

        return sectionBox;
    }

    private void playOpenAnimation() {
        // 内容从底部滑入
        contentBox.setTranslateY(50);
        contentBox.setOpacity(0);

        ParallelTransition pt = new ParallelTransition();

        TranslateTransition tt = new TranslateTransition(Duration.millis(400), contentBox);
        tt.setFromY(50);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(Duration.millis(400), contentBox);
        ft.setFromValue(0);
        ft.setToValue(1);

        pt.getChildren().addAll(tt, ft);
        pt.setDelay(Duration.millis(100));
        pt.play();

        // 各区块交错出现
        int delay = 200;
        for (Node section : contentBox.getChildren()) {
            section.setOpacity(0);
            section.setTranslateY(20);

            ParallelTransition sectionPt = new ParallelTransition();

            TranslateTransition stt = new TranslateTransition(Duration.millis(300), section);
            stt.setFromY(20);
            stt.setToY(0);
            stt.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition sft = new FadeTransition(Duration.millis(300), section);
            sft.setFromValue(0);
            sft.setToValue(1);

            sectionPt.getChildren().addAll(stt, sft);
            sectionPt.setDelay(Duration.millis(delay));
            sectionPt.play();

            delay += 80;
        }
    }

    public void close() {
        // 关闭动画
        ParallelTransition pt = new ParallelTransition();

        TranslateTransition tt = new TranslateTransition(Duration.millis(200), contentBox);
        tt.setToY(30);
        tt.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(Duration.millis(200), root);
        ft.setToValue(0);

        pt.getChildren().addAll(tt, ft);
        pt.setOnFinished(e -> {
            if (onClose != null) onClose.run();
            stage.close();
        });
        pt.play();
    }

    public Stage getStage() {
        return stage;
    }

    // ==================== 内容构建辅助方法 ====================

    /**
     * 创建字段显示行
     */
    public static HBox createFieldRow(String label, String value) {
        HBox row = new HBox(16);
        row.getStyleClass().add("slide-viewer-field");
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label);
        labelNode.getStyleClass().addAll("slide-viewer-field-label");
        labelNode.setMinWidth(100);

        Label valueNode = new Label(value != null ? value : "-");
        valueNode.getStyleClass().addAll("slide-viewer-field-value");
        valueNode.setWrapText(true);
        HBox.setHgrow(valueNode, Priority.ALWAYS);

        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }

    /**
     * 创建两列字段显示
     */
    public static HBox createTwoColumnFields(String label1, String value1, String label2, String value2) {
        HBox row = new HBox(40);

        VBox col1 = new VBox(4);
        Label l1 = new Label(label1);
        l1.getStyleClass().add("slide-viewer-field-label");
        Label v1 = new Label(value1 != null ? value1 : "-");
        v1.getStyleClass().add("slide-viewer-field-value");
        col1.getChildren().addAll(l1, v1);
        HBox.setHgrow(col1, Priority.ALWAYS);

        VBox col2 = new VBox(4);
        Label l2 = new Label(label2);
        l2.getStyleClass().add("slide-viewer-field-label");
        Label v2 = new Label(value2 != null ? value2 : "-");
        v2.getStyleClass().add("slide-viewer-field-value");
        col2.getChildren().addAll(l2, v2);
        HBox.setHgrow(col2, Priority.ALWAYS);

        row.getChildren().addAll(col1, col2);
        return row;
    }

    /**
     * 创建风险等级徽章
     */
    public static Label createRiskBadge(String level) {
        Label badge = new Label(level);
        badge.getStyleClass().add("risk-badge");

        String styleClass;
        switch (level) {
            case "极高风险": styleClass = "risk-badge-extreme"; break;
            case "高风险": styleClass = "risk-badge-high"; break;
            case "中风险": styleClass = "risk-badge-medium"; break;
            default: styleClass = "risk-badge-low"; break;
        }
        badge.getStyleClass().add(styleClass);

        return badge;
    }

    /**
     * 创建照片画廊组件
     */
    public static VBox createPhotoGallery(List<String> photoPaths) {
        VBox gallery = new VBox(16);
        gallery.getStyleClass().add("photo-gallery");

        if (photoPaths == null || photoPaths.isEmpty()) {
            Label noPhoto = new Label("暂无照片");
            noPhoto.getStyleClass().addAll("text-muted");
            noPhoto.setStyle("-fx-padding: 40; -fx-alignment: center;");
            gallery.getChildren().add(noPhoto);
            return gallery;
        }

        // 主图显示区
        StackPane mainImagePane = new StackPane();
        mainImagePane.getStyleClass().add("photo-gallery-main");
        mainImagePane.setMinHeight(350);

        ImageView mainImage = new ImageView();
        mainImage.getStyleClass().add("photo-gallery-main-image");
        mainImage.setFitWidth(500);
        mainImage.setFitHeight(350);
        mainImage.setPreserveRatio(true);

        // 左右导航按钮
        Button prevBtn = new Button("◀");
        prevBtn.getStyleClass().add("photo-nav-button");
        StackPane.setAlignment(prevBtn, Pos.CENTER_LEFT);
        StackPane.setMargin(prevBtn, new Insets(0, 0, 0, 16));

        Button nextBtn = new Button("▶");
        nextBtn.getStyleClass().add("photo-nav-button");
        StackPane.setAlignment(nextBtn, Pos.CENTER_RIGHT);
        StackPane.setMargin(nextBtn, new Insets(0, 16, 0, 0));

        // 计数器
        Label counter = new Label("1 / " + photoPaths.size());
        counter.getStyleClass().add("photo-counter");
        StackPane.setAlignment(counter, Pos.BOTTOM_CENTER);
        StackPane.setMargin(counter, new Insets(0, 0, 16, 0));

        mainImagePane.getChildren().addAll(mainImage, prevBtn, nextBtn, counter);

        // 缩略图区域
        HBox thumbnails = new HBox(12);
        thumbnails.getStyleClass().add("photo-gallery-thumbnails");
        thumbnails.setAlignment(Pos.CENTER);

        IntegerProperty currentIndex = new SimpleIntegerProperty(0);

        // 加载图片
        Runnable loadCurrentImage = () -> {
            int idx = currentIndex.get();
            if (idx >= 0 && idx < photoPaths.size()) {
                try {
                    String path = photoPaths.get(idx);
                    Image img = new Image("file:" + path, 600, 400, true, true);
                    
                    // 切换动画
                    FadeTransition ft = new FadeTransition(Duration.millis(150), mainImage);
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.setOnFinished(e -> {
                        mainImage.setImage(img);
                        FadeTransition ft2 = new FadeTransition(Duration.millis(150), mainImage);
                        ft2.setFromValue(0);
                        ft2.setToValue(1);
                        ft2.play();
                    });
                    ft.play();
                } catch (Exception e) {
                    mainImage.setImage(null);
                }
                counter.setText((idx + 1) + " / " + photoPaths.size());

                // 更新缩略图选中状态
                for (int i = 0; i < thumbnails.getChildren().size(); i++) {
                    Node thumb = thumbnails.getChildren().get(i);
                    thumb.getStyleClass().remove("photo-thumbnail-active");
                    if (i == idx) {
                        thumb.getStyleClass().add("photo-thumbnail-active");
                    }
                }
            }
        };

        // 创建缩略图
        for (int i = 0; i < photoPaths.size(); i++) {
            final int index = i;
            StackPane thumb = new StackPane();
            thumb.getStyleClass().add("photo-thumbnail");
            thumb.setMinSize(70, 70);
            thumb.setMaxSize(70, 70);
            thumb.setCursor(javafx.scene.Cursor.HAND);

            try {
                Image img = new Image("file:" + photoPaths.get(i), 70, 70, true, true);
                ImageView iv = new ImageView(img);
                iv.setFitWidth(66);
                iv.setFitHeight(66);
                thumb.getChildren().add(iv);
            } catch (Exception e) {
                Label placeholder = new Label("📷");
                placeholder.setStyle("-fx-font-size: 20px; -fx-text-fill: #64748b;");
                thumb.getChildren().add(placeholder);
            }

            thumb.setOnMouseClicked(e -> {
                currentIndex.set(index);
                loadCurrentImage.run();
            });

            thumbnails.getChildren().add(thumb);
        }

        // 导航按钮事件
        prevBtn.setOnAction(e -> {
            if (currentIndex.get() > 0) {
                currentIndex.set(currentIndex.get() - 1);
                loadCurrentImage.run();
            }
        });

        nextBtn.setOnAction(e -> {
            if (currentIndex.get() < photoPaths.size() - 1) {
                currentIndex.set(currentIndex.get() + 1);
                loadCurrentImage.run();
            }
        });

        // 初始加载第一张
        loadCurrentImage.run();

        gallery.getChildren().addAll(mainImagePane, thumbnails);

        return gallery;
    }

    /**
     * 创建信息网格
     */
    public static GridPane createInfoGrid(Map<String, String> data) {
        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(16);

        int row = 0;
        int col = 0;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            VBox field = new VBox(4);
            
            Label label = new Label(entry.getKey());
            label.getStyleClass().add("slide-viewer-field-label");
            
            Label value = new Label(entry.getValue() != null ? entry.getValue() : "-");
            value.getStyleClass().add("slide-viewer-field-value");
            
            field.getChildren().addAll(label, value);
            grid.add(field, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        return grid;
    }
}

