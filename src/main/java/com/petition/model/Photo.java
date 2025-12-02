package com.petition.model;

import java.time.LocalDateTime;

/**
 * 照片实体类
 * 用于存储人员的多张照片信息
 */
public class Photo {

    private Integer id;
    private Integer personId;       // 关联人员ID
    private String filePath;        // 照片文件路径
    private String fileName;        // 原始文件名
    private String description;     // 照片描述
    private Boolean isPrimary;      // 是否为主照片（头像）
    private LocalDateTime uploadTime;   // 上传时间
    private Long fileSize;          // 文件大小(字节)
    private String mimeType;        // 文件类型

    public Photo() {
        this.isPrimary = false;
        this.uploadTime = LocalDateTime.now();
    }

    public Photo(Integer personId, String filePath, String fileName) {
        this();
        this.personId = personId;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", personId=" + personId +
                ", fileName='" + fileName + '\'' +
                ", isPrimary=" + isPrimary +
                '}';
    }
}

