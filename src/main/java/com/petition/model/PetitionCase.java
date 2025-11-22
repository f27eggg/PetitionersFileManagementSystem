package com.petition.model;

import com.petition.model.enums.EntryMethod;
import com.petition.model.enums.TransportMethod;

/**
 * 信访案件实体类
 * 记录上访人员的诉求及上访行为相关信息
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class PetitionCase {
    /**
     * 诉求描述（长文本）
     * 上访的具体诉求内容
     */
    private String petitionContent;

    /**
     * 是否能解决
     * 可选值：是/否/部分可以
     */
    private String canResolve;

    /**
     * 解决方式
     * 预计或已采取的解决方式说明
     */
    private String resolutionMethod;

    /**
     * 信访人上访轨迹（长文本）
     * 历次上访的时间地点记录
     */
    private String visitTrajectory;

    /**
     * 进京方式
     * 上访人员进京的交通方式
     */
    private EntryMethod entryMethod;

    /**
     * 在京通行方式
     * 上访人员在北京的日常交通方式
     */
    private TransportMethod transportInBeijing;

    /**
     * 有无接应人
     * 可选值：有/无/不详
     */
    private String hasReception;

    /**
     * 默认构造函数
     */
    public PetitionCase() {
    }

    /**
     * 带诉求内容的构造函数
     *
     * @param petitionContent 诉求描述
     */
    public PetitionCase(String petitionContent) {
        this.petitionContent = petitionContent;
    }

    // ========== Getters and Setters ==========

    public String getPetitionContent() {
        return petitionContent;
    }

    public void setPetitionContent(String petitionContent) {
        this.petitionContent = petitionContent;
    }

    public String getCanResolve() {
        return canResolve;
    }

    public void setCanResolve(String canResolve) {
        this.canResolve = canResolve;
    }

    public String getResolutionMethod() {
        return resolutionMethod;
    }

    public void setResolutionMethod(String resolutionMethod) {
        this.resolutionMethod = resolutionMethod;
    }

    public String getVisitTrajectory() {
        return visitTrajectory;
    }

    public void setVisitTrajectory(String visitTrajectory) {
        this.visitTrajectory = visitTrajectory;
    }

    public EntryMethod getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(EntryMethod entryMethod) {
        this.entryMethod = entryMethod;
    }

    public TransportMethod getTransportInBeijing() {
        return transportInBeijing;
    }

    public void setTransportInBeijing(TransportMethod transportInBeijing) {
        this.transportInBeijing = transportInBeijing;
    }

    public String getHasReception() {
        return hasReception;
    }

    public void setHasReception(String hasReception) {
        this.hasReception = hasReception;
    }

    @Override
    public String toString() {
        return "PetitionCase{" +
                "petitionContent='" + petitionContent + '\'' +
                ", canResolve='" + canResolve + '\'' +
                ", entryMethod=" + entryMethod +
                ", transportInBeijing=" + transportInBeijing +
                '}';
    }
}
