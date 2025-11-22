package com.petition.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理器
 * 负责系统配置的加载和保存
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class ConfigManager {
    /**
     * 配置文件存储路径
     */
    private static final String CONFIG_DIR = "data";

    /**
     * 配置文件名
     */
    private static final String CONFIG_FILE = "config.json";

    /**
     * Jackson ObjectMapper实例
     */
    private final ObjectMapper objectMapper;

    /**
     * 配置文件完整路径
     */
    private final Path configFilePath;

    /**
     * 配置数据缓存
     */
    private Map<String, Object> configCache;

    /**
     * 默认构造函数
     */
    public ConfigManager() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.configFilePath = Paths.get(CONFIG_DIR, CONFIG_FILE);
        this.configCache = new HashMap<>();

        ensureConfigDirectoryExists();
        initializeDefaultConfig();
    }

    /**
     * 构造函数（支持自定义配置目录）
     *
     * @param configDirectory 配置目录路径
     */
    public ConfigManager(String configDirectory) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.configFilePath = Paths.get(configDirectory, CONFIG_FILE);
        this.configCache = new HashMap<>();

        ensureConfigDirectoryExists();
        initializeDefaultConfig();
    }

    /**
     * 确保配置目录存在
     */
    private void ensureConfigDirectoryExists() {
        try {
            Path directory = configFilePath.getParent();
            if (directory != null && !Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建配置目录: " + e.getMessage(), e);
        }
    }

    /**
     * 初始化默认配置
     */
    private void initializeDefaultConfig() {
        configCache.put("appName", "上访人员重点监控信息管理系统");
        configCache.put("version", "1.0.0");
        configCache.put("autoBackup", true);
        configCache.put("backupInterval", 24); // 小时
        configCache.put("maxBackupCount", 7); // 保留最近7个备份
        configCache.put("pageSize", 20); // 每页显示记录数
        configCache.put("theme", "dark"); // 深色主题
        configCache.put("windowWidth", 1280);
        configCache.put("windowHeight", 800);
        configCache.put("dataDirectory", "data");
        configCache.put("backupDirectory", "data/backups");
    }

    /**
     * 加载配置
     * 从JSON文件读取配置
     *
     * @return 配置Map
     * @throws IOException 文件读取异常
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> loadConfig() throws IOException {
        File configFile = configFilePath.toFile();

        if (configFile.exists()) {
            // 读取配置文件
            Map<String, Object> fileConfig = objectMapper.readValue(configFile, Map.class);
            // 合并到缓存（文件配置覆盖默认配置）
            configCache.putAll(fileConfig);
        } else {
            // 如果配置文件不存在，保存默认配置
            saveConfig(configCache);
        }

        return new HashMap<>(configCache);
    }

    /**
     * 保存配置
     * 将配置写入JSON文件
     *
     * @param config 配置Map
     * @throws IOException 文件写入异常
     */
    public void saveConfig(Map<String, Object> config) throws IOException {
        if (config == null) {
            throw new IllegalArgumentException("配置不能为null");
        }

        // 更新缓存
        this.configCache = new HashMap<>(config);

        // 写入文件
        objectMapper.writeValue(configFilePath.toFile(), config);
    }

    /**
     * 获取配置项
     *
     * @param key 配置键
     * @return 配置值
     */
    public Object get(String key) {
        return configCache.get(key);
    }

    /**
     * 获取字符串配置项
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String getString(String key, String defaultValue) {
        Object value = configCache.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    /**
     * 获取整数配置项
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public int getInt(String key, int defaultValue) {
        Object value = configCache.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }

    /**
     * 获取布尔配置项
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = configCache.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }

    /**
     * 设置配置项
     *
     * @param key 配置键
     * @param value 配置值
     */
    public void set(String key, Object value) {
        configCache.put(key, value);
    }

    /**
     * 保存当前配置到文件
     *
     * @throws IOException 文件写入异常
     */
    public void save() throws IOException {
        saveConfig(configCache);
    }

    /**
     * 重置为默认配置
     *
     * @throws IOException 文件写入异常
     */
    public void resetToDefault() throws IOException {
        configCache.clear();
        initializeDefaultConfig();
        save();
    }

    /**
     * 获取配置文件路径
     *
     * @return 配置文件路径
     */
    public Path getConfigFilePath() {
        return configFilePath;
    }

    /**
     * 刷新配置
     * 从文件重新加载配置
     *
     * @throws IOException 文件读取异常
     */
    public void refresh() throws IOException {
        loadConfig();
    }
}
