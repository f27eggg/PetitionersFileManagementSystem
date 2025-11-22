package com.petition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petition.model.enums.RiskLevel;

/**
 * 评估结果实体类
 * 对上访人员的综合风险评估结果
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class RiskAssessment {
    /**
     * 危险等级
     * 评估上访人员的风险级别
     */
    private RiskLevel riskLevel;

    /**
     * 默认构造函数
     */
    public RiskAssessment() {
    }

    /**
     * 带危险等级的构造函数
     *
     * @param riskLevel 危险等级
     */
    public RiskAssessment(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    // ========== Getters and Setters ==========

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    /**
     * 判断是否为高危或极高危人员
     *
     * @return true表示高危或极高危
     */
    @JsonIgnore
    public boolean isHighRisk() {
        return riskLevel == RiskLevel.HIGH || riskLevel == RiskLevel.CRITICAL;
    }

    /**
     * 获取等级顺序值（用于排序）
     *
     * @return 等级值，数字越大危险等级越高
     */
    @JsonIgnore
    public int getLevelValue() {
        return riskLevel != null ? riskLevel.getLevelValue() : -1;
    }

    @Override
    public String toString() {
        return "RiskAssessment{" +
                "riskLevel=" + riskLevel +
                '}';
    }
}
