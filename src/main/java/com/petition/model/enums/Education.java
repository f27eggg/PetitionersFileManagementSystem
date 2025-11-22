package com.petition.model.enums;

/**
 * 学历枚举类
 * 定义上访人员的学历选项
 *
 * @author 济南市公安局历下分局刑侦大队
 * @version 1.0.0
 */
public enum Education {
    /**
     * 小学
     */
    PRIMARY_SCHOOL("小学"),

    /**
     * 初中
     */
    JUNIOR_HIGH("初中"),

    /**
     * 高中
     */
    SENIOR_HIGH("高中"),

    /**
     * 中专
     */
    TECHNICAL_SECONDARY("中专"),

    /**
     * 大专
     */
    JUNIOR_COLLEGE("大专"),

    /**
     * 本科
     */
    BACHELOR("本科"),

    /**
     * 硕士
     */
    MASTER("硕士"),

    /**
     * 博士
     */
    DOCTOR("博士"),

    /**
     * 其他
     */
    OTHER("其他");

    /**
     * 显示名称（中文）
     */
    private final String displayName;

    /**
     * 构造函数
     *
     * @param displayName 显示名称
     */
    Education(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取显示名称
     *
     * @return 学历的中文名称
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据显示名称获取枚举值
     *
     * @param displayName 显示名称（如："本科"、"硕士"等）
     * @return 对应的枚举值，未找到返回null
     */
    public static Education fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (Education education : values()) {
            if (education.displayName.equals(displayName)) {
                return education;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
