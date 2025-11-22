package com.petition.model.enums;

/**
 * 性别枚举类
 * 定义上访人员的性别选项
 *
 * @author 济南市公安局历下分局刑侦大队
 * @version 1.0.0
 */
public enum Gender {
    /**
     * 男性
     */
    MALE("男"),

    /**
     * 女性
     */
    FEMALE("女");

    /**
     * 显示名称（中文）
     */
    private final String displayName;

    /**
     * 构造函数
     *
     * @param displayName 显示名称
     */
    Gender(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取显示名称
     *
     * @return 性别的中文名称
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据显示名称获取枚举值
     *
     * @param displayName 显示名称（如："男"、"女"）
     * @return 对应的枚举值，未找到返回null
     */
    public static Gender fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (Gender gender : values()) {
            if (gender.displayName.equals(displayName)) {
                return gender;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
