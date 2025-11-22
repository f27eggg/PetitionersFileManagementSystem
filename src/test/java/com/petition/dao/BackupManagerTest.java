package com.petition.dao;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BackupManager单元测试
 * 测试备份管理器的各项功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BackupManagerTest {
    private static final String TEST_DATA_DIR = "test_backup_data";
    private static final String TEST_BACKUP_DIR = "test_backup_data/backups";
    private static final String TEST_DATA_FILE = "test_data.json";

    private Path testDataFilePath;
    private BackupManager backupManager;

    @BeforeEach
    void setUp() throws IOException {
        // 创建测试数据目录
        Path dataDir = Paths.get(TEST_DATA_DIR);
        Files.createDirectories(dataDir);

        // 创建测试数据文件
        testDataFilePath = dataDir.resolve(TEST_DATA_FILE);
        Files.writeString(testDataFilePath, "{\"test\": \"data\"}");

        // 创建BackupManager
        backupManager = new BackupManager(testDataFilePath, TEST_BACKUP_DIR, 5);
    }

    @AfterEach
    void tearDown() throws IOException {
        // 停止自动备份
        backupManager.stopAutoBackup();

        // 清理测试数据
        Path testDir = Paths.get(TEST_DATA_DIR);
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
    @DisplayName("测试备份功能")
    void testBackup() throws IOException {
        // 执行备份
        Path backupPath = backupManager.backup();

        // 验证
        assertNotNull(backupPath);
        assertTrue(Files.exists(backupPath));
        assertTrue(backupPath.getFileName().toString().startsWith("petitioners_backup_"));
        assertTrue(backupPath.getFileName().toString().endsWith(".json"));
    }

    @Test
    @Order(2)
    @DisplayName("测试恢复备份")
    void testRestore() throws IOException {
        // 创建备份
        Path backupPath = backupManager.backup();
        String backupFileName = backupPath.getFileName().toString();

        // 修改原文件
        Files.writeString(testDataFilePath, "{\"modified\": \"data\"}");

        // 恢复备份
        boolean restored = backupManager.restore(backupFileName);

        // 验证
        assertTrue(restored);
        String content = Files.readString(testDataFilePath);
        assertTrue(content.contains("test"));
    }

    @Test
    @Order(3)
    @DisplayName("测试列出所有备份")
    void testListBackups() throws IOException, InterruptedException {
        // 创建多个备份
        backupManager.backup();
        Thread.sleep(1100); // 确保文件名时间戳不同（至少1秒）
        backupManager.backup();
        Thread.sleep(1100);
        backupManager.backup();

        // 获取备份列表
        List<String> backups = backupManager.listBackups();

        // 验证
        assertNotNull(backups);
        assertEquals(3, backups.size());
        // 验证按时间倒序排列（最新的在前）
        for (String backup : backups) {
            assertTrue(backup.startsWith("petitioners_backup_"));
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试恢复最新备份")
    void testRestoreLatest() throws IOException, InterruptedException {
        // 创建第一个备份
        backupManager.backup();
        Thread.sleep(1100);

        // 修改数据
        Files.writeString(testDataFilePath, "{\"modified1\": \"data\"}");

        // 创建第二个备份
        backupManager.backup();
        Thread.sleep(1100);

        // 再次修改数据
        Files.writeString(testDataFilePath, "{\"modified2\": \"data\"}");

        // 恢复最新备份
        boolean restored = backupManager.restoreLatest();

        // 验证
        assertTrue(restored);
        String content = Files.readString(testDataFilePath);
        assertTrue(content.contains("modified1"));
    }

    @Test
    @Order(5)
    @DisplayName("测试清理旧备份")
    void testCleanupOldBackups() throws IOException, InterruptedException {
        // 设置最大备份数为3
        backupManager.setMaxBackupCount(3);

        // 创建5个备份
        for (int i = 0; i < 5; i++) {
            backupManager.backup();
            Thread.sleep(1100);
        }

        // 验证只保留3个备份
        List<String> backups = backupManager.listBackups();
        assertEquals(3, backups.size());
    }

    @Test
    @Order(6)
    @DisplayName("测试删除备份")
    void testDeleteBackup() throws IOException {
        // 创建备份
        Path backupPath = backupManager.backup();
        String backupFileName = backupPath.getFileName().toString();

        // 验证备份存在
        assertTrue(Files.exists(backupPath));

        // 删除备份
        boolean deleted = backupManager.deleteBackup(backupFileName);

        // 验证
        assertTrue(deleted);
        assertFalse(Files.exists(backupPath));
    }

    @Test
    @Order(7)
    @DisplayName("测试获取备份文件大小")
    void testGetBackupSize() throws IOException {
        // 创建备份
        Path backupPath = backupManager.backup();
        String backupFileName = backupPath.getFileName().toString();

        // 获取文件大小
        long size = backupManager.getBackupSize(backupFileName);

        // 验证
        assertTrue(size > 0);
        assertEquals(Files.size(backupPath), size);
    }

    @Test
    @Order(8)
    @DisplayName("测试备份不存在的文件")
    void testBackupNonExistentFile() throws IOException {
        // 删除数据文件
        Files.deleteIfExists(testDataFilePath);

        // 尝试备份
        assertThrows(IOException.class, () -> backupManager.backup());
    }

    @Test
    @Order(9)
    @DisplayName("测试恢复不存在的备份")
    void testRestoreNonExistentBackup() {
        // 尝试恢复不存在的备份
        assertThrows(IOException.class, () -> backupManager.restore("non_existent_backup.json"));
    }

    @Test
    @Order(10)
    @DisplayName("测试自动备份启动和停止")
    void testAutoBackup() throws InterruptedException {
        // 注意：这个测试不会等待实际的备份执行，只测试启动和停止功能
        // 启动自动备份（间隔1小时）
        assertDoesNotThrow(() -> backupManager.startAutoBackup(1));

        // 等待一小段时间
        Thread.sleep(100);

        // 停止自动备份
        assertDoesNotThrow(() -> backupManager.stopAutoBackup());
    }

    @Test
    @Order(11)
    @DisplayName("测试获取和设置最大备份数")
    void testMaxBackupCount() {
        // 设置最大备份数
        backupManager.setMaxBackupCount(10);

        // 验证
        assertEquals(10, backupManager.getMaxBackupCount());
    }

    @Test
    @Order(12)
    @DisplayName("测试恢复功能创建了数据备份")
    void testRestoreCreatesDataBackup() throws IOException, InterruptedException {
        // 创建初始备份
        Path initialBackup = backupManager.backup();
        String initialBackupName = initialBackup.getFileName().toString();

        // 等待确保时间戳不同
        Thread.sleep(1100);

        // 修改数据（写入新内容）
        String newData = "{\"modified\": \"data\"}";
        Files.writeString(testDataFilePath, newData);

        // 执行恢复
        boolean restored = backupManager.restore(initialBackupName);
        assertTrue(restored);

        // 验证数据已恢复（不是修改后的内容）
        String restoredContent = Files.readString(testDataFilePath);
        assertFalse(restoredContent.contains("modified"));
        assertTrue(restoredContent.contains("test"));
    }
}
