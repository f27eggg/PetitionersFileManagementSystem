package com.petition.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 日期时间工具类
 * 提供日期时间格式化和计算功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class DateUtil {
    /**
     * 常用日期格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String COMPACT_DATE_PATTERN = "yyyyMMdd";
    public static final String COMPACT_DATETIME_PATTERN = "yyyyMMddHHmmss";
    public static final String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";
    public static final String CHINESE_DATETIME_PATTERN = "yyyy年MM月dd日 HH时mm分ss秒";

    /**
     * 常用日期格式化器
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final DateTimeFormatter COMPACT_DATE_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATE_PATTERN);
    public static final DateTimeFormatter COMPACT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATETIME_PATTERN);
    public static final DateTimeFormatter CHINESE_DATE_FORMATTER = DateTimeFormatter.ofPattern(CHINESE_DATE_PATTERN);
    public static final DateTimeFormatter CHINESE_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(CHINESE_DATETIME_PATTERN);

    /**
     * 私有构造函数，防止实例化
     */
    private DateUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 格式化日期为字符串（默认格式：yyyy-MM-dd）
     *
     * @param date 日期
     * @return 格式化后的字符串，null表示输入为null
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * 格式化日期为字符串（自定义格式）
     *
     * @param date 日期
     * @param pattern 日期格式
     * @return 格式化后的字符串，null表示输入为null
     */
    public static String formatDate(LocalDate date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期时间为字符串（默认格式：yyyy-MM-dd HH:mm:ss）
     *
     * @param dateTime 日期时间
     * @return 格式化后的字符串，null表示输入为null
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * 格式化日期时间为字符串（自定义格式）
     *
     * @param dateTime 日期时间
     * @param pattern 日期时间格式
     * @return 格式化后的字符串，null表示输入为null
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期字符串为LocalDate（默认格式：yyyy-MM-dd）
     *
     * @param dateStr 日期字符串
     * @return LocalDate对象，null表示解析失败
     */
    public static LocalDate parseDate(String dateStr) {
        if (ValidationUtil.isEmpty(dateStr)) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析日期字符串为LocalDate（自定义格式）
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return LocalDate对象，null表示解析失败
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (ValidationUtil.isEmpty(dateStr) || ValidationUtil.isEmpty(pattern)) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析日期时间字符串为LocalDateTime（默认格式：yyyy-MM-dd HH:mm:ss）
     *
     * @param dateTimeStr 日期时间字符串
     * @return LocalDateTime对象，null表示解析失败
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (ValidationUtil.isEmpty(dateTimeStr)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析日期时间字符串为LocalDateTime（自定义格式）
     *
     * @param dateTimeStr 日期时间字符串
     * @param pattern 日期时间格式
     * @return LocalDateTime对象，null表示解析失败
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (ValidationUtil.isEmpty(dateTimeStr) || ValidationUtil.isEmpty(pattern)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算两个日期之间的天数差
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 天数差，负数表示startDate在endDate之后
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个日期时间之间的秒数差
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime 结束日期时间
     * @return 秒数差
     */
    public static long secondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    /**
     * 增加天数
     *
     * @param date 日期
     * @param days 要增加的天数（可以为负数）
     * @return 增加后的日期，null表示输入为null
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }

    /**
     * 增加月数
     *
     * @param date 日期
     * @param months 要增加的月数（可以为负数）
     * @return 增加后的日期，null表示输入为null
     */
    public static LocalDate plusMonths(LocalDate date, long months) {
        if (date == null) {
            return null;
        }
        return date.plusMonths(months);
    }

    /**
     * 增加年数
     *
     * @param date 日期
     * @param years 要增加的年数（可以为负数）
     * @return 增加后的日期，null表示输入为null
     */
    public static LocalDate plusYears(LocalDate date, long years) {
        if (date == null) {
            return null;
        }
        return date.plusYears(years);
    }

    /**
     * 判断日期是否在指定范围内
     *
     * @param date 要判断的日期
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return true表示在范围内，false表示不在范围内或参数为null
     */
    public static boolean isBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * 判断是否为今天
     *
     * @param date 日期
     * @return true表示是今天，false表示不是今天或参数为null
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now());
    }

    /**
     * 判断是否为过去的日期
     *
     * @param date 日期
     * @return true表示是过去的日期，false表示不是或参数为null
     */
    public static boolean isPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }

    /**
     * 判断是否为未来的日期
     *
     * @param date 日期
     * @return true表示是未来的日期，false表示不是或参数为null
     */
    public static boolean isFuture(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }

    /**
     * 获取本月第一天
     *
     * @return 本月第一天
     */
    public static LocalDate getFirstDayOfMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }

    /**
     * 获取本月最后一天
     *
     * @return 本月最后一天
     */
    public static LocalDate getLastDayOfMonth() {
        LocalDate now = LocalDate.now();
        return now.withDayOfMonth(now.lengthOfMonth());
    }

    /**
     * 获取指定日期所在月的第一天
     *
     * @param date 日期
     * @return 月第一天，null表示输入为null
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(1);
    }

    /**
     * 获取指定日期所在月的最后一天
     *
     * @param date 日期
     * @return 月最后一天，null表示输入为null
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(date.lengthOfMonth());
    }
}
