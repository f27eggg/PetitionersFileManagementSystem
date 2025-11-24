package com.petition.view.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

/**
 * 搜索框组件
 * 功能：带搜索按钮的输入框
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class SearchBox extends HBox {

    private final TextField textField;
    private final Button searchButton;
    private final Button clearButton;

    private final StringProperty promptText = new SimpleStringProperty("请输入搜索关键词...");
    private final ObjectProperty<Consumer<String>> onSearch = new SimpleObjectProperty<>();

    /**
     * 构造函数
     */
    public SearchBox() {
        // 初始化组件
        textField = new TextField();
        searchButton = new Button("🔍 搜索");
        clearButton = new Button("✕");

        // 设置样式
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(10);
        this.getStyleClass().add("search-box");

        textField.setPromptText(promptText.get());
        textField.getStyleClass().add("search-field");
        HBox.setHgrow(textField, Priority.ALWAYS);

        searchButton.getStyleClass().add("primary-button");
        clearButton.getStyleClass().add("secondary-button");

        // 绑定事件
        searchButton.setOnAction(e -> handleSearch());
        clearButton.setOnAction(e -> handleClear());
        textField.setOnAction(e -> handleSearch());

        // 添加到容器
        this.getChildren().addAll(textField, searchButton, clearButton);

        // 绑定提示文本
        textField.promptTextProperty().bind(promptText);
    }

    /**
     * 处理搜索
     */
    private void handleSearch() {
        String text = textField.getText();
        if (onSearch.get() != null) {
            onSearch.get().accept(text);
        }
    }

    /**
     * 处理清空
     */
    private void handleClear() {
        textField.clear();
        if (onSearch.get() != null) {
            onSearch.get().accept("");
        }
    }

    /**
     * 获取搜索文本
     *
     * @return 搜索文本
     */
    public String getText() {
        return textField.getText();
    }

    /**
     * 设置搜索文本
     *
     * @param text 搜索文本
     */
    public void setText(String text) {
        textField.setText(text);
    }

    /**
     * 清空搜索文本
     */
    public void clear() {
        textField.clear();
    }

    /**
     * 获取提示文本属性
     *
     * @return 提示文本属性
     */
    public StringProperty promptTextProperty() {
        return promptText;
    }

    /**
     * 设置提示文本
     *
     * @param text 提示文本
     */
    public void setPromptText(String text) {
        promptText.set(text);
    }

    /**
     * 获取提示文本
     *
     * @return 提示文本
     */
    public String getPromptText() {
        return promptText.get();
    }

    /**
     * 设置搜索事件处理器
     *
     * @param handler 搜索处理器
     */
    public void setOnSearch(Consumer<String> handler) {
        onSearch.set(handler);
    }

    /**
     * 获取搜索事件处理器属性
     *
     * @return 搜索处理器属性
     */
    public ObjectProperty<Consumer<String>> onSearchProperty() {
        return onSearch;
    }
}
