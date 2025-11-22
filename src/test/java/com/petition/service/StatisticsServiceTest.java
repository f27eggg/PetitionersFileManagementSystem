package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.*;
import com.petition.model.enums.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StatisticsService单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StatisticsServiceTest {
    private static final String TEST_DATA_DIR = "test_statistics_data";
    private StatisticsService statisticsService;
    private JsonDataManager dataManager;

    @BeforeEach
    void setUp() throws IOException {
        dataManager = new JsonDataManager(TEST_DATA_DIR);
        statisticsService = new StatisticsService(dataManager);

        // 准备测试数据
        prepareTestData();
    }

    @AfterEach
    void tearDown() throws IOException {
        // 清理测试数据
        Path testDir = Paths.get(TEST_DATA_DIR);
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                 .sorted((a, b) -> b.compareTo(a))
                 .forEach(path -> {
                     try {
                         Files.deleteIfExists(path);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 });
        }
    }

    /**
     * 准备测试数据
     */
    private void prepareTestData() throws IOException {
        // 创建10个测试人员，涵盖各种情况
        dataManager.save(createPetitioner("张三", Gender.MALE, RiskLevel.LOW, 0, "山东", Education.BACHELOR, MaritalStatus.MARRIED, EntryMethod.HIGH_SPEED_RAIL));
        dataManager.save(createPetitioner("李四", Gender.FEMALE, RiskLevel.LOW, 2, "山东", Education.BACHELOR, MaritalStatus.UNMARRIED, EntryMethod.TRAIN));
        dataManager.save(createPetitioner("王五", Gender.MALE, RiskLevel.MEDIUM, 3, "河北", Education.SENIOR_HIGH, MaritalStatus.MARRIED, EntryMethod.AIRPLANE));
        dataManager.save(createPetitioner("赵六", Gender.MALE, RiskLevel.MEDIUM, 5, "河北", Education.MASTER, MaritalStatus.DIVORCED, EntryMethod.SELF_DRIVING));
        dataManager.save(createPetitioner("孙七", Gender.FEMALE, RiskLevel.HIGH, 7, "河南", Education.DOCTOR, MaritalStatus.WIDOWED, EntryMethod.LONG_DISTANCE_BUS));
        dataManager.save(createPetitioner("周八", Gender.MALE, RiskLevel.HIGH, 10, "山东", Education.JUNIOR_HIGH, MaritalStatus.MARRIED, EntryMethod.HIGH_SPEED_RAIL));
        dataManager.save(createPetitioner("吴九", Gender.FEMALE, RiskLevel.CRITICAL, 15, "河北", Education.PRIMARY_SCHOOL, MaritalStatus.UNMARRIED, EntryMethod.TRAIN));
        dataManager.save(createPetitioner("郑十", Gender.MALE, RiskLevel.CRITICAL, 20, "河南", Education.BACHELOR, MaritalStatus.MARRIED, EntryMethod.AIRPLANE));
        dataManager.save(createPetitioner("钱十一", Gender.MALE, RiskLevel.LOW, 1, "山东", Education.BACHELOR, MaritalStatus.MARRIED, EntryMethod.SELF_DRIVING));
        dataManager.save(createPetitioner("陈十二", Gender.FEMALE, RiskLevel.MEDIUM, 4, "河南", Education.SENIOR_HIGH, MaritalStatus.DIVORCED, EntryMethod.HIGH_SPEED_RAIL));
    }

    private Petitioner createPetitioner(String name, Gender gender, RiskLevel riskLevel,
                                       int visitCount, String nativePlace, Education education,
                                       MaritalStatus maritalStatus, EntryMethod entryMethod) {
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName(name);
        personalInfo.setIdCard("37010219900101" + String.format("%04d", visitCount));
        personalInfo.setGender(gender);
        personalInfo.setEducation(education);
        personalInfo.setMaritalStatus(maritalStatus);
        personalInfo.setVisitCount(visitCount);
        personalInfo.setNativePlace(nativePlace);

        BeijingContact beijingContact = new BeijingContact();

        PetitionCase petitionCase = new PetitionCase();
        petitionCase.setEntryMethod(entryMethod);

        RiskAssessment riskAssessment = new RiskAssessment(riskLevel);

        return new Petitioner(personalInfo, beijingContact, petitionCase, riskAssessment);
    }

    @Test
    @Order(1)
    @DisplayName("测试获取总数")
    void testGetTotalCount() {
        int count = statisticsService.getTotalCount();
        assertEquals(10, count);
    }

    @Test
    @Order(2)
    @DisplayName("测试危险等级分布")
    void testGetRiskLevelDistribution() throws IOException {
        Map<RiskLevel, Integer> distribution = statisticsService.getRiskLevelDistribution();

        assertEquals(3, distribution.get(RiskLevel.LOW));
        assertEquals(3, distribution.get(RiskLevel.MEDIUM));
        assertEquals(2, distribution.get(RiskLevel.HIGH));
        assertEquals(2, distribution.get(RiskLevel.CRITICAL));
    }

    @Test
    @Order(3)
    @DisplayName("测试上访次数分布")
    void testGetVisitCountDistribution() throws IOException {
        Map<String, Integer> distribution = statisticsService.getVisitCountDistribution();

        assertEquals(1, distribution.get("0次"));      // 0次
        assertEquals(2, distribution.get("1-2次"));    // 1, 2次
        assertEquals(3, distribution.get("3-5次"));    // 3, 4, 5次
        assertEquals(2, distribution.get("6-10次"));   // 7, 10次
        assertEquals(2, distribution.get("10次以上"));  // 15, 20次
    }

    @Test
    @Order(4)
    @DisplayName("测试籍贯分布")
    void testGetNativePlaceDistribution() throws IOException {
        Map<String, Integer> distribution = statisticsService.getNativePlaceDistribution();

        assertEquals(4, distribution.get("山东"));
        assertEquals(3, distribution.get("河北"));
        assertEquals(3, distribution.get("河南"));
    }

    @Test
    @Order(5)
    @DisplayName("测试进京方式分布")
    void testGetEntryMethodDistribution() throws IOException {
        Map<EntryMethod, Integer> distribution = statisticsService.getEntryMethodDistribution();

        assertEquals(3, distribution.get(EntryMethod.HIGH_SPEED_RAIL));
        assertEquals(2, distribution.get(EntryMethod.TRAIN));
        assertEquals(2, distribution.get(EntryMethod.AIRPLANE));
        assertEquals(2, distribution.get(EntryMethod.SELF_DRIVING));
        assertEquals(1, distribution.get(EntryMethod.LONG_DISTANCE_BUS));
        assertEquals(0, distribution.get(EntryMethod.OTHER));
    }

    @Test
    @Order(6)
    @DisplayName("测试学历分布")
    void testGetEducationDistribution() throws IOException {
        Map<Education, Integer> distribution = statisticsService.getEducationDistribution();

        assertEquals(1, distribution.get(Education.PRIMARY_SCHOOL));
        assertEquals(1, distribution.get(Education.JUNIOR_HIGH));
        assertEquals(2, distribution.get(Education.SENIOR_HIGH));
        assertEquals(4, distribution.get(Education.BACHELOR));
        assertEquals(1, distribution.get(Education.MASTER));
        assertEquals(1, distribution.get(Education.DOCTOR));
        assertEquals(0, distribution.get(Education.JUNIOR_COLLEGE));
    }

    @Test
    @Order(7)
    @DisplayName("测试性别分布")
    void testGetGenderDistribution() throws IOException {
        Map<Gender, Integer> distribution = statisticsService.getGenderDistribution();

        assertEquals(6, distribution.get(Gender.MALE));
        assertEquals(4, distribution.get(Gender.FEMALE));
    }

    @Test
    @Order(8)
    @DisplayName("测试婚姻状态分布")
    void testGetMaritalStatusDistribution() throws IOException {
        Map<MaritalStatus, Integer> distribution = statisticsService.getMaritalStatusDistribution();

        assertEquals(5, distribution.get(MaritalStatus.MARRIED));
        assertEquals(2, distribution.get(MaritalStatus.UNMARRIED));
        assertEquals(2, distribution.get(MaritalStatus.DIVORCED));
        assertEquals(1, distribution.get(MaritalStatus.WIDOWED));
    }

    @Test
    @Order(9)
    @DisplayName("测试获取高危人员数量")
    void testGetHighRiskCount() throws IOException {
        int count = statisticsService.getHighRiskCount();

        // HIGH(2个) + CRITICAL(2个) = 4个
        assertEquals(4, count);
    }

    @Test
    @Order(10)
    @DisplayName("测试统计摘要")
    void testGetStatisticsSummary() throws IOException {
        Map<String, Object> summary = statisticsService.getStatisticsSummary();

        assertNotNull(summary);
        assertEquals(10, summary.get("totalCount"));
        assertEquals(4, summary.get("highRiskCount"));
        assertNotNull(summary.get("riskLevelDistribution"));
        assertNotNull(summary.get("visitCountDistribution"));
        assertNotNull(summary.get("genderDistribution"));
    }
}
