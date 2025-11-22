package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.*;
import com.petition.model.enums.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ImportService单元测试
 *
 * @author 刘一村
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImportServiceTest {
    private static final String TEST_DATA_DIR = "test_import_data";
    private static final String TEST_EXCEL_FILE = "test_import.xlsx";
    private ImportService importService;
    private JsonDataManager dataManager;

    @BeforeEach
    void setUp() {
        dataManager = new JsonDataManager(TEST_DATA_DIR);
        importService = new ImportService(TEST_DATA_DIR);
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

        // 清理测试Excel文件
        Path excelFile = Paths.get(TEST_EXCEL_FILE);
        Files.deleteIfExists(excelFile);
    }

    /**
     * 创建测试用Excel文件
     */
    private void createTestExcelFile(String fileName, int rowCount) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("上访人员数据");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "姓名", "身份证号", "性别", "籍贯", "学历", "婚姻状态",
                "联系电话", "职业", "常住地址", "上访次数",
                "在京关系人", "关系", "诉求内容", "进京方式", "在京通行方式", "危险等级"
            };

            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 创建数据行
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue("张三" + i);
                row.createCell(1).setCellValue("37010219900101" + String.format("%04d", i));
                row.createCell(2).setCellValue("男");
                row.createCell(3).setCellValue("山东");
                row.createCell(4).setCellValue("本科");
                row.createCell(5).setCellValue("已婚");
                row.createCell(6).setCellValue("1380013800" + i);
                row.createCell(7).setCellValue("工人");
                row.createCell(8).setCellValue("济南市历下区");
                row.createCell(9).setCellValue(i);
                row.createCell(10).setCellValue("李四");
                row.createCell(11).setCellValue("朋友");
                row.createCell(12).setCellValue("测试诉求");
                row.createCell(13).setCellValue("高铁");
                row.createCell(14).setCellValue("地铁");
                row.createCell(15).setCellValue("低危");
            }

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * 创建包含错误数据的Excel文件
     */
    private void createInvalidExcelFile(String fileName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("上访人员数据");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "姓名", "身份证号", "性别", "籍贯", "学历", "婚姻状态",
                "联系电话", "职业", "常住地址", "上访次数",
                "在京关系人", "关系", "诉求内容", "进京方式", "在京通行方式", "危险等级"
            };

            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 第1行：姓名为空
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("");
            row1.createCell(1).setCellValue("370102199001011111");

            // 第2行：身份证号格式错误
            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("李四");
            row2.createCell(1).setCellValue("12345");

            // 第3行：正常数据
            Row row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("王五");
            row3.createCell(1).setCellValue("370102199003033333");
            row3.createCell(2).setCellValue("男");

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("测试导入成功")
    void testImportSuccess() throws IOException {
        createTestExcelFile(TEST_EXCEL_FILE, 5);

        ImportService.ImportResult result = importService.importFromExcel(TEST_EXCEL_FILE, false);

        assertEquals(5, result.getSuccessCount());
        assertEquals(0, result.getErrors().size());
        assertEquals(0, result.getSkipped().size());

        // 验证数据已保存
        List<Petitioner> allData = dataManager.loadAll();
        assertEquals(5, allData.size());
    }

    @Test
    @Order(2)
    @DisplayName("测试导入空文件")
    void testImportEmptyFile() throws IOException {
        createTestExcelFile(TEST_EXCEL_FILE, 0);

        ImportService.ImportResult result = importService.importFromExcel(TEST_EXCEL_FILE, false);

        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getErrors().size());
    }

    @Test
    @Order(3)
    @DisplayName("测试导入包含错误数据")
    void testImportWithInvalidData() throws IOException {
        createInvalidExcelFile(TEST_EXCEL_FILE);

        ImportService.ImportResult result = importService.importFromExcel(TEST_EXCEL_FILE, false);

        // 第1行和第2行有错误，第3行正常
        assertEquals(1, result.getSuccessCount());
        assertEquals(2, result.getErrors().size());

        // 验证错误信息
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.getMessage().contains("姓名不能为空")));
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.getMessage().contains("身份证号格式不正确")));
    }

    @Test
    @Order(4)
    @DisplayName("测试导入重复数据-不跳过")
    void testImportDuplicateNoSkip() throws IOException {
        // 先导入一次
        createTestExcelFile(TEST_EXCEL_FILE, 3);
        importService.importFromExcel(TEST_EXCEL_FILE, false);

        // 再次导入相同数据
        ImportService.ImportResult result = importService.importFromExcel(TEST_EXCEL_FILE, false);

        // 由于身份证号重复，应该都失败
        assertEquals(0, result.getSuccessCount());
        assertEquals(3, result.getErrors().size());
        assertTrue(result.getErrors().stream()
                .allMatch(e -> e.getMessage().contains("已存在")));
    }

    @Test
    @Order(5)
    @DisplayName("测试导入重复数据-跳过")
    void testImportDuplicateWithSkip() throws IOException {
        // 先导入一次
        createTestExcelFile(TEST_EXCEL_FILE, 3);
        importService.importFromExcel(TEST_EXCEL_FILE, false);

        // 再次导入相同数据，跳过重复
        ImportService.ImportResult result = importService.importFromExcel(TEST_EXCEL_FILE, true);

        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getErrors().size());
        assertEquals(3, result.getSkipped().size());
    }

    @Test
    @Order(6)
    @DisplayName("测试导入文件不存在")
    void testImportFileNotExists() {
        assertThrows(IOException.class, () -> {
            importService.importFromExcel("not_exists.xlsx", false);
        });
    }

    @Test
    @Order(7)
    @DisplayName("测试导入null文件路径")
    void testImportNullFilePath() {
        assertThrows(IllegalArgumentException.class, () -> {
            importService.importFromExcel(null, false);
        });
    }

    @Test
    @Order(8)
    @DisplayName("测试导入空文件路径")
    void testImportEmptyFilePath() {
        assertThrows(IllegalArgumentException.class, () -> {
            importService.importFromExcel("", false);
        });
    }

    @Test
    @Order(9)
    @DisplayName("测试导入结果统计")
    void testImportResultStatistics() throws IOException {
        createInvalidExcelFile(TEST_EXCEL_FILE);

        ImportService.ImportResult result = importService.importFromExcel(TEST_EXCEL_FILE, false);

        assertEquals(1, result.getSuccessCount());
        assertEquals(2, result.getErrors().size());
        assertEquals(0, result.getSkipped().size());

        // 验证错误行号
        assertTrue(result.getErrors().stream().anyMatch(e -> e.getRowNumber() == 1));
        assertTrue(result.getErrors().stream().anyMatch(e -> e.getRowNumber() == 2));
    }

    @Test
    @Order(10)
    @DisplayName("测试导入混合场景")
    void testImportMixedScenario() throws IOException {
        // 先导入2条正常数据
        createTestExcelFile(TEST_EXCEL_FILE, 2);
        importService.importFromExcel(TEST_EXCEL_FILE, false);

        // 创建包含正常、错误、重复数据的文件
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("上访人员数据");

            // 表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "姓名", "身份证号", "性别", "籍贯", "学历", "婚姻状态",
                "联系电话", "职业", "常住地址", "上访次数",
                "在京关系人", "关系", "诉求内容", "进京方式", "在京通行方式", "危险等级"
            };
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 第1行：重复数据
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("张三1");
            row1.createCell(1).setCellValue("370102199001010001");

            // 第2行：错误数据（姓名为空）
            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("");
            row2.createCell(1).setCellValue("370102199001011111");

            // 第3行：正常新数据
            Row row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("新用户");
            row3.createCell(1).setCellValue("370102199001012222");
            row3.createCell(2).setCellValue("女");

            try (FileOutputStream fos = new FileOutputStream(TEST_EXCEL_FILE)) {
                workbook.write(fos);
            }
        }

        // 跳过重复数据导入
        ImportService.ImportResult result = importService.importFromExcel(TEST_EXCEL_FILE, true);

        assertEquals(1, result.getSuccessCount());  // 第3行成功
        assertEquals(1, result.getErrors().size()); // 第2行错误
        assertEquals(1, result.getSkipped().size()); // 第1行跳过
    }

    @Test
    @Order(11)
    @DisplayName("测试枚举值解析")
    void testEnumParsing() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("上访人员数据");

            // 表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "姓名", "身份证号", "性别", "籍贯", "学历", "婚姻状态",
                "联系电话", "职业", "常住地址", "上访次数",
                "在京关系人", "关系", "诉求内容", "进京方式", "在京通行方式", "危险等级"
            };
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 测试各种枚举值
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("测试用户");
            row.createCell(1).setCellValue("370102199001011234");
            row.createCell(2).setCellValue("女");
            row.createCell(3).setCellValue("北京");
            row.createCell(4).setCellValue("硕士");
            row.createCell(5).setCellValue("离异");
            row.createCell(6).setCellValue("13800138000");
            row.createCell(7).setCellValue("教师");
            row.createCell(8).setCellValue("北京市朝阳区");
            row.createCell(9).setCellValue(5);
            row.createCell(10).setCellValue("张三");
            row.createCell(11).setCellValue("亲戚");
            row.createCell(12).setCellValue("测试");
            row.createCell(13).setCellValue("飞机");
            row.createCell(14).setCellValue("公交");
            row.createCell(15).setCellValue("高危");

            try (FileOutputStream fos = new FileOutputStream(TEST_EXCEL_FILE)) {
                workbook.write(fos);
            }
        }

        ImportService.ImportResult result = importService.importFromExcel(TEST_EXCEL_FILE, false);

        assertEquals(1, result.getSuccessCount());

        List<Petitioner> data = dataManager.loadAll();
        Petitioner p = data.get(0);

        assertEquals(Gender.FEMALE, p.getPersonalInfo().getGender());
        assertEquals(Education.MASTER, p.getPersonalInfo().getEducation());
        assertEquals(MaritalStatus.DIVORCED, p.getPersonalInfo().getMaritalStatus());
        assertEquals(EntryMethod.AIRPLANE, p.getPetitionCase().getEntryMethod());
        assertEquals(TransportMethod.BUS, p.getPetitionCase().getTransportInBeijing());
        assertEquals(RiskLevel.HIGH, p.getRiskAssessment().getRiskLevel());
    }

    @Test
    @Order(12)
    @DisplayName("测试数据验证功能")
    void testDataValidation() throws IOException {
        Petitioner validPetitioner = new Petitioner();
        PersonalInfo info = new PersonalInfo();
        info.setName("张三");
        info.setIdCard("370102199001011234");
        validPetitioner.setPersonalInfo(info);

        List<String> errors = importService.validateImportData(validPetitioner);
        assertTrue(errors.isEmpty());

        // 测试空姓名
        PersonalInfo invalidInfo = new PersonalInfo();
        invalidInfo.setName("");
        invalidInfo.setIdCard("370102199001011234");
        Petitioner invalidPetitioner = new Petitioner();
        invalidPetitioner.setPersonalInfo(invalidInfo);

        errors = importService.validateImportData(invalidPetitioner);
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e -> e.contains("姓名不能为空")));
    }
}
