package com.petition.view.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * 数据卡片组件
 * 功能：显示统计数据的卡片
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class DataCard extends VBox {

    private final Label titleLabel;
    private final Label valueLabel;
    private final Label descLabel;

    private final StringProperty title = new SimpleStringProperty("标题");
    private final StringProperty value = new SimpleStringProperty("0");
    private final StringProperty description = new SimpleStringProperty("");

    /**
     * 构造函数
     */
    public DataCard() {
        // 初始化标签
        titleLabel = new Label();
        valueLabel = new Label();
        descLabel = new Label();

        // 设置样式
        this.getStyleClass().add("stat-card");
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(10);
        this.setPadding(new Insets(20));

        titleLabel.getStyleClass().add("stat-card-title");
        valueLabel.getStyleClass().add("stat-card-value");
        descLabel.getStyleClass().add("stat-desc");

        // 绑定属性
        titleLabel.textProperty().bind(title);
        valueLabel.textProperty().bind(value);
        descLabel.textProperty().bind(description);

        // 添加到容器
        this.getChildren().addAll(titleLabel, valueLabel);
    }

    /**
     * 构造函数（带标题和值）
     *
     * @param title 标题
     * @param value 值
     */
    public DataCard(String title, String value) {
        this();
        setTitle(title);
        setValue(value);
    }

    /**
     * 构造函数（带标题、值和描述）
     *
     * @param title 标题
     * @param value 值
     * @param description 描述
     */
    public DataCard(String title, String value, String description) {
        this();
        setTitle(title);
        setValue(value);
        setDescription(description);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title.set(title);
    }

    /**
     * 获取标题
     *
     * @return 标题
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * 获取标题属性
     *
     * @return 标题属性
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * 设置值
     *
     * @param value 值
     */
    public void setValue(String value) {
        this.value.set(value);
    }

    /**
     * 设置数值
     *
     * @param value 数值
     */
    public void setValue(int value) {
        this.value.set(String.valueOf(value));
    }

    /**
     * 获取值
     *
     * @return 值
     */
    public String getValue() {
        return value.get();
    }

    /**
     * 获取值属性
     *
     * @return 值属性
     */
    public StringProperty valueProperty() {
        return value;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description.set(description);
        if (!this.getChildren().contains(descLabel)) {
            this.getChildren().add(descLabel);
        }
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * 获取描述属性
     *
     * @return 描述属性
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * 设置样式类型
     *
     * @param type 类型（danger, warning, success）
     */
    public void setType(String type) {
        this.getStyleClass().clear();
        this.getStyleClass().add("stat-card");
        switch (type.toLowerCase()) {
            case "danger":
                this.getStyleClass().add("stat-card-danger");
                break;
            case "warning":
                this.getStyleClass().add("stat-card-warning");
                break;
            case "success":
                this.getStyleClass().add("stat-card-success");
                break;
        }
    }
}
