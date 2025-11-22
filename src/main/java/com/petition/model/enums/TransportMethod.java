package com.petition.model.enums;

/**
 * 在京通行方式枚举类
 * 定义上访人员在北京的日常交通方式选项
 *
 * @author 刘一村
 * @version 1.0.0
 */
public enum TransportMethod {
    /**
     * 地铁
     */
    SUBWAY("地铁"),

    /**
     * 公交
     */
    BUS("公交"),

    /**
     * 出租车
     */
    TAXI("出租车"),

    /**
     * 网约车
     */
    RIDE_HAILING("网约车"),

    /**
     * 步行
     */
    WALKING("步行"),

    /**
     * 自驾
     */
    SELF_DRIVING("自驾"),

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
    TransportMethod(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取显示名称
     *
     * @return 通行方式的中文名称
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据显示名称获取枚举值
     *
     * @param displayName 显示名称（如："地铁"、"公交"等）
     * @return 对应的枚举值，未找到返回null
     */
    public static TransportMethod fromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        for (TransportMethod method : values()) {
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
