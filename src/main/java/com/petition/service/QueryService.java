package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.Petitioner;
import com.petition.model.enums.RiskLevel;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询服务
 * 提供上访人员的各种查询和筛选功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class QueryService {
    /**
     * 数据管理器
     */
    private final JsonDataManager dataManager;

    /**
     * 默认构造函数
     */
    public QueryService() {
        this.dataManager = new JsonDataManager();
    }

    /**
     * 带数据目录的构造函数
     *
     * @param dataDirectory 数据目录路径
     */
    public QueryService(String dataDirectory) {
        this.dataManager = new JsonDataManager(dataDirectory);
    }

    /**
     * 带数据管理器的构造函数
     *
     * @param dataManager 数据管理器实例
     */
    public QueryService(JsonDataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 快速搜索
     * 支持按姓名、身份证号、手机号进行模糊搜索
     *
     * @param keyword 搜索关键词
     * @return 匹配的上访人员列表
     * @throws IOException 数据读取异常
     */
    public List<Petitioner> quickSearch(String keyword) throws IOException {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        String searchKey = keyword.trim().toLowerCase();
        List<Petitioner> allData = dataManager.loadAll();

        return allData.stream()
                .filter(p -> matchesKeyword(p, searchKey))
                .collect(Collectors.toList());
    }

    /**
     * 判断上访人员是否匹配搜索关键词
     *
     * @param petitioner 上访人员
     * @param keyword 关键词（小写）
     * @return 是否匹配
     */
    private boolean matchesKeyword(Petitioner petitioner, String keyword) {
        // 搜索姓名
        if (petitioner.getName() != null &&
            petitioner.getName().toLowerCase().contains(keyword)) {
            return true;
        }

        // 搜索身份证号
        if (petitioner.getIdCard() != null &&
            petitioner.getIdCard().toLowerCase().contains(keyword)) {
            return true;
        }

        // 搜索手机号
        if (petitioner.getPersonalInfo() != null &&
            petitioner.getPersonalInfo().getPhones() != null) {
            for (String phone : petitioner.getPersonalInfo().getPhones()) {
                if (phone != null && phone.contains(keyword)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 高级查询
     * 支持多条件组合查询
     *
     * @param criteria 查询条件
     * @return 匹配的上访人员列表
     * @throws IOException 数据读取异常
     */
    public List<Petitioner> advancedQuery(QueryCriteria criteria) throws IOException {
        if (criteria == null) {
            return dataManager.loadAll();
        }

        List<Petitioner> allData = dataManager.loadAll();
        return allData.stream()
                .filter(criteria::matches)
                .collect(Collectors.toList());
    }

    /**
     * 按危险等级筛选
     *
     * @param riskLevel 危险等级
     * @return 匹配的上访人员列表
     * @throws IOException 数据读取异常
     */
    public List<Petitioner> filterByRiskLevel(RiskLevel riskLevel) throws IOException {
        if (riskLevel == null) {
            return dataManager.loadAll();
        }

        List<Petitioner> allData = dataManager.loadAll();
        return allData.stream()
                .filter(p -> p.getRiskAssessment() != null &&
                            riskLevel.equals(p.getRiskAssessment().getRiskLevel()))
                .collect(Collectors.toList());
    }

    /**
     * 按上访次数筛选
     *
     * @param minCount 最小上访次数
     * @param maxCount 最大上访次数（null表示不限）
     * @return 匹配的上访人员列表
     * @throws IOException 数据读取异常
     */
    public List<Petitioner> filterByVisitCount(int minCount, Integer maxCount) throws IOException {
        List<Petitioner> allData = dataManager.loadAll();

        return allData.stream()
                .filter(p -> {
                    if (p.getPersonalInfo() == null ||
                        p.getPersonalInfo().getVisitCount() == null) {
                        return false;
                    }

                    int count = p.getPersonalInfo().getVisitCount();
                    if (count < minCount) {
                        return false;
                    }

                    return maxCount == null || count <= maxCount;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取高危人员列表
     * 包括高危和极高危人员
     *
     * @return 高危人员列表
     * @throws IOException 数据读取异常
     */
    public List<Petitioner> getHighRiskPetitioners() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();

        return allData.stream()
                .filter(Petitioner::isHighRisk)
                .collect(Collectors.toList());
    }

    /**
     * 按籍贯筛选
     *
     * @param nativePlace 籍贯
     * @return 匹配的上访人员列表
     * @throws IOException 数据读取异常
     */
    public List<Petitioner> filterByNativePlace(String nativePlace) throws IOException {
        if (nativePlace == null || nativePlace.isBlank()) {
            return List.of();
        }

        List<Petitioner> allData = dataManager.loadAll();
        String searchPlace = nativePlace.trim();

        return allData.stream()
                .filter(p -> p.getPersonalInfo() != null &&
                            searchPlace.equals(p.getPersonalInfo().getNativePlace()))
                .collect(Collectors.toList());
    }

    /**
     * 查询条件类
     * 用于高级查询的条件封装
     */
    public static class QueryCriteria {
        private String name;
        private String idCard;
        private RiskLevel riskLevel;
        private Integer minVisitCount;
        private Integer maxVisitCount;
        private String nativePlace;

        public QueryCriteria() {
        }

        /**
         * 判断上访人员是否匹配条件
         *
         * @param petitioner 上访人员
         * @return 是否匹配
         */
        public boolean matches(Petitioner petitioner) {
            // 姓名匹配
            if (name != null && !name.isBlank()) {
                if (petitioner.getName() == null ||
                    !petitioner.getName().contains(name.trim())) {
                    return false;
                }
            }

            // 身份证号匹配
            if (idCard != null && !idCard.isBlank()) {
                if (petitioner.getIdCard() == null ||
                    !petitioner.getIdCard().contains(idCard.trim())) {
                    return false;
                }
            }

            // 危险等级匹配
            if (riskLevel != null) {
                if (petitioner.getRiskAssessment() == null ||
                    !riskLevel.equals(petitioner.getRiskAssessment().getRiskLevel())) {
                    return false;
                }
            }

            // 上访次数匹配
            if (minVisitCount != null || maxVisitCount != null) {
                if (petitioner.getPersonalInfo() == null ||
                    petitioner.getPersonalInfo().getVisitCount() == null) {
                    return false;
                }

                int count = petitioner.getPersonalInfo().getVisitCount();
                if (minVisitCount != null && count < minVisitCount) {
                    return false;
                }
                if (maxVisitCount != null && count > maxVisitCount) {
                    return false;
                }
            }

            // 籍贯匹配
            if (nativePlace != null && !nativePlace.isBlank()) {
                if (petitioner.getPersonalInfo() == null ||
                    !nativePlace.trim().equals(petitioner.getPersonalInfo().getNativePlace())) {
                    return false;
                }
            }

            return true;
        }

        // Getters and Setters

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

        public RiskLevel getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(RiskLevel riskLevel) {
            this.riskLevel = riskLevel;
        }

        public Integer getMinVisitCount() {
            return minVisitCount;
        }

        public void setMinVisitCount(Integer minVisitCount) {
            this.minVisitCount = minVisitCount;
        }

        public Integer getMaxVisitCount() {
            return maxVisitCount;
        }

        public void setMaxVisitCount(Integer maxVisitCount) {
            this.maxVisitCount = maxVisitCount;
        }

        public String getNativePlace() {
            return nativePlace;
        }

        public void setNativePlace(String nativePlace) {
            this.nativePlace = nativePlace;
        }
    }
}
