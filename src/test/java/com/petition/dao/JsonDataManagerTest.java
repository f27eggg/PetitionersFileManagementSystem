package com.petition.dao;

import com.petition.model.*;
import com.petition.model.enums.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JsonDataManager单元测试
 * 测试JSON数据管理器的各项功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JsonDataManagerTest {
    private static final String TEST_DATA_DIR = "test_data";
    private JsonDataManager dataManager;

    @BeforeEach
    void setUp() {
        // 使用测试专用目录
        dataManager = new JsonDataManager(TEST_DATA_DIR);
    }

    @AfterEach
    void tearDown() throws IOException {
        // 清理测试数据
        Path testDir = Paths.get(TEST_DATA_DIR);
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                 .sorted((a, b) -> b.compareTo(a)) // 逆序删除（先删文件再删目录）
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
     * 创建测试用上访人员对象
     */
    private Petitioner createTestPetitioner(String name, String idCard) {
        // 创建个人信息
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName(name);
        personalInfo.setIdCard(idCard);
        personalInfo.setGender(Gender.MALE);
        personalInfo.setEducation(Education.BACHELOR);
        personalInfo.setMaritalStatus(MaritalStatus.MARRIED);
        personalInfo.addPhone("13800138000");

        // 创建在京关系人
        BeijingContact beijingContact = new BeijingContact();
        beijingContact.setContactName("张三");
        beijingContact.setRelationship("朋友");

        // 创建信访案件
        PetitionCase petitionCase = new PetitionCase();
        petitionCase.setPetitionContent("测试诉求内容");
        petitionCase.setEntryMethod(EntryMethod.HIGH_SPEED_RAIL);
        petitionCase.setTransportInBeijing(TransportMethod.SUBWAY);

        // 创建评估结果
        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setRiskLevel(RiskLevel.LOW);

        // 创建主实体
        return new Petitioner(personalInfo, beijingContact, petitionCase, riskAssessment);
    }

    @Test
    @Order(1)
    @DisplayName("测试保存和加载单个数据")
    void testSaveAndLoadSingle() throws IOException {
        // 创建测试数据
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");

        // 保存数据
        dataManager.save(petitioner);

        // 加载数据
        List<Petitioner> loaded = dataManager.loadAll();

        // 验证
        assertNotNull(loaded);
        assertEquals(1, loaded.size());
        assertEquals("张三", loaded.get(0).getName());
        assertEquals("370102199001011234", loaded.get(0).getIdCard());
    }

    @Test
    @Order(2)
    @DisplayName("测试保存和加载多个数据")
    void testSaveAndLoadMultiple() throws IOException {
        // 创建多个测试数据
        List<Petitioner> petitioners = new ArrayList<>();
        petitioners.add(createTestPetitioner("张三", "370102199001011234"));
        petitioners.add(createTestPetitioner("李四", "370102199002021234"));
        petitioners.add(createTestPetitioner("王五", "370102199003031234"));

        // 保存所有数据
        dataManager.saveAll(petitioners);

        // 加载数据
        List<Petitioner> loaded = dataManager.loadAll();

        // 验证
        assertNotNull(loaded);
        assertEquals(3, loaded.size());
    }

    @Test
    @Order(3)
    @DisplayName("测试按ID查找")
    void testFindById() throws IOException {
        // 创建并保存测试数据
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        dataManager.save(petitioner);

        // 按ID查找
        Optional<Petitioner> found = dataManager.findById(petitioner.getId());

        // 验证
        assertTrue(found.isPresent());
        assertEquals("张三", found.get().getName());
    }

    @Test
    @Order(4)
    @DisplayName("测试查找不存在的ID")
    void testFindByIdNotFound() throws IOException {
        // 查找不存在的ID
        Optional<Petitioner> found = dataManager.findById("non-existent-id");

        // 验证
        assertFalse(found.isPresent());
    }

    @Test
    @Order(5)
    @DisplayName("测试更新数据")
    void testUpdate() throws IOException {
        // 创建并保存测试数据
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        dataManager.save(petitioner);

        // 修改数据
        petitioner.getPersonalInfo().setName("张三丰");
        dataManager.save(petitioner);

        // 加载并验证
        Optional<Petitioner> updated = dataManager.findById(petitioner.getId());
        assertTrue(updated.isPresent());
        assertEquals("张三丰", updated.get().getName());

        // 验证总数没有变化
        assertEquals(1, dataManager.count());
    }

    @Test
    @Order(6)
    @DisplayName("测试删除数据")
    void testDelete() throws IOException {
        // 创建并保存测试数据
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        dataManager.save(petitioner);

        // 验证数据已保存
        assertEquals(1, dataManager.count());

        // 删除数据
        boolean deleted = dataManager.delete(petitioner.getId());

        // 验证
        assertTrue(deleted);
        assertEquals(0, dataManager.count());
    }

    @Test
    @Order(7)
    @DisplayName("测试批量删除")
    void testBatchDelete() throws IOException {
        // 创建并保存多个测试数据
        Petitioner p1 = createTestPetitioner("张三", "370102199001011234");
        Petitioner p2 = createTestPetitioner("李四", "370102199002021234");
        Petitioner p3 = createTestPetitioner("王五", "370102199003031234");

        dataManager.save(p1);
        dataManager.save(p2);
        dataManager.save(p3);

        assertEquals(3, dataManager.count());

        // 批量删除
        List<String> idsToDelete = List.of(p1.getId(), p2.getId());
        int deletedCount = dataManager.batchDelete(idsToDelete);

        // 验证
        assertEquals(2, deletedCount);
        assertEquals(1, dataManager.count());
    }

    @Test
    @Order(8)
    @DisplayName("测试清空所有数据")
    void testClear() throws IOException {
        // 创建并保存测试数据
        dataManager.save(createTestPetitioner("张三", "370102199001011234"));
        dataManager.save(createTestPetitioner("李四", "370102199002021234"));

        assertEquals(2, dataManager.count());

        // 清空数据
        dataManager.clear();

        // 验证
        assertEquals(0, dataManager.count());
    }

    @Test
    @Order(9)
    @DisplayName("测试数据持久化")
    void testDataPersistence() throws IOException {
        // 创建并保存数据
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        dataManager.save(petitioner);

        // 创建新的DataManager实例（模拟程序重启）
        JsonDataManager newDataManager = new JsonDataManager(TEST_DATA_DIR);
        newDataManager.loadAll();

        // 验证数据已持久化
        assertEquals(1, newDataManager.count());
        Optional<Petitioner> loaded = newDataManager.findById(petitioner.getId());
        assertTrue(loaded.isPresent());
        assertEquals("张三", loaded.get().getName());
    }

    @Test
    @Order(10)
    @DisplayName("测试枚举序列化和反序列化")
    void testEnumSerialization() throws IOException {
        // 创建包含各种枚举的测试数据
        Petitioner petitioner = createTestPetitioner("测试", "370102199001011234");
        petitioner.getPersonalInfo().setGender(Gender.FEMALE);
        petitioner.getPersonalInfo().setEducation(Education.MASTER);
        petitioner.getPersonalInfo().setMaritalStatus(MaritalStatus.DIVORCED);
        petitioner.getPetitionCase().setEntryMethod(EntryMethod.AIRPLANE);
        petitioner.getPetitionCase().setTransportInBeijing(TransportMethod.TAXI);
        petitioner.getRiskAssessment().setRiskLevel(RiskLevel.HIGH);

        // 保存数据
        dataManager.save(petitioner);

        // 加载并验证
        Optional<Petitioner> loaded = dataManager.findById(petitioner.getId());
        assertTrue(loaded.isPresent());

        Petitioner loadedPetitioner = loaded.get();
        assertEquals(Gender.FEMALE, loadedPetitioner.getPersonalInfo().getGender());
        assertEquals(Education.MASTER, loadedPetitioner.getPersonalInfo().getEducation());
        assertEquals(MaritalStatus.DIVORCED, loadedPetitioner.getPersonalInfo().getMaritalStatus());
        assertEquals(EntryMethod.AIRPLANE, loadedPetitioner.getPetitionCase().getEntryMethod());
        assertEquals(TransportMethod.TAXI, loadedPetitioner.getPetitionCase().getTransportInBeijing());
        assertEquals(RiskLevel.HIGH, loadedPetitioner.getRiskAssessment().getRiskLevel());
    }

    @Test
    @Order(11)
    @DisplayName("测试LocalDateTime序列化和反序列化")
    void testLocalDateTimeSerialization() throws IOException {
        // 创建并保存数据
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        dataManager.save(petitioner);

        // 记录创建时间
        String originalId = petitioner.getId();
        assertNotNull(petitioner.getCreateTime());
        assertNotNull(petitioner.getUpdateTime());

        // 加载并验证时间字段
        Optional<Petitioner> loaded = dataManager.findById(originalId);
        assertTrue(loaded.isPresent());
        assertNotNull(loaded.get().getCreateTime());
        assertNotNull(loaded.get().getUpdateTime());
    }

    @Test
    @Order(12)
    @DisplayName("测试刷新功能")
    void testRefresh() throws IOException {
        // 保存初始数据
        dataManager.save(createTestPetitioner("张三", "370102199001011234"));
        assertEquals(1, dataManager.count());

        // 直接修改文件（模拟外部修改）
        List<Petitioner> external = new ArrayList<>();
        external.add(createTestPetitioner("李四", "370102199002021234"));
        external.add(createTestPetitioner("王五", "370102199003031234"));
        dataManager.saveAll(external);

        // 刷新数据
        dataManager.refresh();

        // 验证数据已更新
        assertEquals(2, dataManager.count());
    }
}
