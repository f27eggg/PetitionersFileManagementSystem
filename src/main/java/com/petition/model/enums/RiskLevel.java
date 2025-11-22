package com.petition.model.enums;

/**
 * 危险等级枚举类
 * 定义上访人员的风险评估等级
 *
 * @author 刘一村
 * @version 1.0.0
 */
public enum RiskLevel {
    /**
     * 低危
     */
    LOW("低危"),

    /**
     * 中危
     */
    MEDIUM("中危"),

    /**
     * 高危
     */
    HIGH("高危"),

    /**
     * 极高危
     */
    CRITICAL("极高危");

    /**
     * 显示名称（中文）
     */
    private final String displayName;

    /**
     * 构造函数
     *
     * @param displayName 显示名称
     */
    RiskLevel(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取显示名称
     *
     * @return 危险等级的中文名称
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据显示名称获取枚举值
     *
     * @param displayName 显示名称（如："低危"、"高危"等）
     * @return 对应的枚举值，未找到返回null
     */
    public static RiskLevel fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (RiskLevel level : values()) {
            if (level.displayName.equals(displayName)) {
                return level;
            }
        }
        return null;
    }

    /**
     * 获取等级顺序值（用于排序和比较）
     *
     * @return 等级值，数字越大危险等级越高
     */
    public int getLevelValue() {
        return ordinal();
    }

    @Override
    public String toString() {
        return displayName;
    }
}
