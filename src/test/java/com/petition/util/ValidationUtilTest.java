package com.petition.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ValidationUtil单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ValidationUtilTest {

    @Test
    @Order(1)
    @DisplayName("测试isEmpty-null")
    void testIsEmptyNull() {
        assertTrue(ValidationUtil.isEmpty(null));
    }

    @Test
    @Order(2)
    @DisplayName("测试isEmpty-空字符串")
    void testIsEmptyBlank() {
        assertTrue(ValidationUtil.isEmpty(""));
        assertTrue(ValidationUtil.isEmpty("   "));
        assertTrue(ValidationUtil.isEmpty("\t"));
    }

    @Test
    @Order(3)
    @DisplayName("测试isEmpty-非空字符串")
    void testIsEmptyNonBlank() {
        assertFalse(ValidationUtil.isEmpty("test"));
        assertFalse(ValidationUtil.isEmpty(" test "));
    }

    @Test
    @Order(4)
    @DisplayName("测试isNotEmpty")
    void testIsNotEmpty() {
        assertTrue(ValidationUtil.isNotEmpty("test"));
        assertFalse(ValidationUtil.isNotEmpty(null));
        assertFalse(ValidationUtil.isNotEmpty(""));
    }

    @Test
    @Order(5)
    @DisplayName("测试isValidIdCard-18位有效身份证号")
    void testIsValidIdCard18Valid() {
        assertTrue(ValidationUtil.isValidIdCard("11010119900101001X")); // 北京
        assertTrue(ValidationUtil.isValidIdCard("130102199001010017")); // 河北
    }

    @Test
    @Order(6)
    @DisplayName("测试isValidIdCard-15位有效身份证号")
    void testIsValidIdCard15Valid() {
        assertTrue(ValidationUtil.isValidIdCard("110101900307761")); // 15位
    }

    @Test
    @Order(7)
    @DisplayName("测试isValidIdCard-无效身份证号")
    void testIsValidIdCardInvalid() {
        assertFalse(ValidationUtil.isValidIdCard(null));
        assertFalse(ValidationUtil.isValidIdCard(""));
        assertFalse(ValidationUtil.isValidIdCard("12345"));
        assertFalse(ValidationUtil.isValidIdCard("123456789012345678")); // 格式不对
    }

    @Test
    @Order(8)
    @DisplayName("测试isValidPhone-有效手机号")
    void testIsValidPhoneValid() {
        assertTrue(ValidationUtil.isValidPhone("13800138000"));
        assertTrue(ValidationUtil.isValidPhone("15912345678"));
        assertTrue(ValidationUtil.isValidPhone("18888888888"));
    }

    @Test
    @Order(9)
    @DisplayName("测试isValidPhone-带格式的手机号")
    void testIsValidPhoneFormatted() {
        assertTrue(ValidationUtil.isValidPhone("138 0013 8000"));
        assertTrue(ValidationUtil.isValidPhone("138-0013-8000"));
        assertTrue(ValidationUtil.isValidPhone("(138)00138000"));
    }

    @Test
    @Order(10)
    @DisplayName("测试isValidPhone-无效手机号")
    void testIsValidPhoneInvalid() {
        assertFalse(ValidationUtil.isValidPhone(null));
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone("12345678901")); // 不是1开头
        assertFalse(ValidationUtil.isValidPhone("1380013800")); // 长度不够
        assertFalse(ValidationUtil.isValidPhone("138001380000")); // 长度超过
    }

    @Test
    @Order(11)
    @DisplayName("测试isValidEmail-有效邮箱")
    void testIsValidEmailValid() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user_123@test.com.cn"));
        assertTrue(ValidationUtil.isValidEmail("admin-user@company.org"));
    }

    @Test
    @Order(12)
    @DisplayName("测试isValidEmail-无效邮箱")
    void testIsValidEmailInvalid() {
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail("test"));
        assertFalse(ValidationUtil.isValidEmail("test@"));
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
        assertFalse(ValidationUtil.isValidEmail("test@example"));
    }

    @Test
    @Order(13)
    @DisplayName("测试isLengthInRange-在范围内")
    void testIsLengthInRangeValid() {
        assertTrue(ValidationUtil.isLengthInRange("test", 1, 10));
        assertTrue(ValidationUtil.isLengthInRange("test", 4, 4));
        assertTrue(ValidationUtil.isLengthInRange("", 0, 5));
    }

    @Test
    @Order(14)
    @DisplayName("测试isLengthInRange-不在范围内")
    void testIsLengthInRangeInvalid() {
        assertFalse(ValidationUtil.isLengthInRange(null, 1, 10));
        assertFalse(ValidationUtil.isLengthInRange("test", 5, 10));
        assertFalse(ValidationUtil.isLengthInRange("test", 1, 3));
    }

    @Test
    @Order(15)
    @DisplayName("测试isNumberInRange")
    void testIsNumberInRange() {
        assertTrue(ValidationUtil.isNumberInRange(5, 1, 10));
        assertTrue(ValidationUtil.isNumberInRange(1, 1, 10));
        assertTrue(ValidationUtil.isNumberInRange(10, 1, 10));
        assertFalse(ValidationUtil.isNumberInRange(0, 1, 10));
        assertFalse(ValidationUtil.isNumberInRange(11, 1, 10));
    }

    @Test
    @Order(16)
    @DisplayName("测试isNumeric-纯数字")
    void testIsNumericValid() {
        assertTrue(ValidationUtil.isNumeric("123"));
        assertTrue(ValidationUtil.isNumeric("0"));
        assertTrue(ValidationUtil.isNumeric("999999"));
    }

    @Test
    @Order(17)
    @DisplayName("测试isNumeric-非纯数字")
    void testIsNumericInvalid() {
        assertFalse(ValidationUtil.isNumeric(null));
        assertFalse(ValidationUtil.isNumeric(""));
        assertFalse(ValidationUtil.isNumeric("12.3"));
        assertFalse(ValidationUtil.isNumeric("abc"));
        assertFalse(ValidationUtil.isNumeric("12a"));
    }

    @Test
    @Order(18)
    @DisplayName("测试isAlpha-纯字母")
    void testIsAlphaValid() {
        assertTrue(ValidationUtil.isAlpha("abc"));
        assertTrue(ValidationUtil.isAlpha("ABC"));
        assertTrue(ValidationUtil.isAlpha("AbC"));
    }

    @Test
    @Order(19)
    @DisplayName("测试isAlpha-非纯字母")
    void testIsAlphaInvalid() {
        assertFalse(ValidationUtil.isAlpha(null));
        assertFalse(ValidationUtil.isAlpha(""));
        assertFalse(ValidationUtil.isAlpha("abc123"));
        assertFalse(ValidationUtil.isAlpha("abc "));
        assertFalse(ValidationUtil.isAlpha("中文"));
    }

    @Test
    @Order(20)
    @DisplayName("测试isAlphanumeric-字母和数字")
    void testIsAlphanumericValid() {
        assertTrue(ValidationUtil.isAlphanumeric("abc123"));
        assertTrue(ValidationUtil.isAlphanumeric("ABC"));
        assertTrue(ValidationUtil.isAlphanumeric("123"));
    }

    @Test
    @Order(21)
    @DisplayName("测试isAlphanumeric-包含特殊字符")
    void testIsAlphanumericInvalid() {
        assertFalse(ValidationUtil.isAlphanumeric(null));
        assertFalse(ValidationUtil.isAlphanumeric(""));
        assertFalse(ValidationUtil.isAlphanumeric("abc-123"));
        assertFalse(ValidationUtil.isAlphanumeric("abc 123"));
        assertFalse(ValidationUtil.isAlphanumeric("中文123"));
    }

    @Test
    @Order(22)
    @DisplayName("测试matches-正则匹配")
    void testMatches() {
        assertTrue(ValidationUtil.matches("test123", "test\\d+"));
        assertTrue(ValidationUtil.matches("abc", "[a-z]+"));
        assertFalse(ValidationUtil.matches("test", "\\d+"));
        assertFalse(ValidationUtil.matches(null, "test"));
        assertFalse(ValidationUtil.matches("test", null));
    }
}
