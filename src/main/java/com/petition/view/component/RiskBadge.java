package com.petition.view.component;

import com.petition.model.enums.RiskLevel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * 危险等级标签组件
 * 功能：显示危险等级的彩色徽章
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class RiskBadge extends HBox {

    private final Label label;
    private RiskLevel riskLevel;

    /**
     * 构造函数
     */
    public RiskBadge() {
        this.label = new Label();
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(label);

        // 设置默认样式
        label.getStyleClass().add("risk-badge");
    }

    /**
     * 构造函数（带初始风险等级）
     *
     * @param riskLevel 风险等级
     */
    public RiskBadge(RiskLevel riskLevel) {
        this();
        setRiskLevel(riskLevel);
    }

    /**
     * 设置风险等级
     *
     * @param riskLevel 风险等级
     */
    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
        updateDisplay();
    }

    /**
     * 获取风险等级
     *
     * @return 风险等级
     */
    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    /**
     * 更新显示
     */
    private void updateDisplay() {
        if (riskLevel == null) {
            label.setText("未评估");
            label.getStyleClass().clear();
            label.getStyleClass().add("risk-badge");
            return;
        }

        // 设置文本
        label.setText(riskLevel.getDisplayName());

        // 清除旧样式
        label.getStyleClass().clear();

        // 根据风险等级设置样式
        switch (riskLevel) {
            case LOW:
                label.getStyleClass().add("risk-badge-low");
                break;
            case MEDIUM:
                label.getStyleClass().add("risk-badge-medium");
                break;
            case HIGH:
                label.getStyleClass().add("risk-badge-high");
                break;
            case CRITICAL:
                label.getStyleClass().add("risk-badge-extreme");
                break;
            default:
                label.getStyleClass().add("risk-badge");
                break;
        }
    }

    /**
     * 设置大尺寸样式
     */
    public void setLarge() {
        label.getStyleClass().clear();
        label.getStyleClass().add("risk-badge-large");
    }
}
