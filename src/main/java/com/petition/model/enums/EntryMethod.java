package com.petition.model.enums;

/**
 * 进京方式枚举类
 * 定义上访人员进京的交通方式选项
 *
 * @author 济南市公安局历下分局刑侦大队
 * @version 1.0.0
 */
public enum EntryMethod {
    /**
     * 自驾车
     */
    SELF_DRIVING("自驾车"),

    /**
     * 火车
     */
    TRAIN("火车"),

    /**
     * 高铁
     */
    HIGH_SPEED_RAIL("高铁"),

    /**
     * 飞机
     */
    AIRPLANE("飞机"),

    /**
     * 长途汽车
     */
    LONG_DISTANCE_BUS("长途汽车"),

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
    EntryMethod(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取显示名称
     *
     * @return 进京方式的中文名称
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据显示名称获取枚举值
     *
     * @param displayName 显示名称（如："自驾车"、"火车"等）
     * @return 对应的枚举值，未找到返回null
     */
    public static EntryMethod fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (EntryMethod method : values()) {
            if (method.displayName.equals(displayName)) {
                return method;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
