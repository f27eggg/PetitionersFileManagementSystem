package com.petition.util;

import com.petition.model.enums.Gender;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IdCardUtil单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IdCardUtilTest {

    @Test
    @Order(1)
    @DisplayName("测试extractGender-18位男性")
    void testExtractGender18Male() {
        assertEquals(Gender.MALE, IdCardUtil.extractGender("11010119900101001X")); // 倒数第2位是奇数
        assertEquals(Gender.MALE, IdCardUtil.extractGender("130102199001010017")); // 倒数第2位是奇数
    }

    @Test
    @Order(2)
    @DisplayName("测试extractGender-18位女性")
    void testExtractGender18Female() {
        assertEquals(Gender.FEMALE, IdCardUtil.extractGender("110101199001010024")); // 倒数第2位是偶数
    }

    @Test
    @Order(3)
    @DisplayName("测试extractGender-15位")
    void testExtractGender15() {
        assertEquals(Gender.MALE, IdCardUtil.extractGender("110101900307761")); // 倒数第1位是奇数
    }

    @Test
    @Order(4)
    @DisplayName("测试extractGender-无效身份证号")
    void testExtractGenderInvalid() {
        assertNull(IdCardUtil.extractGender(null));
        assertNull(IdCardUtil.extractGender(""));
        assertNull(IdCardUtil.extractGender("12345"));
    }

    @Test
    @Order(5)
    @DisplayName("测试extractBirthDate-18位身份证")
    void testExtractBirthDate18() {
        LocalDate birthDate = IdCardUtil.extractBirthDate("11010119900101001X");
        assertNotNull(birthDate);
        assertEquals(1990, birthDate.getYear());
        assertEquals(1, birthDate.getMonthValue());
        assertEquals(1, birthDate.getDayOfMonth());
    }

    @Test
    @Order(6)
    @DisplayName("测试extractBirthDate-15位身份证")
    void testExtractBirthDate15() {
        LocalDate birthDate = IdCardUtil.extractBirthDate("110101900307761");
        assertNotNull(birthDate);
        assertEquals(1990, birthDate.getYear());
        assertEquals(3, birthDate.getMonthValue());
        assertEquals(7, birthDate.getDayOfMonth());
    }

    @Test
    @Order(7)
    @DisplayName("测试extractBirthDate-无效身份证号")
    void testExtractBirthDateInvalid() {
        assertNull(IdCardUtil.extractBirthDate(null));
        assertNull(IdCardUtil.extractBirthDate(""));
        assertNull(IdCardUtil.extractBirthDate("12345"));
    }

    @Test
    @Order(8)
    @DisplayName("测试extractAge-计算年龄")
    void testExtractAge() {
        int age = IdCardUtil.extractAge("11010119900101001X");
        assertTrue(age >= 34 && age <= 35); // 1990年出生，当前年龄应该在34-35之间
    }

    @Test
    @Order(9)
    @DisplayName("测试extractAge-无效身份证号")
    void testExtractAgeInvalid() {
        assertEquals(-1, IdCardUtil.extractAge(null));
        assertEquals(-1, IdCardUtil.extractAge("12345"));
    }

    @Test
    @Order(10)
    @DisplayName("测试extractProvinceCode")
    void testExtractProvinceCode() {
        assertEquals("37", IdCardUtil.extractProvinceCode("370102199001010410")); // 山东
        assertEquals("11", IdCardUtil.extractProvinceCode("11010119900101001X")); // 北京
    }

    @Test
    @Order(11)
    @DisplayName("测试extractProvinceCode-无效")
    void testExtractProvinceCodeInvalid() {
        assertNull(IdCardUtil.extractProvinceCode(null));
        assertNull(IdCardUtil.extractProvinceCode("12345"));
    }

    @Test
    @Order(12)
    @DisplayName("测试extractProvinceName")
    void testExtractProvinceName() {
        assertEquals("山东", IdCardUtil.extractProvinceName("370102199001010410"));
        assertEquals("北京", IdCardUtil.extractProvinceName("11010119900101001X"));
        assertEquals("广东", IdCardUtil.extractProvinceName("440301199001010071"));
        assertEquals("河北", IdCardUtil.extractProvinceName("130102199001010017"));
    }

    @Test
    @Order(13)
    @DisplayName("测试extractProvinceName-未知省份")
    void testExtractProvinceNameUnknown() {
        assertNull(IdCardUtil.extractProvinceName(null));
        assertNull(IdCardUtil.extractProvinceName("12345"));
    }

    @Test
    @Order(14)
    @DisplayName("测试maskIdCard-默认脱敏")
    void testMaskIdCardDefault() {
        assertEquals("370102********0410", IdCardUtil.maskIdCard("370102199001010410"));
        assertEquals("110101********001X", IdCardUtil.maskIdCard("11010119900101001X"));
        assertEquals("110101*****7761", IdCardUtil.maskIdCard("110101900307761"));
    }

    @Test
    @Order(15)
    @DisplayName("测试maskIdCard-自定义脱敏")
    void testMaskIdCardCustom() {
        assertEquals("3701**********0410", IdCardUtil.maskIdCard("370102199001010410", 4, 4));
        assertEquals("37010**********410", IdCardUtil.maskIdCard("370102199001010410", 5, 3));
    }

    @Test
    @Order(16)
    @DisplayName("测试maskIdCard-无效输入")
    void testMaskIdCardInvalid() {
        assertNull(IdCardUtil.maskIdCard(null));
        assertNull(IdCardUtil.maskIdCard("12345"));
    }

    @Test
    @Order(17)
    @DisplayName("测试convert15To18-正常转换")
    void testConvert15To18() {
        String id18 = IdCardUtil.convert15To18("110101900307761");
        assertNotNull(id18);
        assertEquals(18, id18.length());
        assertEquals("110101", id18.substring(0, 6));
        assertEquals("19900307", id18.substring(6, 14));
    }

    @Test
    @Order(18)
    @DisplayName("测试convert15To18-无效输入")
    void testConvert15To18Invalid() {
        assertNull(IdCardUtil.convert15To18(null));
        assertNull(IdCardUtil.convert15To18(""));
        assertNull(IdCardUtil.convert15To18("12345"));
        assertNull(IdCardUtil.convert15To18("370102199001011234")); // 已经是18位
    }

    @Test
    @Order(19)
    @DisplayName("测试isValid")
    void testIsValid() {
        assertTrue(IdCardUtil.isValid("370102199001010410"));
        assertTrue(IdCardUtil.isValid("110101900307761"));
        assertFalse(IdCardUtil.isValid(null));
        assertFalse(IdCardUtil.isValid("12345"));
    }
}
