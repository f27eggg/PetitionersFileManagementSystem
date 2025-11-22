package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.*;
import com.petition.model.enums.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 导入服务
 * 提供从Excel文件导入上访人员数据的功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class ImportService {
    /**
     * 数据管理器
     */
    private final JsonDataManager dataManager;

    /**
     * 上访人员服务（用于数据验证和保存）
     */
    private final PetitionerService petitionerService;

    /**
     * 默认构造函数
     */
    public ImportService() {
        this.dataManager = new JsonDataManager();
        this.petitionerService = new PetitionerService(dataManager);
    }

    /**
     * 带数据目录的构造函数
     *
     * @param dataDirectory 数据目录路径
     */
    public ImportService(String dataDirectory) {
        this.dataManager = new JsonDataManager(dataDirectory);
        this.petitionerService = new PetitionerService(dataManager);
    }

    /**
     * 从Excel文件导入数据
     *
     * @param filePath Excel文件路径
     * @param skipDuplicates 是否跳过重复数据（true跳过，false报错）
     * @return 导入结果
     * @throws IOException 文件读取异常
     */
    public ImportResult importFromExcel(String filePath, boolean skipDuplicates) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        ImportResult result = new ImportResult();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getLastRowNum();

            // 跳过表头，从第二行开始
            for (int i = 1; i <= totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                try {
                    // 解析行数据
                    Petitioner petitioner = parseRowData(row);

                    // 验证数据
                    List<String> errors = validateImportData(petitioner);
                    if (!errors.isEmpty()) {
                        result.addError(i, String.join("; ", errors));
                        continue;
                    }

                    // 检查重复
                    if (isDuplicate(petitioner)) {
                        if (skipDuplicates) {
                            result.addSkipped(i, "身份证号已存在: " + petitioner.getIdCard());
                            continue;
                        } else {
                            result.addError(i, "身份证号已存在: " + petitioner.getIdCard());
                            continue;
                        }
                    }

                    // 保存数据
                    petitionerService.addPetitioner(petitioner);
                    result.incrementSuccess();

                } catch (Exception e) {
                    result.addError(i, "解析失败: " + e.getMessage());
                }
            }
        }

        return result;
    }

    /**
     * 解析Excel行数据为Petitioner对象
     *
     * @param row Excel行
     * @return Petitioner对象
     */
    private Petitioner parseRowData(Row row) {
        // 个人信息
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setName(getCellValue(row, 0));
        personalInfo.setIdCard(getCellValue(row, 1));
        personalInfo.setGender(parseGender(getCellValue(row, 2)));
        personalInfo.setNativePlace(getCellValue(row, 3));
        personalInfo.setEducation(parseEducation(getCellValue(row, 4)));
        personalInfo.setMaritalStatus(parseMaritalStatus(getCellValue(row, 5)));

        // 联系电话（可能多个，用逗号分隔）
        String phones = getCellValue(row, 6);
        if (phones != null && !phones.isBlank()) {
            for (String phone : phones.split("[,，]")) {
                personalInfo.addPhone(phone.trim());
            }
        }

        personalInfo.setOccupation(getCellValue(row, 7));
        personalInfo.setHomeAddress(getCellValue(row, 8));

        // 上访次数
        String visitCountStr = getCellValue(row, 9);
        if (visitCountStr != null && !visitCountStr.isBlank()) {
            try {
                personalInfo.setVisitCount(Integer.parseInt(visitCountStr));
            } catch (NumberFormatException e) {
                personalInfo.setVisitCount(0);
            }
        }

        // 在京关系人
        BeijingContact beijingContact = new BeijingContact();
        beijingContact.setContactName(getCellValue(row, 10));
        beijingContact.setRelationship(getCellValue(row, 11));

        // 信访案件
        PetitionCase petitionCase = new PetitionCase();
        petitionCase.setPetitionContent(getCellValue(row, 12));
        petitionCase.setEntryMethod(parseEntryMethod(getCellValue(row, 13)));
        petitionCase.setTransportInBeijing(parseTransportMethod(getCellValue(row, 14)));

        // 评估结果
        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setRiskLevel(parseRiskLevel(getCellValue(row, 15)));

        return new Petitioner(personalInfo, beijingContact, petitionCase, riskAssessment);
    }

    /**
     * 获取单元格的字符串值
     *
     * @param row Excel行
     * @param cellIndex 单元格索引
     * @return 单元格值
     */
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // 处理数字类型（可能是身份证号等）
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    /**
     * 解析性别
     */
    private Gender parseGender(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Gender.fromDisplayName(value.trim());
    }

    /**
     * 解析学历
     */
    private Education parseEducation(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Education.fromDisplayName(value.trim());
    }

    /**
     * 解析婚姻状态
     */
    private MaritalStatus parseMaritalStatus(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return MaritalStatus.fromDisplayName(value.trim());
    }

    /**
     * 解析进京方式
     */
    private EntryMethod parseEntryMethod(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return EntryMethod.fromDisplayName(value.trim());
    }

    /**
     * 解析在京通行方式
     */
    private TransportMethod parseTransportMethod(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return TransportMethod.fromDisplayName(value.trim());
    }

    /**
     * 解析危险等级
     */
    private RiskLevel parseRiskLevel(String value) {
        if (value == null || value.isBlank()) {
            return RiskLevel.LOW; // 默认低危
        }
        return RiskLevel.fromDisplayName(value.trim());
    }

    /**
     * 验证导入数据
     *
     * @param petitioner 上访人员对象
     * @return 错误信息列表
     */
    public List<String> validateImportData(Petitioner petitioner) {
        List<String> errors = new ArrayList<>();

        if (petitioner.getPersonalInfo() == null) {
            errors.add("个人信息不能为空");
            return errors;
        }

        // 验证必填字段
        String name = petitioner.getName();
        if (name == null || name.isBlank()) {
            errors.add("姓名不能为空");
        }

        String idCard = petitioner.getIdCard();
        if (idCard == null || idCard.isBlank()) {
            errors.add("身份证号不能为空");
        } else if (!idCard.matches("^\\d{15}(\\d{2}[0-9Xx])?$")) {
            errors.add("身份证号格式不正确");
        }

        return errors;
    }

    /**
     * 检查是否为重复数据
     *
     * @param petitioner 上访人员对象
     * @return 是否重复
     */
    private boolean isDuplicate(Petitioner petitioner) throws IOException {
        String idCard = petitioner.getIdCard();
        if (idCard == null || idCard.isBlank()) {
            return false;
        }

        List<Petitioner> existing = dataManager.loadAll();
        return existing.stream()
                .anyMatch(p -> idCard.equals(p.getIdCard()));
    }

    /**
     * 导入结果类
     */
    public static class ImportResult {
        private int successCount = 0;
        private final List<ImportError> errors = new ArrayList<>();
        private final List<ImportError> skipped = new ArrayList<>();

        public void incrementSuccess() {
            successCount++;
        }

        public void addError(int rowNumber, String message) {
            errors.add(new ImportError(rowNumber, message));
        }

        public void addSkipped(int rowNumber, String message) {
            skipped.add(new ImportError(rowNumber, message));
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getErrorCount() {
            return errors.size();
        }

        public int getSkippedCount() {
            return skipped.size();
        }

        public List<ImportError> getErrors() {
            return new ArrayList<>(errors);
        }

        public List<ImportError> getSkipped() {
            return new ArrayList<>(skipped);
        }

        public int getTotalProcessed() {
            return successCount + errors.size() + skipped.size();
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        @Override
        public String toString() {
            return String.format("导入结果: 成功=%d, 失败=%d, 跳过=%d",
                    successCount, errors.size(), skipped.size());
        }
    }

    /**
     * 导入错误记录类
     */
    public static class ImportError {
        private final int rowNumber;
        private final String message;

        public ImportError(int rowNumber, String message) {
            this.rowNumber = rowNumber;
            this.message = message;
        }

        public int getRowNumber() {
            return rowNumber;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return String.format("第%d行: %s", rowNumber, message);
        }
    }
}
