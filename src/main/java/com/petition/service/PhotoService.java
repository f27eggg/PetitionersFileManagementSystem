package com.petition.service;

import com.petition.dao.PhotoDao;
import com.petition.model.Photo;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 照片服务层
 * 处理照片的业务逻辑，包括文件存储和管理
 */
public class PhotoService {

    private final PhotoDao photoDao;
    private final String photoStoragePath;

    // 支持的图片格式
    private static final List<String> SUPPORTED_FORMATS = List.of(
        ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );

    // 最大文件大小 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public PhotoService() {
        this.photoDao = new PhotoDao();
        // 照片存储目录，可以通过配置文件设置
        this.photoStoragePath = getStoragePath();
        ensureStorageDirectoryExists();
    }

    public PhotoService(String storagePath) {
        this.photoDao = new PhotoDao();
        this.photoStoragePath = storagePath;
        ensureStorageDirectoryExists();
    }

    /**
     * 获取存储路径
     */
    private String getStoragePath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + ".petition-system" + File.separator + "photos";
    }

    /**
     * 确保存储目录存在
     */
    private void ensureStorageDirectoryExists() {
        File dir = new File(photoStoragePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 上传照片
     * @param personId 人员ID
     * @param sourceFile 源文件
     * @param description 照片描述
     * @param isPrimary 是否设为主照片
     * @return 上传后的照片对象
     */
    public Photo uploadPhoto(int personId, File sourceFile, String description, boolean isPrimary) 
            throws IOException {
        
        // 验证文件
        validateFile(sourceFile);

        // 生成新文件名
        String originalName = sourceFile.getName();
        String extension = getFileExtension(originalName);
        String newFileName = generateFileName(personId, extension);

        // 创建人员专属目录
        String personDir = photoStoragePath + File.separator + personId;
        new File(personDir).mkdirs();

        // 复制文件
        Path sourcePath = sourceFile.toPath();
        Path targetPath = Paths.get(personDir, newFileName);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 创建照片记录
        Photo photo = new Photo();
        photo.setPersonId(personId);
        photo.setFilePath(targetPath.toString());
        photo.setFileName(originalName);
        photo.setDescription(description);
        photo.setIsPrimary(isPrimary);
        photo.setUploadTime(LocalDateTime.now());
        photo.setFileSize(Files.size(targetPath));
        photo.setMimeType(getMimeType(extension));

        // 如果设为主照片，先取消其他主照片
        if (isPrimary) {
            clearPrimaryPhoto(personId);
        }

        // 保存到数据库
        int id = photoDao.add(photo);
        photo.setId(id);

        return photo;
    }

    /**
     * 批量上传照片
     */
    public List<Photo> uploadPhotos(int personId, List<File> files, boolean firstAsPrimary) 
            throws IOException {
        
        List<Photo> photos = new ArrayList<>();
        
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            boolean isPrimary = firstAsPrimary && i == 0;
            Photo photo = uploadPhoto(personId, file, null, isPrimary);
            photos.add(photo);
        }
        
        return photos;
    }

    /**
     * 删除照片
     */
    public boolean deletePhoto(int photoId) {
        Photo photo = photoDao.getById(photoId);
        if (photo == null) return false;

        // 删除文件
        try {
            Files.deleteIfExists(Paths.get(photo.getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 删除数据库记录
        return photoDao.delete(photoId);
    }

    /**
     * 删除某人的所有照片
     */
    public boolean deleteAllPhotos(int personId) {
        List<Photo> photos = photoDao.getByPersonId(personId);
        
        // 删除所有文件
        for (Photo photo : photos) {
            try {
                Files.deleteIfExists(Paths.get(photo.getFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 删除人员照片目录
        try {
            Path personDir = Paths.get(photoStoragePath, String.valueOf(personId));
            if (Files.exists(personDir)) {
                Files.walk(personDir)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(path -> {
                        try { Files.delete(path); } catch (IOException ignored) {}
                    });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 删除数据库记录
        return photoDao.deleteByPersonId(personId);
    }

    /**
     * 获取照片
     */
    public Photo getPhoto(int photoId) {
        return photoDao.getById(photoId);
    }

    /**
     * 获取某人的所有照片
     */
    public List<Photo> getPhotosByPerson(int personId) {
        return photoDao.getByPersonId(personId);
    }

    /**
     * 获取照片路径列表
     */
    public List<String> getPhotoPathsByPerson(int personId) {
        return photoDao.getPhotoPathsByPersonId(personId);
    }

    /**
     * 获取主照片（头像）
     */
    public Photo getPrimaryPhoto(int personId) {
        return photoDao.getPrimaryPhoto(personId);
    }

    /**
     * 获取主照片路径
     */
    public String getPrimaryPhotoPath(int personId) {
        Photo photo = getPrimaryPhoto(personId);
        return photo != null ? photo.getFilePath() : null;
    }

    /**
     * 设置主照片
     */
    public boolean setPrimaryPhoto(int personId, int photoId) {
        return photoDao.setPrimaryPhoto(personId, photoId);
    }

    /**
     * 清除主照片标记
     */
    private void clearPrimaryPhoto(int personId) {
        List<Photo> photos = photoDao.getByPersonId(personId);
        for (Photo photo : photos) {
            if (photo.getIsPrimary()) {
                photo.setIsPrimary(false);
                photoDao.update(photo);
            }
        }
    }

    /**
     * 获取照片数量
     */
    public int getPhotoCount(int personId) {
        return photoDao.getPhotoCount(personId);
    }

    /**
     * 更新照片描述
     */
    public boolean updateDescription(int photoId, String description) {
        Photo photo = photoDao.getById(photoId);
        if (photo == null) return false;
        
        photo.setDescription(description);
        return photoDao.update(photo);
    }

    /**
     * 验证文件
     */
    private void validateFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("文件不存在");
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw new IOException("文件大小超过限制（最大10MB）");
        }

        String extension = getFileExtension(file.getName()).toLowerCase();
        if (!SUPPORTED_FORMATS.contains(extension)) {
            throw new IOException("不支持的文件格式，支持: " + String.join(", ", SUPPORTED_FORMATS));
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }

    /**
     * 生成新文件名
     */
    private String generateFileName(int personId, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "photo_" + personId + "_" + timestamp + "_" + uuid + extension;
    }

    /**
     * 获取MIME类型
     */
    private String getMimeType(String extension) {
        return switch (extension.toLowerCase()) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".bmp" -> "image/bmp";
            case ".webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }

    /**
     * 初始化（创建数据库表）
     */
    public static void init() {
        PhotoDao.initTable();
    }
}

