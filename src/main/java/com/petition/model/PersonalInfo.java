package com.petition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petition.model.enums.Education;
import com.petition.model.enums.Gender;
import com.petition.model.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息实体类
 * 包含上访人员的基本身份信息、联系方式、职业住址、上访相关信息和照片
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class PersonalInfo {
    // ========== 基本身份信息 ==========
    /**
     * 姓名（必填）
     */
    private String name;

    /**
     * 身份证号（必填，18位，唯一标识）
     */
    private String idCard;

    /**
     * 性别（必填）
     */
    private Gender gender;

    /**
     * 籍贯（民族/籍贯信息，如：汉族）
     */
    private String nativePlace;

    /**
     * 学历
     */
    private Education education;

    /**
     * 曾用名
     */
    private String formerName;

    /**
     * 婚姻状态
     */
    private MaritalStatus maritalStatus;

    /**
     * 配偶姓名
     */
    private String spouse;

    // ========== 联系方式 ==========
    /**
     * 联系电话列表（必填，至少一个）
     * 第一个号码为主要联系方式，后续为备用联系方式
     */
    private List<String> phones;

    // ========== 职业与住址 ==========
    /**
     * 职业/收入来源
     */
    private String occupation;

    /**
     * 工作地址
     */
    private String workAddress;

    /**
     * 常住地址
     */
    private String homeAddress;

    // ========== 上访相关信息 ==========
    /**
     * 赴京上访次数
     */
    private Integer visitCount;

    /**
     * 反侦察手段描述
     */
    private String counterMeasures;

    /**
     * 消费住宿习惯描述
     */
    private String consumptionHabits;

    // ========== 个人照片 ==========
    /**
     * 照片文件路径列表（相对路径）
     * 第一张照片作为主照片，用于列表缩略图展示
     * 存储路径：data/photos/petitioners/{身份证号}/
     */
    private List<String> photos;

    /**
     * 默认构造函数
     */
    public PersonalInfo() {
        this.phones = new ArrayList<>();
        this.photos = new ArrayList<>();
    }

    /**
     * 带必填字段的构造函数
     *
     * @param name   姓名
     * @param idCard 身份证号
     * @param gender 性别
     */
    public PersonalInfo(String name, String idCard, Gender gender) {
        this();
        this.name = name;
        this.idCard = idCard;
        this.gender = gender;
    }

    // ========== Getters and Setters ==========

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public String getFormerName() {
        return formerName;
    }

    public void setFormerName(String formerName) {
        this.formerName = formerName;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public String getCounterMeasures() {
        return counterMeasures;
    }

    public void setCounterMeasures(String counterMeasures) {
        this.counterMeasures = counterMeasures;
    }

    public String getConsumptionHabits() {
        return consumptionHabits;
    }

    public void setConsumptionHabits(String consumptionHabits) {
        this.consumptionHabits = consumptionHabits;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    /**
     * 添加联系电话
     *
     * @param phone 电话号码
     */
    public void addPhone(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            this.phones.add(phone);
        }
    }

    /**
     * 添加照片
     *
     * @param photoPath 照片文件路径
     */
    public void addPhoto(String photoPath) {
        if (photoPath != null && !photoPath.trim().isEmpty()) {
            this.photos.add(photoPath);
        }
    }

    /**
     * 获取主要联系电话（第一个电话）
     *
     * @return 主要联系电话，无电话返回null
     */
    @JsonIgnore
    public String getPrimaryPhone() {
        return phones != null && !phones.isEmpty() ? phones.get(0) : null;
    }

    /**
     * 获取主照片（第一张照片）
     *
     * @return 主照片路径，无照片返回null
     */
    @JsonIgnore
    public String getPrimaryPhoto() {
        return photos != null && !phones.isEmpty() ? photos.get(0) : null;
    }

    @Override
    public String toString() {
        return "PersonalInfo{" +
                "name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", gender=" + gender +
                ", phones=" + phones +
                ", visitCount=" + visitCount +
                '}';
    }
}
