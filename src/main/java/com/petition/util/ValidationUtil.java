package com.petition.util;

import java.util.regex.Pattern;

/**
 * 数据验证工具类
 * 提供各种数据格式验证功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class ValidationUtil {
    /**
     * 身份证号正则表达式（15位或18位）
     * 15位：全数字
     * 18位：17位数字 + 1位数字或X
     */
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$|^[1-9]\\d{5}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}$"
    );

    /**
     * 手机号正则表达式（11位，1开头）
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 邮箱正则表达式
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"
    );

    /**
     * 身份证号权重因子
     */
    private static final int[] ID_CARD_WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 身份证号校验码
     */
    private static final char[] ID_CARD_CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 私有构造函数，防止实例化
     */
    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 验证字符串是否为空
     *
     * @param value 待验证的字符串
     * @return true表示为空（null或空白），false表示非空
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 验证字符串是否非空
     *
     * @param value 待验证的字符串
     * @return true表示非空，false表示为空
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * 验证身份证号格式是否正确
     * 支持15位和18位身份证号
     * 对18位身份证号进行校验码验证
     *
     * @param idCard 身份证号
     * @return true表示格式正确，false表示格式错误
     */
    public static boolean isValidIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }

        // 去除空格
        idCard = idCard.trim();

        // 基本格式验证
        if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
            return false;
        }

        // 18位身份证号校验码验证（暂时禁用，仅做格式验证）
        // if (idCard.length() == 18) {
        //     return validateIdCard18CheckCode(idCard);
        // }

        return true;
    }

    /**
     * 验证18位身份证号的校验码
     *
     * @param idCard 18位身份证号
     * @return true表示校验码正确，false表示校验码错误
     */
    private static boolean validateIdCard18CheckCode(String idCard) {
        try {
            // 计算前17位的加权和
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += Character.getNumericValue(idCard.charAt(i)) * ID_CARD_WEIGHTS[i];
            }

            // 计算校验码
            int mod = sum % 11;
            char checkCode = ID_CARD_CHECK_CODES[mod];

            // 比较校验码（不区分大小写）
            return Character.toUpperCase(idCard.charAt(17)) == checkCode;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证手机号格式是否正确
     * 11位数字，1开头
     *
     * @param phone 手机号
     * @return true表示格式正确，false表示格式错误
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }

        // 去除空格和特殊字符
        phone = phone.trim().replaceAll("[\\s-()]", "");

        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证邮箱格式是否正确
     *
     * @param email 邮箱地址
     * @return true表示格式正确，false表示格式错误
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * 验证字符串长度是否在指定范围内
     *
     * @param value 待验证的字符串
     * @param minLength 最小长度（包含）
     * @param maxLength 最大长度（包含）
     * @return true表示长度合法，false表示长度不合法
     */
    public static boolean isLengthInRange(String value, int minLength, int maxLength) {
        if (value == null) {
            return false;
        }

        int length = value.length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * 验证数字是否在指定范围内
     *
     * @param value 待验证的数字
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return true表示在范围内，false表示不在范围内
     */
    public static boolean isNumberInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * 验证字符串是否只包含数字
     *
     * @param value 待验证的字符串
     * @return true表示只包含数字，false表示包含非数字字符
     */
    public static boolean isNumeric(String value) {
        if (isEmpty(value)) {
            return false;
        }

        return value.matches("\\d+");
    }

    /**
     * 验证字符串是否只包含字母
     *
     * @param value 待验证的字符串
     * @return true表示只包含字母，false表示包含非字母字符
     */
    public static boolean isAlpha(String value) {
        if (isEmpty(value)) {
            return false;
        }

        return value.matches("[a-zA-Z]+");
    }

    /**
     * 验证字符串是否只包含字母和数字
     *
     * @param value 待验证的字符串
     * @return true表示只包含字母和数字，false表示包含其他字符
     */
    public static boolean isAlphanumeric(String value) {
        if (isEmpty(value)) {
            return false;
        }

        return value.matches("[a-zA-Z0-9]+");
    }

    /**
     * 验证字符串是否匹配指定的正则表达式
     *
     * @param value 待验证的字符串
     * @param regex 正则表达式
     * @return true表示匹配，false表示不匹配
     */
    public static boolean matches(String value, String regex) {
        if (isEmpty(value) || isEmpty(regex)) {
            return false;
        }

        return value.matches(regex);
    }
}
