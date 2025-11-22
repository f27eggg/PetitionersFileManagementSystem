package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.Petitioner;
import com.petition.model.enums.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计服务
 * 提供各种统计分析功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class StatisticsService {
    /**
     * 数据管理器
     */
    private final JsonDataManager dataManager;

    /**
     * 默认构造函数
     */
    public StatisticsService() {
        this.dataManager = new JsonDataManager();
    }

    /**
     * 带数据目录的构造函数
     *
     * @param dataDirectory 数据目录路径
     */
    public StatisticsService(String dataDirectory) {
        this.dataManager = new JsonDataManager(dataDirectory);
    }

    /**
     * 带数据管理器的构造函数
     *
     * @param dataManager 数据管理器实例
     */
    public StatisticsService(JsonDataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 获取上访人员总数
     *
     * @return 总人数
     */
    public int getTotalCount() {
        return dataManager.count();
    }

    /**
     * 获取危险等级分布
     *
     * @return 危险等级分布Map（等级 -> 人数）
     * @throws IOException 数据读取异常
     */
    public Map<RiskLevel, Integer> getRiskLevelDistribution() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        Map<RiskLevel, Integer> distribution = new HashMap<>();

        // 初始化所有等级为0
        for (RiskLevel level : RiskLevel.values()) {
            distribution.put(level, 0);
        }

        // 统计每个等级的人数
        for (Petitioner p : allData) {
            if (p.getRiskAssessment() != null &&
                p.getRiskAssessment().getRiskLevel() != null) {
                RiskLevel level = p.getRiskAssessment().getRiskLevel();
                distribution.put(level, distribution.get(level) + 1);
            }
        }

        return distribution;
    }

    /**
     * 获取上访次数分布
     * 按区间统计：0次、1-2次、3-5次、6-10次、10次以上
     *
     * @return 上访次数分布Map（区间描述 -> 人数）
     * @throws IOException 数据读取异常
     */
    public Map<String, Integer> getVisitCountDistribution() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        Map<String, Integer> distribution = new HashMap<>();

        // 初始化区间
        distribution.put("0次", 0);
        distribution.put("1-2次", 0);
        distribution.put("3-5次", 0);
        distribution.put("6-10次", 0);
        distribution.put("10次以上", 0);

        // 统计
        for (Petitioner p : allData) {
            if (p.getPersonalInfo() != null &&
                p.getPersonalInfo().getVisitCount() != null) {
                int count = p.getPersonalInfo().getVisitCount();

                if (count == 0) {
                    distribution.put("0次", distribution.get("0次") + 1);
                } else if (count <= 2) {
                    distribution.put("1-2次", distribution.get("1-2次") + 1);
                } else if (count <= 5) {
                    distribution.put("3-5次", distribution.get("3-5次") + 1);
                } else if (count <= 10) {
                    distribution.put("6-10次", distribution.get("6-10次") + 1);
                } else {
                    distribution.put("10次以上", distribution.get("10次以上") + 1);
                }
            }
        }

        return distribution;
    }

    /**
     * 获取籍贯分布
     *
     * @return 籍贯分布Map（籍贯 -> 人数）
     * @throws IOException 数据读取异常
     */
    public Map<String, Integer> getNativePlaceDistribution() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        Map<String, Integer> distribution = new HashMap<>();

        for (Petitioner p : allData) {
            if (p.getPersonalInfo() != null &&
                p.getPersonalInfo().getNativePlace() != null) {
                String nativePlace = p.getPersonalInfo().getNativePlace();
                distribution.put(nativePlace, distribution.getOrDefault(nativePlace, 0) + 1);
            }
        }

        return distribution;
    }

    /**
     * 获取进京方式分布
     *
     * @return 进京方式分布Map（进京方式 -> 人数）
     * @throws IOException 数据读取异常
     */
    public Map<EntryMethod, Integer> getEntryMethodDistribution() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        Map<EntryMethod, Integer> distribution = new HashMap<>();

        // 初始化所有方式为0
        for (EntryMethod method : EntryMethod.values()) {
            distribution.put(method, 0);
        }

        // 统计
        for (Petitioner p : allData) {
            if (p.getPetitionCase() != null &&
                p.getPetitionCase().getEntryMethod() != null) {
                EntryMethod method = p.getPetitionCase().getEntryMethod();
                distribution.put(method, distribution.get(method) + 1);
            }
        }

        return distribution;
    }

    /**
     * 获取学历分布
     *
     * @return 学历分布Map（学历 -> 人数）
     * @throws IOException 数据读取异常
     */
    public Map<Education, Integer> getEducationDistribution() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        Map<Education, Integer> distribution = new HashMap<>();

        // 初始化所有学历为0
        for (Education edu : Education.values()) {
            distribution.put(edu, 0);
        }

        // 统计
        for (Petitioner p : allData) {
            if (p.getPersonalInfo() != null &&
                p.getPersonalInfo().getEducation() != null) {
                Education edu = p.getPersonalInfo().getEducation();
                distribution.put(edu, distribution.get(edu) + 1);
            }
        }

        return distribution;
    }

    /**
     * 获取性别分布
     *
     * @return 性别分布Map（性别 -> 人数）
     * @throws IOException 数据读取异常
     */
    public Map<Gender, Integer> getGenderDistribution() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        Map<Gender, Integer> distribution = new HashMap<>();

        // 初始化
        for (Gender gender : Gender.values()) {
            distribution.put(gender, 0);
        }

        // 统计
        for (Petitioner p : allData) {
            if (p.getPersonalInfo() != null &&
                p.getPersonalInfo().getGender() != null) {
                Gender gender = p.getPersonalInfo().getGender();
                distribution.put(gender, distribution.get(gender) + 1);
            }
        }

        return distribution;
    }

    /**
     * 获取婚姻状态分布
     *
     * @return 婚姻状态分布Map（婚姻状态 -> 人数）
     * @throws IOException 数据读取异常
     */
    public Map<MaritalStatus, Integer> getMaritalStatusDistribution() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        Map<MaritalStatus, Integer> distribution = new HashMap<>();

        // 初始化
        for (MaritalStatus status : MaritalStatus.values()) {
            distribution.put(status, 0);
        }

        // 统计
        for (Petitioner p : allData) {
            if (p.getPersonalInfo() != null &&
                p.getPersonalInfo().getMaritalStatus() != null) {
                MaritalStatus status = p.getPersonalInfo().getMaritalStatus();
                distribution.put(status, distribution.get(status) + 1);
            }
        }

        return distribution;
    }

    /**
     * 获取高危人员数量
     *
     * @return 高危人员数量（包括高危和极高危）
     * @throws IOException 数据读取异常
     */
    public int getHighRiskCount() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();

        return (int) allData.stream()
                .filter(Petitioner::isHighRisk)
                .count();
    }

    /**
     * 获取统计摘要
     * 返回包含各项统计数据的摘要信息
     *
     * @return 统计摘要Map
     * @throws IOException 数据读取异常
     */
    public Map<String, Object> getStatisticsSummary() throws IOException {
        Map<String, Object> summary = new HashMap<>();

        summary.put("totalCount", getTotalCount());
        summary.put("highRiskCount", getHighRiskCount());
        summary.put("riskLevelDistribution", getRiskLevelDistribution());
        summary.put("visitCountDistribution", getVisitCountDistribution());
        summary.put("genderDistribution", getGenderDistribution());

        return summary;
    }
}
