package com.petition.dao;

import com.petition.model.Photo;
import com.petition.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 照片数据访问层
 * 提供照片的CRUD操作
 */
public class PhotoDao {

    /**
     * 初始化照片表
     * 在应用启动时调用以确保表存在
     */
    public static void initTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS photos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                person_id INTEGER NOT NULL,
                file_path TEXT NOT NULL,
                file_name TEXT,
                description TEXT,
                is_primary INTEGER DEFAULT 0,
                upload_time TEXT,
                file_size INTEGER,
                mime_type TEXT,
                FOREIGN KEY (person_id) REFERENCES petitioner(id) ON DELETE CASCADE
            )
            """;
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            // 创建索引
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_photos_person ON photos(person_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_photos_primary ON photos(person_id, is_primary)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加照片
     */
    public int add(Photo photo) {
        String sql = """
            INSERT INTO photos (person_id, file_path, file_name, description, 
                               is_primary, upload_time, file_size, mime_type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, photo.getPersonId());
            pstmt.setString(2, photo.getFilePath());
            pstmt.setString(3, photo.getFileName());
            pstmt.setString(4, photo.getDescription());
            pstmt.setInt(5, photo.getIsPrimary() ? 1 : 0);
            pstmt.setString(6, photo.getUploadTime() != null ? photo.getUploadTime().toString() : null);
            pstmt.setObject(7, photo.getFileSize());
            pstmt.setString(8, photo.getMimeType());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新照片信息
     */
    public boolean update(Photo photo) {
        String sql = """
            UPDATE photos SET 
                file_path = ?, file_name = ?, description = ?, 
                is_primary = ?, mime_type = ?
            WHERE id = ?
            """;
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, photo.getFilePath());
            pstmt.setString(2, photo.getFileName());
            pstmt.setString(3, photo.getDescription());
            pstmt.setInt(4, photo.getIsPrimary() ? 1 : 0);
            pstmt.setString(5, photo.getMimeType());
            pstmt.setInt(6, photo.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除照片
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM photos WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除某人的所有照片
     */
    public boolean deleteByPersonId(int personId) {
        String sql = "DELETE FROM photos WHERE person_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取照片详情
     */
    public Photo getById(int id) {
        String sql = "SELECT * FROM photos WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某人的所有照片
     */
    public List<Photo> getByPersonId(int personId) {
        String sql = "SELECT * FROM photos WHERE person_id = ? ORDER BY is_primary DESC, upload_time DESC";
        List<Photo> photos = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                photos.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;
    }

    /**
     * 获取某人的照片路径列表
     */
    public List<String> getPhotoPathsByPersonId(int personId) {
        String sql = "SELECT file_path FROM photos WHERE person_id = ? ORDER BY is_primary DESC, upload_time DESC";
        List<String> paths = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                paths.add(rs.getString("file_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * 获取某人的主照片（头像）
     */
    public Photo getPrimaryPhoto(int personId) {
        String sql = "SELECT * FROM photos WHERE person_id = ? AND is_primary = 1 LIMIT 1";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 如果没有主照片，返回第一张
        return getFirstPhoto(personId);
    }

    /**
     * 获取第一张照片
     */
    public Photo getFirstPhoto(int personId) {
        String sql = "SELECT * FROM photos WHERE person_id = ? ORDER BY upload_time ASC LIMIT 1";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置主照片
     */
    public boolean setPrimaryPhoto(int personId, int photoId) {
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // 先取消该人所有照片的主照片标记
                PreparedStatement pstmt1 = conn.prepareStatement(
                    "UPDATE photos SET is_primary = 0 WHERE person_id = ?");
                pstmt1.setInt(1, personId);
                pstmt1.executeUpdate();
                
                // 设置指定照片为主照片
                PreparedStatement pstmt2 = conn.prepareStatement(
                    "UPDATE photos SET is_primary = 1 WHERE id = ? AND person_id = ?");
                pstmt2.setInt(1, photoId);
                pstmt2.setInt(2, personId);
                int affected = pstmt2.executeUpdate();
                
                conn.commit();
                return affected > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取照片数量
     */
    public int getPhotoCount(int personId) {
        String sql = "SELECT COUNT(*) FROM photos WHERE person_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 批量添加照片
     */
    public boolean addBatch(List<Photo> photos) {
        String sql = """
            INSERT INTO photos (person_id, file_path, file_name, description, 
                               is_primary, upload_time, file_size, mime_type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (Photo photo : photos) {
                pstmt.setInt(1, photo.getPersonId());
                pstmt.setString(2, photo.getFilePath());
                pstmt.setString(3, photo.getFileName());
                pstmt.setString(4, photo.getDescription());
                pstmt.setInt(5, photo.getIsPrimary() ? 1 : 0);
                pstmt.setString(6, photo.getUploadTime() != null ? photo.getUploadTime().toString() : null);
                pstmt.setObject(7, photo.getFileSize());
                pstmt.setString(8, photo.getMimeType());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ResultSet映射到Photo对象
     */
    private Photo mapResultSet(ResultSet rs) throws SQLException {
        Photo photo = new Photo();
        photo.setId(rs.getInt("id"));
        photo.setPersonId(rs.getInt("person_id"));
        photo.setFilePath(rs.getString("file_path"));
        photo.setFileName(rs.getString("file_name"));
        photo.setDescription(rs.getString("description"));
        photo.setIsPrimary(rs.getInt("is_primary") == 1);
        
        String uploadTime = rs.getString("upload_time");
        if (uploadTime != null) {
            photo.setUploadTime(LocalDateTime.parse(uploadTime));
        }
        
        photo.setFileSize(rs.getLong("file_size"));
        photo.setMimeType(rs.getString("mime_type"));
        
        return photo;
    }
}

