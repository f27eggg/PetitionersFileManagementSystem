package com.petition.dao;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ConfigManager单元测试
 * 测试配置管理器的各项功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConfigManagerTest {
    private static final String TEST_CONFIG_DIR = "test_config";
    private ConfigManager configManager;

    @BeforeEach
    void setUp() {
        configManager = new ConfigManager(TEST_CONFIG_DIR);
    }

    @AfterEach
    void tearDown() throws IOException {
        // 清理测试配置
        Path testDir = Paths.get(TEST_CONFIG_DIR);
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                 .sorted((a, b) -> b.compareTo(a))
                 .forEach(path -> {
                     try {
                         Files.deleteIfExists(path);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 });
        }
    }

    @Test
    @Order(1)
    @DisplayName("测试加载默认配置")
    void testLoadDefaultConfig() throws IOException {
        Map<String, Object> config = configManager.loadConfig();

        assertNotNull(config);
        assertEquals("上访人员重点监控信息管理系统", config.get("appName"));
        assertEquals("1.0.0", config.get("version"));
        assertEquals(true, config.get("autoBackup"));
    }

    @Test
    @Order(2)
    @DisplayName("测试保存配置")
    void testSaveConfig() throws IOException {
        Map<String, Object> config = configManager.loadConfig();
        config.put("testKey", "testValue");

        configManager.saveConfig(config);

        // 重新加载验证
        ConfigManager newManager = new ConfigManager(TEST_CONFIG_DIR);
        Map<String, Object> reloaded = newManager.loadConfig();

        assertEquals("testValue", reloaded.get("testKey"));
    }

    @Test
    @Order(3)
    @DisplayName("测试获取字符串配置")
    void testGetString() throws IOException {
        configManager.loadConfig();

        String appName = configManager.getString("appName", "默认名称");
        assertEquals("上访人员重点监控信息管理系统", appName);

        String notExist = configManager.getString("notExist", "默认值");
        assertEquals("默认值", notExist);
    }

    @Test
    @Order(4)
    @DisplayName("测试获取整数配置")
    void testGetInt() throws IOException {
        configManager.loadConfig();

        int pageSize = configManager.getInt("pageSize", 10);
        assertEquals(20, pageSize);

        int notExist = configManager.getInt("notExist", 99);
        assertEquals(99, notExist);
    }

    @Test
    @Order(5)
    @DisplayName("测试获取布尔配置")
    void testGetBoolean() throws IOException {
        configManager.loadConfig();

        boolean autoBackup = configManager.getBoolean("autoBackup", false);
        assertTrue(autoBackup);

        boolean notExist = configManager.getBoolean("notExist", false);
        assertFalse(notExist);
    }

    @Test
    @Order(6)
    @DisplayName("测试设置配置项")
    void testSet() throws IOException {
        configManager.loadConfig();
        configManager.set("customKey", "customValue");
        configManager.save();

        // 验证
        assertEquals("customValue", configManager.get("customKey"));
    }

    @Test
    @Order(7)
    @DisplayName("测试重置为默认配置")
    void testResetToDefault() throws IOException {
        configManager.loadConfig();
        configManager.set("customKey", "customValue");
        configManager.save();

        // 验证自定义配置已保存
        assertEquals("customValue", configManager.get("customKey"));

        // 重置
        configManager.resetToDefault();

        // 验证已恢复默认配置
        assertNull(configManager.get("customKey"));
        assertEquals("上访人员重点监控信息管理系统", configManager.get("appName"));
    }

    @Test
    @Order(8)
    @DisplayName("测试配置持久化")
    void testConfigPersistence() throws IOException {
        configManager.loadConfig();
        configManager.set("persistKey", "persistValue");
        configManager.save();

        // 创建新的ConfigManager实例（模拟程序重启）
        ConfigManager newManager = new ConfigManager(TEST_CONFIG_DIR);
        newManager.loadConfig();

        // 验证配置已持久化
        assertEquals("persistValue", newManager.get("persistKey"));
    }

    @Test
    @Order(9)
    @DisplayName("测试刷新配置")
    void testRefresh() throws IOException {
        configManager.loadConfig();
        configManager.set("beforeRefresh", "value1");

        // 外部修改配置文件
        Map<String, Object> externalConfig = configManager.loadConfig();
        externalConfig.put("afterRefresh", "value2");
        configManager.saveConfig(externalConfig);

        // 刷新配置
        configManager.refresh();

        // 验证配置已更新
        assertEquals("value2", configManager.get("afterRefresh"));
    }
}
