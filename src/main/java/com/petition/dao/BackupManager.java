package com.petition.dao;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 数据备份管理器
 * 负责数据文件的备份、恢复和自动备份功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class BackupManager {
    /**
     * 备份目录路径
     */
    private static final String BACKUP_DIR = "data/backups";

    /**
     * 备份文件名前缀
     */
    private static final String BACKUP_PREFIX = "petitioners_backup_";

    /**
     * 备份文件扩展名
     */
    private static final String BACKUP_EXTENSION = ".json";

    /**
     * 日期时间格式化器
     */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * 备份目录路径
     */
    private final Path backupDirectory;

    /**
     * 数据文件路径
     */
    private final Path dataFilePath;

    /**
     * 最大备份文件数量
     */
    private int maxBackupCount;

    /**
     * 自动备份定时器
     */
    private ScheduledExecutorService autoBackupScheduler;

    /**
     * 默认构造函数
     *
     * @param dataFilePath 数据文件路径
     */
    public BackupManager(Path dataFilePath) {
        this(dataFilePath, BACKUP_DIR, 7);
    }

    /**
     * 完整构造函数
     *
     * @param dataFilePath 数据文件路径
     * @param backupDir 备份目录路径
     * @param maxBackupCount 最大备份数量
     */
    public BackupManager(Path dataFilePath, String backupDir, int maxBackupCount) {
        this.dataFilePath = dataFilePath;
        this.backupDirectory = Paths.get(backupDir);
        this.maxBackupCount = maxBackupCount;

        ensureBackupDirectoryExists();
    }

    /**
     * 确保备份目录存在
     */
    private void ensureBackupDirectoryExists() {
        try {
            if (!Files.exists(backupDirectory)) {
                Files.createDirectories(backupDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建备份目录: " + e.getMessage(), e);
        }
    }

    /**
     * 执行备份操作
     * 将当前数据文件备份到备份目录
     *
     * @return 备份文件路径
     * @throws IOException 文件操作异常
     */
    public Path backup() throws IOException {
        // 检查数据文件是否存在
        if (!Files.exists(dataFilePath)) {
            throw new IOException("数据文件不存在: " + dataFilePath);
        }

        // 生成备份文件名
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String backupFileName = BACKUP_PREFIX + timestamp + BACKUP_EXTENSION;
        Path backupFilePath = backupDirectory.resolve(backupFileName);

        // 复制文件到备份目录
        Files.copy(dataFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);

        // 清理旧备份
        cleanupOldBackups();

        return backupFilePath;
    }

    /**
     * 从备份文件恢复数据
     *
     * @param backupFileName 备份文件名
     * @return 是否恢复成功
     * @throws IOException 文件操作异常
     */
    public boolean restore(String backupFileName) throws IOException {
        Path backupFilePath = backupDirectory.resolve(backupFileName);

        // 检查备份文件是否存在
        if (!Files.exists(backupFilePath)) {
            throw new IOException("备份文件不存在: " + backupFileName);
        }

        // 在恢复之前先备份当前数据（安全措施）
        if (Files.exists(dataFilePath)) {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            String safetyBackupName = "before_restore_" + timestamp + BACKUP_EXTENSION;
            Path safetyBackupPath = backupDirectory.resolve(safetyBackupName);
            Files.copy(dataFilePath, safetyBackupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 恢复备份文件到数据目录
        Files.copy(backupFilePath, dataFilePath, StandardCopyOption.REPLACE_EXISTING);

        return true;
    }

    /**
     * 恢复最新的备份
     *
     * @return 是否恢复成功
     * @throws IOException 文件操作异常
     */
    public boolean restoreLatest() throws IOException {
        List<String> backups = listBackups();

        if (backups.isEmpty()) {
            throw new IOException("没有可用的备份文件");
        }

        // 获取最新的备份文件（列表已按时间倒序排列）
        String latestBackup = backups.get(0);
        return restore(latestBackup);
    }

    /**
     * 获取所有备份文件列表
     * 按时间倒序排列（最新的在前）
     *
     * @return 备份文件名列表
     * @throws IOException 文件操作异常
     */
    public List<String> listBackups() throws IOException {
        List<String> backups = new ArrayList<>();

        try (Stream<Path> stream = Files.list(backupDirectory)) {
            stream.filter(Files::isRegularFile)
                  .filter(path -> path.getFileName().toString().startsWith(BACKUP_PREFIX))
                  .filter(path -> path.getFileName().toString().endsWith(BACKUP_EXTENSION))
                  .sorted(Comparator.comparing(Path::getFileName).reversed())
                  .forEach(path -> backups.add(path.getFileName().toString()));
        }

        return backups;
    }

    /**
     * 清理旧的备份文件
     * 只保留最新的maxBackupCount个备份
     *
     * @throws IOException 文件操作异常
     */
    private void cleanupOldBackups() throws IOException {
        List<String> backups = listBackups();

        // 如果备份数量超过限制，删除旧的备份
        if (backups.size() > maxBackupCount) {
            for (int i = maxBackupCount; i < backups.size(); i++) {
                Path oldBackup = backupDirectory.resolve(backups.get(i));
                Files.deleteIfExists(oldBackup);
            }
        }
    }

    /**
     * 启动自动备份
     * 按指定间隔定期备份数据
     *
     * @param intervalHours 备份间隔（小时）
     */
    public void startAutoBackup(int intervalHours) {
        // 如果已有定时器在运行，先停止
        stopAutoBackup();

        // 创建定时任务
        autoBackupScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "AutoBackupThread");
            thread.setDaemon(true); // 设置为守护线程
            return thread;
        });

        // 延迟1小时后首次执行，然后按间隔执行
        autoBackupScheduler.scheduleAtFixedRate(() -> {
            try {
                Path backupPath = backup();
                System.out.println("自动备份完成: " + backupPath.getFileName());
            } catch (IOException e) {
                System.err.println("自动备份失败: " + e.getMessage());
            }
        }, 1, intervalHours, TimeUnit.HOURS);
    }

    /**
     * 停止自动备份
     */
    public void stopAutoBackup() {
        if (autoBackupScheduler != null && !autoBackupScheduler.isShutdown()) {
            autoBackupScheduler.shutdown();
            try {
                if (!autoBackupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    autoBackupScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                autoBackupScheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 删除指定的备份文件
     *
     * @param backupFileName 备份文件名
     * @return 是否删除成功
     * @throws IOException 文件操作异常
     */
    public boolean deleteBackup(String backupFileName) throws IOException {
        Path backupFilePath = backupDirectory.resolve(backupFileName);
        return Files.deleteIfExists(backupFilePath);
    }

    /**
     * 获取备份文件的大小
     *
     * @param backupFileName 备份文件名
     * @return 文件大小（字节）
     * @throws IOException 文件操作异常
     */
    public long getBackupSize(String backupFileName) throws IOException {
        Path backupFilePath = backupDirectory.resolve(backupFileName);
        return Files.size(backupFilePath);
    }

    /**
     * 设置最大备份数量
     *
     * @param maxBackupCount 最大备份数量
     */
    public void setMaxBackupCount(int maxBackupCount) {
        this.maxBackupCount = maxBackupCount;
    }

    /**
     * 获取最大备份数量
     *
     * @return 最大备份数量
     */
    public int getMaxBackupCount() {
        return maxBackupCount;
    }

    /**
     * 获取备份目录路径
     *
     * @return 备份目录路径
     */
    public Path getBackupDirectory() {
        return backupDirectory;
    }
}
