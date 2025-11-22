package com.petition.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 上访人员主实体类
 * 聚合个人信息、在京关系人、信访案件、评估结果四大模块
 *
 * @author 济南市公安局历下分局刑侦大队
 * @version 1.0.0
 */
public class Petitioner {
    /**
     * 唯一标识符（UUID）
     * 系统内部生成的技术主键
     */
    private String id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 个人信息模块（17个字段）
     * 包含基本身份信息、联系方式、职业住址、上访相关信息和照片
     */
    private PersonalInfo personalInfo;

    /**
     * 在京关系人模块（6个字段）
     * 记录上访人员在北京的社会关系人信息
     */
    private BeijingContact beijingContact;

    /**
     * 信访案件模块（7个字段）
     * 记录上访人员的诉求及上访行为相关信息
     */
    private PetitionCase petitionCase;

    /**
     * 评估结果模块（1个字段）
     * 对上访人员的综合风险评估结果
     */
    private RiskAssessment riskAssessment;

    /**
     * 默认构造函数
     * 自动生成UUID和创建时间
     */
    public Petitioner() {
        this.id = UUID.randomUUID().toString();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.personalInfo = new PersonalInfo();
        this.beijingContact = new BeijingContact();
        this.petitionCase = new PetitionCase();
        this.riskAssessment = new RiskAssessment();
    }

    /**
     * 带个人信息的构造函数
     *
     * @param personalInfo 个人信息
     */
    public Petitioner(PersonalInfo personalInfo) {
        this();
        this.personalInfo = personalInfo;
    }

    /**
     * 完整构造函数
     *
     * @param personalInfo    个人信息
     * @param beijingContact  在京关系人
     * @param petitionCase    信访案件
     * @param riskAssessment  评估结果
     */
    public Petitioner(PersonalInfo personalInfo, BeijingContact beijingContact,
                      PetitionCase petitionCase, RiskAssessment riskAssessment) {
        this();
        this.personalInfo = personalInfo;
        this.beijingContact = beijingContact;
        this.petitionCase = petitionCase;
        this.riskAssessment = riskAssessment;
    }

    // ========== Getters and Setters ==========

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public BeijingContact getBeijingContact() {
        return beijingContact;
    }

    public void setBeijingContact(BeijingContact beijingContact) {
        this.beijingContact = beijingContact;
    }

    public PetitionCase getPetitionCase() {
        return petitionCase;
    }

    public void setPetitionCase(PetitionCase petitionCase) {
        this.petitionCase = petitionCase;
    }

    public RiskAssessment getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(RiskAssessment riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    // ========== 业务方法 ==========

    /**
     * 更新修改时间为当前时间
     */
    public void touch() {
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 获取上访人员姓名（快捷方法）
     *
     * @return 姓名，personalInfo为null时返回null
     */
    public String getName() {
        return personalInfo != null ? personalInfo.getName() : null;
    }

    /**
     * 获取上访人员身份证号（快捷方法）
     *
     * @return 身份证号，personalInfo为null时返回null
     */
    public String getIdCard() {
        return personalInfo != null ? personalInfo.getIdCard() : null;
    }

    /**
     * 判断是否为高危或极高危人员（快捷方法）
     *
     * @return true表示高危或极高危
     */
    public boolean isHighRisk() {
        return riskAssessment != null && riskAssessment.isHighRisk();
    }

    @Override
    public String toString() {
        return "Petitioner{" +
                "id='" + id + '\'' +
                ", name='" + getName() + '\'' +
                ", idCard='" + getIdCard() + '\'' +
                ", riskLevel=" + (riskAssessment != null ? riskAssessment.getRiskLevel() : null) +
                ", createTime=" + createTime +
                '}';
    }
}
