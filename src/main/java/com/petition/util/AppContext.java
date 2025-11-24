package com.petition.util;

import com.petition.dao.ConfigManager;

/**
 * 应用程序上下文（全局状态管理）
 * 功能：管理全局配置、用户信息、应用状态等
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class AppContext {

    // ==================== 单例实现 ====================

    private static AppContext instance;

    private AppContext() {
        loadConfig();
    }

    /**
     * 获取单例实例
     */
    public static AppContext getInstance() {
        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext();
                }
            }
        }
        return instance;
    }

    // ==================== 配置属性 ====================

    private final ConfigManager configManager = new ConfigManager();

    // 用户信息
    private String currentUser = "管理员";

    // 显示设置
    private int pageSize = 20;
    private boolean maskIdCard = true;
    private String exportFormat = "Excel (.xlsx)";

    // 备份设置
    private boolean autoBackup = false;
    private int backupInterval = 6;

    // ==================== 加载配置 ====================

    /**
     * 从配置文件加载设置
     */
    private void loadConfig() {
        try {
            this.pageSize = configManager.getInt("pageSize", 20);
            this.maskIdCard = configManager.getBoolean("maskIdCard", true);
            this.exportFormat = configManager.getString("exportFormat", "Excel (.xlsx)");
            this.autoBackup = configManager.getBoolean("autoBackup", false);
            this.backupInterval = configManager.getInt("backupInterval", 6);
        } catch (Exception e) {
            System.err.println("加载配置失败，使用默认值: " + e.getMessage());
        }
    }

    /**
     * 重新加载配置
     */
    public void reloadConfig() {
        loadConfig();
    }

    // ==================== Getter/Setter ====================

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        configManager.set("pageSize", pageSize);
    }

    public boolean isMaskIdCard() {
        return maskIdCard;
    }

    public void setMaskIdCard(boolean maskIdCard) {
        this.maskIdCard = maskIdCard;
        configManager.set("maskIdCard", maskIdCard);
    }

    public String getExportFormat() {
        return exportFormat;
    }

    public void setExportFormat(String exportFormat) {
        this.exportFormat = exportFormat;
        configManager.set("exportFormat", exportFormat);
    }

    public boolean isAutoBackup() {
        return autoBackup;
    }

    public void setAutoBackup(boolean autoBackup) {
        this.autoBackup = autoBackup;
        configManager.set("autoBackup", autoBackup);
    }

    public int getBackupInterval() {
        return backupInterval;
    }

    public void setBackupInterval(int backupInterval) {
        this.backupInterval = backupInterval;
        configManager.set("backupInterval", backupInterval);
    }

    /**
     * 获取配置管理器
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * 对身份证号进行脱敏处理
     *
     * @param idCard 原始身份证号
     * @return 脱敏后的身份证号
     */
    public String maskIdCardNumber(String idCard) {
        if (!maskIdCard || idCard == null || idCard.length() < 10) {
            return idCard;
        }
        // 只显示前6位和后4位
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }
}
