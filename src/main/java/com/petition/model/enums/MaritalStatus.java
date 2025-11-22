package com.petition.model.enums;

/**
 * 婚姻状态枚举类
 * 定义上访人员的婚姻状态选项
 *
 * @author 济南市公安局历下分局刑侦大队
 * @version 1.0.0
 */
public enum MaritalStatus {
    /**
     * 未婚
     */
    UNMARRIED("未婚"),

    /**
     * 已婚
     */
    MARRIED("已婚"),

    /**
     * 离异
     */
    DIVORCED("离异"),

    /**
     * 丧偶
     */
    WIDOWED("丧偶");

    /**
     * 显示名称（中文）
     */
    private final String displayName;

    /**
     * 构造函数
     *
     * @param displayName 显示名称
     */
    MaritalStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取显示名称
     *
     * @return 婚姻状态的中文名称
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据显示名称获取枚举值
     *
     * @param displayName 显示名称（如："未婚"、"已婚"等）
     * @return 对应的枚举值，未找到返回null
     */
    public static MaritalStatus fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (MaritalStatus status : values()) {
            if (status.displayName.equals(displayName)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
