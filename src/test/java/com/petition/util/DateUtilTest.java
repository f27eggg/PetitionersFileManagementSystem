package com.petition.util;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateUtil单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DateUtilTest {

    @Test
    @Order(1)
    @DisplayName("测试now")
    void testNow() {
        LocalDate date = DateUtil.now();
        assertNotNull(date);
        assertEquals(LocalDate.now(), date);
    }

    @Test
    @Order(2)
    @DisplayName("测试nowDateTime")
    void testNowDateTime() {
        LocalDateTime dateTime = DateUtil.nowDateTime();
        assertNotNull(dateTime);
    }

    @Test
    @Order(3)
    @DisplayName("测试formatDate-默认格式")
    void testFormatDate() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        assertEquals("2025-01-15", DateUtil.formatDate(date));
    }

    @Test
    @Order(4)
    @DisplayName("测试formatDate-自定义格式")
    void testFormatDateCustom() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        assertEquals("2025年01月15日", DateUtil.formatDate(date, "yyyy年MM月dd日"));
        assertEquals("20250115", DateUtil.formatDate(date, "yyyyMMdd"));
    }

    @Test
    @Order(5)
    @DisplayName("测试formatDate-null")
    void testFormatDateNull() {
        assertNull(DateUtil.formatDate(null));
        assertNull(DateUtil.formatDate(LocalDate.now(), null));
    }

    @Test
    @Order(6)
    @DisplayName("测试formatDateTime-默认格式")
    void testFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 15, 10, 30, 45);
        assertEquals("2025-01-15 10:30:45", DateUtil.formatDateTime(dateTime));
    }

    @Test
    @Order(7)
    @DisplayName("测试formatDateTime-自定义格式")
    void testFormatDateTimeCustom() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 15, 10, 30, 45);
        assertEquals("2025年01月15日 10时30分45秒", DateUtil.formatDateTime(dateTime, "yyyy年MM月dd日 HH时mm分ss秒"));
    }

    @Test
    @Order(8)
    @DisplayName("测试parseDate-默认格式")
    void testParseDate() {
        LocalDate date = DateUtil.parseDate("2025-01-15");
        assertNotNull(date);
        assertEquals(2025, date.getYear());
        assertEquals(1, date.getMonthValue());
        assertEquals(15, date.getDayOfMonth());
    }

    @Test
    @Order(9)
    @DisplayName("测试parseDate-自定义格式")
    void testParseDateCustom() {
        LocalDate date = DateUtil.parseDate("20250115", "yyyyMMdd");
        assertNotNull(date);
        assertEquals(2025, date.getYear());
        assertEquals(1, date.getMonthValue());
        assertEquals(15, date.getDayOfMonth());
    }

    @Test
    @Order(10)
    @DisplayName("测试parseDate-无效格式")
    void testParseDateInvalid() {
        assertNull(DateUtil.parseDate(null));
        assertNull(DateUtil.parseDate(""));
        assertNull(DateUtil.parseDate("invalid"));
        assertNull(DateUtil.parseDate("2025-01-15", null));
    }

    @Test
    @Order(11)
    @DisplayName("测试parseDateTime-默认格式")
    void testParseDateTime() {
        LocalDateTime dateTime = DateUtil.parseDateTime("2025-01-15 10:30:45");
        assertNotNull(dateTime);
        assertEquals(2025, dateTime.getYear());
        assertEquals(1, dateTime.getMonthValue());
        assertEquals(15, dateTime.getDayOfMonth());
        assertEquals(10, dateTime.getHour());
        assertEquals(30, dateTime.getMinute());
        assertEquals(45, dateTime.getSecond());
    }

    @Test
    @Order(12)
    @DisplayName("测试parseDateTime-无效格式")
    void testParseDateTimeInvalid() {
        assertNull(DateUtil.parseDateTime(null));
        assertNull(DateUtil.parseDateTime(""));
        assertNull(DateUtil.parseDateTime("invalid"));
    }

    @Test
    @Order(13)
    @DisplayName("测试daysBetween")
    void testDaysBetween() {
        LocalDate date1 = LocalDate.of(2025, 1, 1);
        LocalDate date2 = LocalDate.of(2025, 1, 15);

        assertEquals(14, DateUtil.daysBetween(date1, date2));
        assertEquals(-14, DateUtil.daysBetween(date2, date1));
        assertEquals(0, DateUtil.daysBetween(date1, date1));
    }

    @Test
    @Order(14)
    @DisplayName("测试daysBetween-null")
    void testDaysBetweenNull() {
        LocalDate date = LocalDate.now();
        assertEquals(0, DateUtil.daysBetween(null, date));
        assertEquals(0, DateUtil.daysBetween(date, null));
    }

    @Test
    @Order(15)
    @DisplayName("测试secondsBetween")
    void testSecondsBetween() {
        LocalDateTime dt1 = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2025, 1, 1, 10, 1, 0);

        assertEquals(60, DateUtil.secondsBetween(dt1, dt2));
        assertEquals(-60, DateUtil.secondsBetween(dt2, dt1));
    }

    @Test
    @Order(16)
    @DisplayName("测试plusDays")
    void testPlusDays() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        LocalDate result = DateUtil.plusDays(date, 10);

        assertEquals(LocalDate.of(2025, 1, 11), result);
    }

    @Test
    @Order(17)
    @DisplayName("测试plusDays-负数")
    void testPlusDaysNegative() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        LocalDate result = DateUtil.plusDays(date, -5);

        assertEquals(LocalDate.of(2025, 1, 10), result);
    }

    @Test
    @Order(18)
    @DisplayName("测试plusMonths")
    void testPlusMonths() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        LocalDate result = DateUtil.plusMonths(date, 3);

        assertEquals(LocalDate.of(2025, 4, 15), result);
    }

    @Test
    @Order(19)
    @DisplayName("测试plusYears")
    void testPlusYears() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        LocalDate result = DateUtil.plusYears(date, 2);

        assertEquals(LocalDate.of(2027, 1, 15), result);
    }

    @Test
    @Order(20)
    @DisplayName("测试isBetween-在范围内")
    void testIsBetweenTrue() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        assertTrue(DateUtil.isBetween(date, start, end));
        assertTrue(DateUtil.isBetween(start, start, end)); // 边界值
        assertTrue(DateUtil.isBetween(end, start, end)); // 边界值
    }

    @Test
    @Order(21)
    @DisplayName("测试isBetween-不在范围内")
    void testIsBetweenFalse() {
        LocalDate date = LocalDate.of(2025, 2, 1);
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        assertFalse(DateUtil.isBetween(date, start, end));
        assertFalse(DateUtil.isBetween(null, start, end));
    }

    @Test
    @Order(22)
    @DisplayName("测试isToday")
    void testIsToday() {
        assertTrue(DateUtil.isToday(LocalDate.now()));
        assertFalse(DateUtil.isToday(LocalDate.now().plusDays(1)));
        assertFalse(DateUtil.isToday(null));
    }

    @Test
    @Order(23)
    @DisplayName("测试isPast")
    void testIsPast() {
        assertTrue(DateUtil.isPast(LocalDate.now().minusDays(1)));
        assertFalse(DateUtil.isPast(LocalDate.now()));
        assertFalse(DateUtil.isPast(LocalDate.now().plusDays(1)));
        assertFalse(DateUtil.isPast(null));
    }

    @Test
    @Order(24)
    @DisplayName("测试isFuture")
    void testIsFuture() {
        assertTrue(DateUtil.isFuture(LocalDate.now().plusDays(1)));
        assertFalse(DateUtil.isFuture(LocalDate.now()));
        assertFalse(DateUtil.isFuture(LocalDate.now().minusDays(1)));
        assertFalse(DateUtil.isFuture(null));
    }

    @Test
    @Order(25)
    @DisplayName("测试getFirstDayOfMonth")
    void testGetFirstDayOfMonth() {
        LocalDate firstDay = DateUtil.getFirstDayOfMonth();
        assertEquals(1, firstDay.getDayOfMonth());
    }

    @Test
    @Order(26)
    @DisplayName("测试getLastDayOfMonth")
    void testGetLastDayOfMonth() {
        LocalDate lastDay = DateUtil.getLastDayOfMonth();
        assertTrue(lastDay.getDayOfMonth() >= 28 && lastDay.getDayOfMonth() <= 31);
    }

    @Test
    @Order(27)
    @DisplayName("测试getFirstDayOfMonth-指定日期")
    void testGetFirstDayOfMonthWithDate() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        LocalDate firstDay = DateUtil.getFirstDayOfMonth(date);

        assertEquals(LocalDate.of(2025, 1, 1), firstDay);
    }

    @Test
    @Order(28)
    @DisplayName("测试getLastDayOfMonth-指定日期")
    void testGetLastDayOfMonthWithDate() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        LocalDate lastDay = DateUtil.getLastDayOfMonth(date);

        assertEquals(LocalDate.of(2025, 1, 31), lastDay);
    }
}
