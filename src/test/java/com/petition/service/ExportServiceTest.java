package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.*;
import com.petition.model.enums.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ExportService单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExportServiceTest {
    private static final String TEST_DATA_DIR = "test_export_data";
    private static final String TEST_EXCEL_FILE = "test_export.xlsx";
    private static final String TEST_CSV_FILE = "test_export.csv";
    private ExportService exportService;
    private JsonDataManager dataManager;

    @BeforeEach
    void setUp() throws IOException {
        dataManager = new JsonDataManager(TEST_DATA_DIR);
        exportService = new ExportService(TEST_DATA_DIR);

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

        // 清理导出文件
        Files.deleteIfExists(Paths.get(TEST_EXCEL_FILE));
        Files.deleteIfExists(Paths.get(TEST_CSV_FILE));
    }

    /**
     * 准备测试数据
     */
    private void prepareTestData() throws IOException {
        for (int i = 1; i <= 5; i++) {
            dataManager.save(createTestPetitioner("张三" + i, "37010219900101" + String.format("%04d", i), i));
        }
    }

    private Petitioner createTestPetitioner(String name, String idCard, int visitCount) {
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName(name);
        personalInfo.setIdCard(idCard);
        personalInfo.setGender(Gender.MALE);
        personalInfo.setNativePlace("山东");
        personalInfo.setEducation(Education.BACHELOR);
        personalInfo.setMaritalStatus(MaritalStatus.MARRIED);
        personalInfo.addPhone("1380013800" + visitCount);
        personalInfo.setOccupation("工人");
        personalInfo.setHomeAddress("济南市历下区");
        personalInfo.setVisitCount(visitCount);

        BeijingContact beijingContact = new BeijingContact();
        beijingContact.setContactName("李四");
        beijingContact.setRelationship("朋友");

        PetitionCase petitionCase = new PetitionCase();
        petitionCase.setPetitionContent("测试诉求");
        petitionCase.setEntryMethod(EntryMethod.HIGH_SPEED_RAIL);
        petitionCase.setTransportInBeijing(TransportMethod.SUBWAY);

        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setRiskLevel(RiskLevel.LOW);

        return new Petitioner(personalInfo, beijingContact, petitionCase, riskAssessment);
    }

    @Test
    @Order(1)
    @DisplayName("测试导出所有数据到Excel")
    void testExportAllToExcel() throws IOException {
        int count = exportService.exportToExcel(TEST_EXCEL_FILE);

        assertEquals(5, count);
        assertTrue(Files.exists(Paths.get(TEST_EXCEL_FILE)));

        // 验证Excel文件内容
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(TEST_EXCEL_FILE))) {
            Sheet sheet = workbook.getSheetAt(0);

            // 验证表头
            Row headerRow = sheet.getRow(0);
            assertEquals("姓名", headerRow.getCell(0).getStringCellValue());
            assertEquals("身份证号", headerRow.getCell(1).getStringCellValue());
            assertEquals("危险等级", headerRow.getCell(15).getStringCellValue());

            // 验证数据行数
            assertEquals(6, sheet.getPhysicalNumberOfRows()); // 1个表头 + 5个数据行

            // 验证第一行数据
            Row dataRow = sheet.getRow(1);
            assertEquals("张三1", dataRow.getCell(0).getStringCellValue());
            assertEquals("370102199001010001", dataRow.getCell(1).getStringCellValue());
        }
    }

    @Test
    @Order(2)
    @DisplayName("测试导出指定数据到Excel")
    void testExportSelectedToExcel() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        List<Petitioner> selectedData = allData.subList(0, 2);

        int count = exportService.exportToExcel(TEST_EXCEL_FILE, selectedData);

        assertEquals(2, count);

        // 验证只导出了2条数据
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(TEST_EXCEL_FILE))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(3, sheet.getPhysicalNumberOfRows()); // 1个表头 + 2个数据行
        }
    }

    @Test
    @Order(3)
    @DisplayName("测试导出空数据到Excel")
    void testExportEmptyToExcel() throws IOException {
        dataManager.clear();

        int count = exportService.exportToExcel(TEST_EXCEL_FILE);

        assertEquals(0, count);

        // 验证只有表头
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(TEST_EXCEL_FILE))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(1, sheet.getPhysicalNumberOfRows()); // 只有表头
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试导出所有数据到CSV")
    void testExportAllToCsv() throws IOException {
        int count = exportService.exportToCsv(TEST_CSV_FILE);

        assertEquals(5, count);
        assertTrue(Files.exists(Paths.get(TEST_CSV_FILE)));

        // 验证CSV文件内容
        List<String> lines = Files.readAllLines(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);

        // 第一行是BOM + 表头
        assertTrue(lines.get(0).contains("姓名"));

        // 总行数 = 1个表头 + 5个数据行
        assertEquals(6, lines.size());

        // 验证第一行数据
        String firstDataLine = lines.get(1);
        assertTrue(firstDataLine.contains("张三1"));
        assertTrue(firstDataLine.contains("370102199001010001"));
    }

    @Test
    @Order(5)
    @DisplayName("测试导出指定数据到CSV")
    void testExportSelectedToCsv() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        List<Petitioner> selectedData = allData.subList(0, 3);

        int count = exportService.exportToCsv(TEST_CSV_FILE, selectedData);

        assertEquals(3, count);

        // 验证只导出了3条数据
        List<String> lines = Files.readAllLines(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);
        assertEquals(4, lines.size()); // 1个表头 + 3个数据行
    }

    @Test
    @Order(6)
    @DisplayName("测试导出空数据到CSV")
    void testExportEmptyToCsv() throws IOException {
        dataManager.clear();

        int count = exportService.exportToCsv(TEST_CSV_FILE);

        assertEquals(0, count);

        // 验证只有表头
        List<String> lines = Files.readAllLines(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);
        assertEquals(1, lines.size()); // 只有表头
    }

    @Test
    @Order(7)
    @DisplayName("测试CSV转义功能")
    void testCsvEscaping() throws IOException {
        // 创建包含特殊字符的数据
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName("张三,含逗号");
        personalInfo.setIdCard("370102199001011234");
        personalInfo.setGender(Gender.MALE);
        personalInfo.setHomeAddress("地址包含\"引号\"和,逗号");

        BeijingContact beijingContact = new BeijingContact();
        PetitionCase petitionCase = new PetitionCase();
        petitionCase.setPetitionContent("诉求内容\n包含换行符");

        RiskAssessment riskAssessment = new RiskAssessment();

        Petitioner petitioner = new Petitioner(personalInfo, beijingContact, petitionCase, riskAssessment);

        dataManager.clear();
        dataManager.save(petitioner);

        exportService.exportToCsv(TEST_CSV_FILE);

        // 读取CSV内容
        List<String> lines = Files.readAllLines(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);
        String dataLine = lines.get(1);

        // 验证特殊字符被正确转义（包含逗号和引号的字段应该被引号包围）
        assertTrue(dataLine.contains("\"张三,含逗号\""));
        assertTrue(dataLine.contains("\"地址包含\"\"引号\"\"和,逗号\""));
    }

    @Test
    @Order(8)
    @DisplayName("测试按ID列表导出Excel")
    void testExportSelectedByIds() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        List<String> ids = new ArrayList<>();
        ids.add(allData.get(0).getId());
        ids.add(allData.get(1).getId());

        int count = exportService.exportSelected(TEST_EXCEL_FILE, ids, "excel");

        assertEquals(2, count);

        // 验证导出了2条数据
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(TEST_EXCEL_FILE))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(3, sheet.getPhysicalNumberOfRows()); // 1个表头 + 2个数据行
        }
    }

    @Test
    @Order(9)
    @DisplayName("测试按ID列表导出CSV")
    void testExportSelectedByIdsToCsv() throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        List<String> ids = new ArrayList<>();
        ids.add(allData.get(0).getId());
        ids.add(allData.get(2).getId());

        int count = exportService.exportSelected(TEST_CSV_FILE, ids, "csv");

        assertEquals(2, count);

        // 验证导出了2条数据
        List<String> lines = Files.readAllLines(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);
        assertEquals(3, lines.size()); // 1个表头 + 2个数据行
    }

    @Test
    @Order(10)
    @DisplayName("测试导出空ID列表")
    void testExportEmptyIdList() throws IOException {
        int count = exportService.exportSelected(TEST_EXCEL_FILE, new ArrayList<>(), "excel");

        assertEquals(0, count);
    }

    @Test
    @Order(11)
    @DisplayName("测试导出null ID列表")
    void testExportNullIdList() throws IOException {
        int count = exportService.exportSelected(TEST_EXCEL_FILE, null, "excel");

        assertEquals(0, count);
    }

    @Test
    @Order(12)
    @DisplayName("测试导出不存在的ID")
    void testExportNonExistentIds() throws IOException {
        List<String> ids = new ArrayList<>();
        ids.add("non-existent-id-1");
        ids.add("non-existent-id-2");

        int count = exportService.exportSelected(TEST_EXCEL_FILE, ids, "excel");

        assertEquals(0, count);
    }

    @Test
    @Order(13)
    @DisplayName("测试Excel包含所有字段")
    void testExcelContainsAllFields() throws IOException {
        exportService.exportToExcel(TEST_EXCEL_FILE);

        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(TEST_EXCEL_FILE))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            // 验证所有列头
            assertEquals(16, headerRow.getPhysicalNumberOfCells());
            assertEquals("姓名", headerRow.getCell(0).getStringCellValue());
            assertEquals("身份证号", headerRow.getCell(1).getStringCellValue());
            assertEquals("性别", headerRow.getCell(2).getStringCellValue());
            assertEquals("籍贯", headerRow.getCell(3).getStringCellValue());
            assertEquals("学历", headerRow.getCell(4).getStringCellValue());
            assertEquals("婚姻状态", headerRow.getCell(5).getStringCellValue());
            assertEquals("联系电话", headerRow.getCell(6).getStringCellValue());
            assertEquals("职业", headerRow.getCell(7).getStringCellValue());
            assertEquals("常住地址", headerRow.getCell(8).getStringCellValue());
            assertEquals("上访次数", headerRow.getCell(9).getStringCellValue());
            assertEquals("在京关系人", headerRow.getCell(10).getStringCellValue());
            assertEquals("关系", headerRow.getCell(11).getStringCellValue());
            assertEquals("诉求内容", headerRow.getCell(12).getStringCellValue());
            assertEquals("进京方式", headerRow.getCell(13).getStringCellValue());
            assertEquals("在京通行方式", headerRow.getCell(14).getStringCellValue());
            assertEquals("危险等级", headerRow.getCell(15).getStringCellValue());

            // 验证数据行
            Row dataRow = sheet.getRow(1);
            assertEquals("张三1", dataRow.getCell(0).getStringCellValue());
            assertEquals("370102199001010001", dataRow.getCell(1).getStringCellValue());
            assertEquals("男", dataRow.getCell(2).getStringCellValue());
            assertEquals("山东", dataRow.getCell(3).getStringCellValue());
            assertEquals("本科", dataRow.getCell(4).getStringCellValue());
            assertEquals("已婚", dataRow.getCell(5).getStringCellValue());
            assertEquals("13800138001", dataRow.getCell(6).getStringCellValue());
            assertEquals("工人", dataRow.getCell(7).getStringCellValue());
            assertEquals("济南市历下区", dataRow.getCell(8).getStringCellValue());
            assertEquals(1, (int) dataRow.getCell(9).getNumericCellValue());
            assertEquals("李四", dataRow.getCell(10).getStringCellValue());
            assertEquals("朋友", dataRow.getCell(11).getStringCellValue());
            assertEquals("测试诉求", dataRow.getCell(12).getStringCellValue());
            assertEquals("高铁", dataRow.getCell(13).getStringCellValue());
            assertEquals("地铁", dataRow.getCell(14).getStringCellValue());
            assertEquals("低危", dataRow.getCell(15).getStringCellValue());
        }
    }

    @Test
    @Order(14)
    @DisplayName("测试CSV包含所有字段")
    void testCsvContainsAllFields() throws IOException {
        exportService.exportToCsv(TEST_CSV_FILE);

        List<String> lines = Files.readAllLines(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);

        // 验证表头
        String header = lines.get(0);
        assertTrue(header.contains("姓名"));
        assertTrue(header.contains("身份证号"));
        assertTrue(header.contains("性别"));
        assertTrue(header.contains("籍贯"));
        assertTrue(header.contains("学历"));
        assertTrue(header.contains("婚姻状态"));
        assertTrue(header.contains("联系电话"));
        assertTrue(header.contains("职业"));
        assertTrue(header.contains("常住地址"));
        assertTrue(header.contains("上访次数"));
        assertTrue(header.contains("在京关系人"));
        assertTrue(header.contains("关系"));
        assertTrue(header.contains("诉求内容"));
        assertTrue(header.contains("进京方式"));
        assertTrue(header.contains("在京通行方式"));
        assertTrue(header.contains("危险等级"));

        // 验证数据行
        String dataLine = lines.get(1);
        assertTrue(dataLine.contains("张三1"));
        assertTrue(dataLine.contains("370102199001010001"));
        assertTrue(dataLine.contains("男"));
        assertTrue(dataLine.contains("山东"));
    }

    @Test
    @Order(15)
    @DisplayName("测试CSV UTF-8编码")
    void testCsvUtf8Encoding() throws IOException {
        exportService.exportToCsv(TEST_CSV_FILE);

        // 读取文件的前3个字节
        try (FileInputStream fis = new FileInputStream(TEST_CSV_FILE)) {
            byte[] bom = new byte[3];
            fis.read(bom);

            // 验证UTF-8 BOM
            assertEquals((byte) 0xEF, bom[0]);
            assertEquals((byte) 0xBB, bom[1]);
            assertEquals((byte) 0xBF, bom[2]);
        }
    }

    @Test
    @Order(16)
    @DisplayName("测试多个电话号码导出")
    void testMultiplePhonesExport() throws IOException {
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName("多号码测试");
        personalInfo.setIdCard("370102199001011234");
        personalInfo.addPhone("13800138001");
        personalInfo.addPhone("13800138002");
        personalInfo.addPhone("13800138003");

        BeijingContact beijingContact = new BeijingContact();
        PetitionCase petitionCase = new PetitionCase();
        RiskAssessment riskAssessment = new RiskAssessment();

        Petitioner petitioner = new Petitioner(personalInfo, beijingContact, petitionCase, riskAssessment);

        dataManager.clear();
        dataManager.save(petitioner);

        exportService.exportToCsv(TEST_CSV_FILE);

        List<String> lines = Files.readAllLines(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);
        String dataLine = lines.get(1);

        // CSV中多个电话用分号分隔
        assertTrue(dataLine.contains("13800138001;13800138002;13800138003"));
    }
}
