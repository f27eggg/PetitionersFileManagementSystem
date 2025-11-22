package com.petition.util;

import com.petition.model.enums.Gender;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * 身份证号工具类
 * 提供身份证号信息提取和处理功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class IdCardUtil {
    /**
     * 私有构造函数，防止实例化
     */
    private IdCardUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 从身份证号提取性别
     * 15位身份证：第15位奇数为男，偶数为女
     * 18位身份证：第17位奇数为男，偶数为女
     *
     * @param idCard 身份证号
     * @return 性别枚举值，null表示无法提取
     */
    public static Gender extractGender(String idCard) {
        if (!ValidationUtil.isValidIdCard(idCard)) {
            return null;
        }

        try {
            int genderCode;
            if (idCard.length() == 15) {
                genderCode = Character.getNumericValue(idCard.charAt(14));
            } else {
                genderCode = Character.getNumericValue(idCard.charAt(16));
            }

            return (genderCode % 2 == 0) ? Gender.FEMALE : Gender.MALE;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从身份证号提取出生日期
     * 15位身份证：7-12位为YYMMDD
     * 18位身份证：7-14位为YYYYMMDD
     *
     * @param idCard 身份证号
     * @return 出生日期，null表示无法提取
     */
    public static LocalDate extractBirthDate(String idCard) {
        if (!ValidationUtil.isValidIdCard(idCard)) {
            return null;
        }

        try {
            String birthStr;
            DateTimeFormatter formatter;

            if (idCard.length() == 15) {
                // 15位身份证：YYMMDD
                birthStr = idCard.substring(6, 12);
                // 假设19XX年或20XX年
                int year = Integer.parseInt(birthStr.substring(0, 2));
                year = (year > 30) ? 1900 + year : 2000 + year;
                birthStr = year + birthStr.substring(2);
                formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            } else {
                // 18位身份证：YYYYMMDD
                birthStr = idCard.substring(6, 14);
                formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            }

            return LocalDate.parse(birthStr, formatter);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从身份证号提取年龄
     *
     * @param idCard 身份证号
     * @return 年龄，-1表示无法提取
     */
    public static int extractAge(String idCard) {
        LocalDate birthDate = extractBirthDate(idCard);
        if (birthDate == null) {
            return -1;
        }

        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * 从身份证号提取省份代码
     * 前两位为省份代码
     *
     * @param idCard 身份证号
     * @return 省份代码，null表示无法提取
     */
    public static String extractProvinceCode(String idCard) {
        if (!ValidationUtil.isValidIdCard(idCard)) {
            return null;
        }

        return idCard.substring(0, 2);
    }

    /**
     * 从身份证号提取省份名称
     *
     * @param idCard 身份证号
     * @return 省份名称，null表示无法识别
     */
    public static String extractProvinceName(String idCard) {
        String code = extractProvinceCode(idCard);
        if (code == null) {
            return null;
        }

        return getProvinceName(code);
    }

    /**
     * 根据省份代码获取省份名称
     *
     * @param code 省份代码
     * @return 省份名称
     */
    private static String getProvinceName(String code) {
        switch (code) {
            case "11": return "北京";
            case "12": return "天津";
            case "13": return "河北";
            case "14": return "山西";
            case "15": return "内蒙古";
            case "21": return "辽宁";
            case "22": return "吉林";
            case "23": return "黑龙江";
            case "31": return "上海";
            case "32": return "江苏";
            case "33": return "浙江";
            case "34": return "安徽";
            case "35": return "福建";
            case "36": return "江西";
            case "37": return "山东";
            case "41": return "河南";
            case "42": return "湖北";
            case "43": return "湖南";
            case "44": return "广东";
            case "45": return "广西";
            case "46": return "海南";
            case "50": return "重庆";
            case "51": return "四川";
            case "52": return "贵州";
            case "53": return "云南";
            case "54": return "西藏";
            case "61": return "陕西";
            case "62": return "甘肃";
            case "63": return "青海";
            case "64": return "宁夏";
            case "65": return "新疆";
            case "71": return "台湾";
            case "81": return "香港";
            case "82": return "澳门";
            default: return "未知";
        }
    }

    /**
     * 身份证号脱敏显示
     * 隐藏中间部分，只显示前6位和后4位
     * 例如：370102199001011234 -> 370102********1234
     *
     * @param idCard 身份证号
     * @return 脱敏后的身份证号，null表示输入无效
     */
    public static String maskIdCard(String idCard) {
        if (!ValidationUtil.isValidIdCard(idCard)) {
            return null;
        }

        int length = idCard.length();
        if (length == 15) {
            return idCard.substring(0, 6) + "*****" + idCard.substring(11);
        } else {
            return idCard.substring(0, 6) + "********" + idCard.substring(14);
        }
    }

    /**
     * 身份证号脱敏显示（自定义显示位数）
     *
     * @param idCard 身份证号
     * @param prefixLength 前面显示的位数
     * @param suffixLength 后面显示的位数
     * @return 脱敏后的身份证号，null表示输入无效
     */
    public static String maskIdCard(String idCard, int prefixLength, int suffixLength) {
        if (!ValidationUtil.isValidIdCard(idCard)) {
            return null;
        }

        int length = idCard.length();
        if (prefixLength + suffixLength >= length) {
            return idCard;
        }

        int maskLength = length - prefixLength - suffixLength;
        StringBuilder masked = new StringBuilder();
        masked.append(idCard, 0, prefixLength);
        for (int i = 0; i < maskLength; i++) {
            masked.append("*");
        }
        masked.append(idCard.substring(length - suffixLength));

        return masked.toString();
    }

    /**
     * 将15位身份证号转换为18位
     * 注意：仅用于简单转换，不保证完全准确
     *
     * @param idCard15 15位身份证号
     * @return 18位身份证号，null表示转换失败
     */
    public static String convert15To18(String idCard15) {
        if (idCard15 == null || idCard15.length() != 15) {
            return null;
        }

        if (!ValidationUtil.isNumeric(idCard15)) {
            return null;
        }

        try {
            // 补充世纪码（19或20）
            int year = Integer.parseInt(idCard15.substring(6, 8));
            String century = (year > 30) ? "19" : "20";
            String idCard17 = idCard15.substring(0, 6) + century + idCard15.substring(6);

            // 计算校验码
            char checkCode = calculateCheckCode(idCard17);

            return idCard17 + checkCode;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算18位身份证号的校验码
     *
     * @param idCard17 前17位身份证号
     * @return 校验码字符
     */
    private static char calculateCheckCode(String idCard17) {
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Character.getNumericValue(idCard17.charAt(i)) * weights[i];
        }

        int mod = sum % 11;
        return checkCodes[mod];
    }

    /**
     * 判断是否为有效的身份证号
     * （委托给ValidationUtil）
     *
     * @param idCard 身份证号
     * @return true表示有效，false表示无效
     */
    public static boolean isValid(String idCard) {
        return ValidationUtil.isValidIdCard(idCard);
    }
}
