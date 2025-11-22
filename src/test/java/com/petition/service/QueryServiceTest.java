package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.*;
import com.petition.model.enums.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * QueryService单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QueryServiceTest {
    private static final String TEST_DATA_DIR = "test_query_data";
    private QueryService queryService;
    private JsonDataManager dataManager;

    @BeforeEach
    void setUp() throws IOException {
        dataManager = new JsonDataManager(TEST_DATA_DIR);
        queryService = new QueryService(dataManager);

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
        // 创建5个测试人员
        dataManager.save(createPetitioner("张三", "370102199001011234", RiskLevel.LOW, 2, "山东"));
        dataManager.save(createPetitioner("李四", "370102199002021234", RiskLevel.HIGH, 5, "山东"));
        dataManager.save(createPetitioner("王五", "370102199003031234", RiskLevel.CRITICAL, 10, "河北"));
        dataManager.save(createPetitioner("赵六", "370102199004041234", RiskLevel.MEDIUM, 3, "河南"));
        dataManager.save(createPetitioner("孙七", "370102199005051234", RiskLevel.LOW, 1, "山东"));
    }

    private Petitioner createPetitioner(String name, String idCard, RiskLevel riskLevel,
                                       int visitCount, String nativePlace) {
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName(name);
        personalInfo.setIdCard(idCard);
        personalInfo.setGender(Gender.MALE);
        personalInfo.addPhone("1380013800" + visitCount);
        personalInfo.setVisitCount(visitCount);
        personalInfo.setNativePlace(nativePlace);

        BeijingContact beijingContact = new BeijingContact();
        PetitionCase petitionCase = new PetitionCase();
        RiskAssessment riskAssessment = new RiskAssessment(riskLevel);

        return new Petitioner(personalInfo, beijingContact, petitionCase, riskAssessment);
    }

    @Test
    @Order(1)
    @DisplayName("测试快速搜索-按姓名")
    void testQuickSearchByName() throws IOException {
        List<Petitioner> results = queryService.quickSearch("张三");

        assertEquals(1, results.size());
        assertEquals("张三", results.get(0).getName());
    }

    @Test
    @Order(2)
    @DisplayName("测试快速搜索-按身份证号")
    void testQuickSearchByIdCard() throws IOException {
        List<Petitioner> results = queryService.quickSearch("199001011234");

        assertEquals(1, results.size());
        assertEquals("张三", results.get(0).getName());
    }

    @Test
    @Order(3)
    @DisplayName("测试快速搜索-按手机号")
    void testQuickSearchByPhone() throws IOException {
        List<Petitioner> results = queryService.quickSearch("13800138005");

        assertEquals(1, results.size());
        assertEquals("李四", results.get(0).getName());
    }

    @Test
    @Order(4)
    @DisplayName("测试快速搜索-模糊匹配")
    void testQuickSearchFuzzy() throws IOException {
        List<Petitioner> results = queryService.quickSearch("3701");

        // 应该匹配所有身份证号包含3701的人员
        assertEquals(5, results.size());
    }

    @Test
    @Order(5)
    @DisplayName("测试快速搜索-空关键词")
    void testQuickSearchEmptyKeyword() throws IOException {
        List<Petitioner> results = queryService.quickSearch("");

        assertEquals(0, results.size());
    }

    @Test
    @Order(6)
    @DisplayName("测试按危险等级筛选")
    void testFilterByRiskLevel() throws IOException {
        List<Petitioner> lowRisk = queryService.filterByRiskLevel(RiskLevel.LOW);
        assertEquals(2, lowRisk.size());

        List<Petitioner> highRisk = queryService.filterByRiskLevel(RiskLevel.HIGH);
        assertEquals(1, highRisk.size());

        List<Petitioner> critical = queryService.filterByRiskLevel(RiskLevel.CRITICAL);
        assertEquals(1, critical.size());
    }

    @Test
    @Order(7)
    @DisplayName("测试按上访次数筛选")
    void testFilterByVisitCount() throws IOException {
        // 1-3次
        List<Petitioner> result1 = queryService.filterByVisitCount(1, 3);
        assertEquals(3, result1.size());

        // 5次以上
        List<Petitioner> result2 = queryService.filterByVisitCount(5, null);
        assertEquals(2, result2.size());

        // 恰好5次
        List<Petitioner> result3 = queryService.filterByVisitCount(5, 5);
        assertEquals(1, result3.size());
    }

    @Test
    @Order(8)
    @DisplayName("测试获取高危人员")
    void testGetHighRiskPetitioners() throws IOException {
        List<Petitioner> highRisk = queryService.getHighRiskPetitioners();

        // 应该包括HIGH和CRITICAL
        assertEquals(2, highRisk.size());
    }

    @Test
    @Order(9)
    @DisplayName("测试按籍贯筛选")
    void testFilterByNativePlace() throws IOException {
        List<Petitioner> shandong = queryService.filterByNativePlace("山东");
        assertEquals(3, shandong.size());

        List<Petitioner> hebei = queryService.filterByNativePlace("河北");
        assertEquals(1, hebei.size());
    }

    @Test
    @Order(10)
    @DisplayName("测试高级查询-单条件")
    void testAdvancedQuerySingleCondition() throws IOException {
        QueryService.QueryCriteria criteria = new QueryService.QueryCriteria();
        criteria.setRiskLevel(RiskLevel.LOW);

        List<Petitioner> results = queryService.advancedQuery(criteria);

        assertEquals(2, results.size());
    }

    @Test
    @Order(11)
    @DisplayName("测试高级查询-多条件组合")
    void testAdvancedQueryMultipleConditions() throws IOException {
        QueryService.QueryCriteria criteria = new QueryService.QueryCriteria();
        criteria.setNativePlace("山东");
        criteria.setMinVisitCount(2);
        criteria.setMaxVisitCount(5);

        List<Petitioner> results = queryService.advancedQuery(criteria);

        // 山东 + 上访2-5次：张三(2次)、李四(5次)
        assertEquals(2, results.size());
    }

    @Test
    @Order(12)
    @DisplayName("测试高级查询-空条件")
    void testAdvancedQueryNullCriteria() throws IOException {
        List<Petitioner> results = queryService.advancedQuery(null);

        // 应该返回所有数据
        assertEquals(5, results.size());
    }

    @Test
    @Order(13)
    @DisplayName("测试高级查询-姓名模糊匹配")
    void testAdvancedQueryNameContains() throws IOException {
        QueryService.QueryCriteria criteria = new QueryService.QueryCriteria();
        criteria.setName("张");

        List<Petitioner> results = queryService.advancedQuery(criteria);

        assertEquals(1, results.size());
        assertEquals("张三", results.get(0).getName());
    }

    @Test
    @Order(14)
    @DisplayName("测试高级查询-身份证号模糊匹配")
    void testAdvancedQueryIdCardContains() throws IOException {
        QueryService.QueryCriteria criteria = new QueryService.QueryCriteria();
        criteria.setIdCard("199001");

        List<Petitioner> results = queryService.advancedQuery(criteria);

        assertEquals(1, results.size());
        assertEquals("张三", results.get(0).getName());
    }
}
