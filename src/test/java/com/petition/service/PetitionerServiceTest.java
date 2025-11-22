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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PetitionerService单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PetitionerServiceTest {
    private static final String TEST_DATA_DIR = "test_service_data";
    private PetitionerService service;
    private JsonDataManager dataManager;

    @BeforeEach
    void setUp() {
        dataManager = new JsonDataManager(TEST_DATA_DIR);
        service = new PetitionerService(dataManager);
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
     * 创建测试用上访人员
     */
    private Petitioner createTestPetitioner(String name, String idCard) {
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName(name);
        personalInfo.setIdCard(idCard);
        personalInfo.setGender(Gender.MALE);
        personalInfo.setEducation(Education.BACHELOR);
        personalInfo.addPhone("13800138000");
        personalInfo.setVisitCount(5);

        BeijingContact beijingContact = new BeijingContact();
        beijingContact.setContactName("张三");

        PetitionCase petitionCase = new PetitionCase();
        petitionCase.setPetitionContent("测试诉求");
        petitionCase.setEntryMethod(EntryMethod.HIGH_SPEED_RAIL);

        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setRiskLevel(RiskLevel.LOW);

        return new Petitioner(personalInfo, beijingContact, petitionCase, riskAssessment);
    }

    @Test
    @Order(1)
    @DisplayName("测试添加上访人员")
    void testAddPetitioner() throws IOException {
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");

        service.addPetitioner(petitioner);

        List<Petitioner> all = service.getAllPetitioners();
        assertEquals(1, all.size());
        assertEquals("张三", all.get(0).getName());
    }

    @Test
    @Order(2)
    @DisplayName("测试添加重复身份证号")
    void testAddDuplicateIdCard() throws IOException {
        Petitioner p1 = createTestPetitioner("张三", "370102199001011234");
        Petitioner p2 = createTestPetitioner("李四", "370102199001011234");

        service.addPetitioner(p1);

        assertThrows(IllegalArgumentException.class, () -> {
            service.addPetitioner(p2);
        });
    }

    @Test
    @Order(3)
    @DisplayName("测试添加空对象")
    void testAddNullPetitioner() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addPetitioner(null);
        });
    }

    @Test
    @Order(4)
    @DisplayName("测试添加缺少必填字段")
    void testAddMissingRequiredFields() {
        Petitioner petitioner = new Petitioner();

        assertThrows(IllegalArgumentException.class, () -> {
            service.addPetitioner(petitioner);
        });
    }

    @Test
    @Order(5)
    @DisplayName("测试添加身份证号格式错误")
    void testAddInvalidIdCard() {
        Petitioner petitioner = createTestPetitioner("张三", "12345");

        assertThrows(IllegalArgumentException.class, () -> {
            service.addPetitioner(petitioner);
        });
    }

    @Test
    @Order(6)
    @DisplayName("测试根据ID获取上访人员")
    void testGetPetitionerById() throws IOException {
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        service.addPetitioner(petitioner);

        Optional<Petitioner> found = service.getPetitionerById(petitioner.getId());

        assertTrue(found.isPresent());
        assertEquals("张三", found.get().getName());
    }

    @Test
    @Order(7)
    @DisplayName("测试更新上访人员")
    void testUpdatePetitioner() throws IOException {
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        service.addPetitioner(petitioner);

        petitioner.getPersonalInfo().setName("张三丰");
        service.updatePetitioner(petitioner);

        Optional<Petitioner> updated = service.getPetitionerById(petitioner.getId());
        assertTrue(updated.isPresent());
        assertEquals("张三丰", updated.get().getName());
    }

    @Test
    @Order(8)
    @DisplayName("测试更新不存在的上访人员")
    void testUpdateNonExistentPetitioner() throws IOException {
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        // 不添加，直接更新

        assertThrows(IllegalArgumentException.class, () -> {
            service.updatePetitioner(petitioner);
        });
    }

    @Test
    @Order(9)
    @DisplayName("测试删除上访人员")
    void testDeletePetitioner() throws IOException {
        Petitioner petitioner = createTestPetitioner("张三", "370102199001011234");
        service.addPetitioner(petitioner);

        assertEquals(1, service.getTotalCount());

        boolean deleted = service.deletePetitioner(petitioner.getId());

        assertTrue(deleted);
        assertEquals(0, service.getTotalCount());
    }

    @Test
    @Order(10)
    @DisplayName("测试批量删除")
    void testBatchDelete() throws IOException {
        Petitioner p1 = createTestPetitioner("张三", "370102199001011234");
        Petitioner p2 = createTestPetitioner("李四", "370102199002021234");
        Petitioner p3 = createTestPetitioner("王五", "370102199003031234");

        service.addPetitioner(p1);
        service.addPetitioner(p2);
        service.addPetitioner(p3);

        assertEquals(3, service.getTotalCount());

        int deleted = service.batchDelete(List.of(p1.getId(), p2.getId()));

        assertEquals(2, deleted);
        assertEquals(1, service.getTotalCount());
    }

    @Test
    @Order(11)
    @DisplayName("测试获取所有上访人员")
    void testGetAllPetitioners() throws IOException {
        service.addPetitioner(createTestPetitioner("张三", "370102199001011234"));
        service.addPetitioner(createTestPetitioner("李四", "370102199002021234"));
        service.addPetitioner(createTestPetitioner("王五", "370102199003031234"));

        List<Petitioner> all = service.getAllPetitioners();

        assertEquals(3, all.size());
    }

    @Test
    @Order(12)
    @DisplayName("测试获取总数")
    void testGetTotalCount() throws IOException {
        assertEquals(0, service.getTotalCount());

        service.addPetitioner(createTestPetitioner("张三", "370102199001011234"));
        assertEquals(1, service.getTotalCount());

        service.addPetitioner(createTestPetitioner("李四", "370102199002021234"));
        assertEquals(2, service.getTotalCount());
    }

    @Test
    @Order(13)
    @DisplayName("测试清空所有数据")
    void testClearAll() throws IOException {
        service.addPetitioner(createTestPetitioner("张三", "370102199001011234"));
        service.addPetitioner(createTestPetitioner("李四", "370102199002021234"));

        assertEquals(2, service.getTotalCount());

        service.clearAll();

        assertEquals(0, service.getTotalCount());
    }

    @Test
    @Order(14)
    @DisplayName("测试刷新数据")
    void testRefresh() throws IOException {
        service.addPetitioner(createTestPetitioner("张三", "370102199001011234"));

        // 直接通过dataManager修改
        dataManager.clear();

        // 刷新service
        service.refresh();

        assertEquals(0, service.getTotalCount());
    }
}
