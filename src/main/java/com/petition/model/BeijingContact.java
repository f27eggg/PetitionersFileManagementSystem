package com.petition.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 在京关系人实体类
 * 记录上访人员在北京的社会关系人信息，用于追踪可能的协助网络
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class BeijingContact {
    /**
     * 在京关系人姓名
     */
    private String contactName;

    /**
     * 关系人曾经的居住地址
     */
    private String formerAddress;

    /**
     * 关系人身份证号码（18位）
     */
    private String contactIdCard;

    /**
     * 与上访人的关系（如：亲属/朋友/同乡等）
     */
    private String relationship;

    /**
     * 提供的协助内容说明（长文本）
     */
    private String assistDescription;

    /**
     * 关系人照片文件路径列表（相对路径）
     * 存储路径：data/photos/contacts/{关系人身份证号}/
     * 如无身份证号，使用 {上访人身份证号}_contact_{序号}/ 作为目录名
     */
    private List<String> photos;

    /**
     * 默认构造函数
     */
    public BeijingContact() {
        this.photos = new ArrayList<>();
    }

    /**
     * 带关系人姓名的构造函数
     *
     * @param contactName 关系人姓名
     */
    public BeijingContact(String contactName) {
        this();
        this.contactName = contactName;
    }

    // ========== Getters and Setters ==========

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getFormerAddress() {
        return formerAddress;
    }

    public void setFormerAddress(String formerAddress) {
        this.formerAddress = formerAddress;
    }

    public String getContactIdCard() {
        return contactIdCard;
    }

    public void setContactIdCard(String contactIdCard) {
        this.contactIdCard = contactIdCard;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAssistDescription() {
        return assistDescription;
    }

    public void setAssistDescription(String assistDescription) {
        this.assistDescription = assistDescription;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    /**
     * 添加关系人照片
     *
     * @param photoPath 照片文件路径
     */
    public void addPhoto(String photoPath) {
        if (photoPath != null && !photoPath.trim().isEmpty()) {
            this.photos.add(photoPath);
        }
    }

    /**
     * 获取主照片（第一张照片）
     *
     * @return 主照片路径，无照片返回null
     */
    public String getPrimaryPhoto() {
        return photos != null && !photos.isEmpty() ? photos.get(0) : null;
    }

    @Override
    public String toString() {
        return "BeijingContact{" +
                "contactName='" + contactName + '\'' +
                ", contactIdCard='" + contactIdCard + '\'' +
                ", relationship='" + relationship + '\'' +
                '}';
    }
}
