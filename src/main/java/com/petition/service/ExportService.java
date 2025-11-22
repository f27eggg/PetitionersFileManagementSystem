package com.petition.service;

import com.petition.dao.JsonDataManager;
import com.petition.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 导出服务
 * 提供将上访人员数据导出为Excel或CSV文件的功能
 *
 * @author 刘一村
 * @version 1.0.0
 */
public class ExportService {
    /**
     * 数据管理器
     */
    private final JsonDataManager dataManager;

    /**
     * 默认构造函数
     */
    public ExportService() {
        this.dataManager = new JsonDataManager();
    }

    /**
     * 带数据目录的构造函数
     *
     * @param dataDirectory 数据目录路径
     */
    public ExportService(String dataDirectory) {
        this.dataManager = new JsonDataManager(dataDirectory);
    }

    /**
     * 导出所有数据到Excel
     *
     * @param filePath 导出文件路径
     * @return 导出的记录数
     * @throws IOException 文件操作异常
     */
    public int exportToExcel(String filePath) throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        return exportToExcel(filePath, allData);
    }

    /**
     * 导出指定数据到Excel
     *
     * @param filePath 导出文件路径
     * @param petitioners 要导出的数据列表
     * @return 导出的记录数
     * @throws IOException 文件操作异常
     */
    public int exportToExcel(String filePath, List<Petitioner> petitioners) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("上访人员数据");

            // 创建表头
            createExcelHeader(sheet);

            // 创建数据行
            int rowNum = 1;
            for (Petitioner p : petitioners) {
                Row row = sheet.createRow(rowNum++);
                fillExcelRow(row, p);
            }

            // 自动调整列宽
            for (int i = 0; i < 16; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            return petitioners.size();
        }
    }

    /**
     * 创建Excel表头
     *
     * @param sheet Excel工作表
     */
    private void createExcelHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);

        // 创建表头样式
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] headers = {
            "姓名", "身份证号", "性别", "籍贯", "学历", "婚姻状态",
            "联系电话", "职业", "常住地址", "上访次数",
            "在京关系人", "关系", "诉求内容", "进京方式", "在京通行方式", "危险等级"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * 填充Excel数据行
     *
     * @param row Excel行
     * @param petitioner 上访人员对象
     */
    private void fillExcelRow(Row row, Petitioner petitioner) {
        int cellNum = 0;

        // 个人信息
        PersonalInfo info = petitioner.getPersonalInfo();
        if (info != null) {
            row.createCell(cellNum++).setCellValue(info.getName());
            row.createCell(cellNum++).setCellValue(info.getIdCard());
            row.createCell(cellNum++).setCellValue(info.getGender() != null ? info.getGender().toString() : "");
            row.createCell(cellNum++).setCellValue(info.getNativePlace());
            row.createCell(cellNum++).setCellValue(info.getEducation() != null ? info.getEducation().toString() : "");
            row.createCell(cellNum++).setCellValue(info.getMaritalStatus() != null ? info.getMaritalStatus().toString() : "");
            row.createCell(cellNum++).setCellValue(info.getPhones() != null ? String.join(",", info.getPhones()) : "");
            row.createCell(cellNum++).setCellValue(info.getOccupation());
            row.createCell(cellNum++).setCellValue(info.getHomeAddress());
            row.createCell(cellNum++).setCellValue(info.getVisitCount() != null ? info.getVisitCount() : 0);
        } else {
            cellNum += 10;
        }

        // 在京关系人
        BeijingContact contact = petitioner.getBeijingContact();
        if (contact != null) {
            row.createCell(cellNum++).setCellValue(contact.getContactName());
            row.createCell(cellNum++).setCellValue(contact.getRelationship());
        } else {
            cellNum += 2;
        }

        // 信访案件
        PetitionCase petitionCase = petitioner.getPetitionCase();
        if (petitionCase != null) {
            row.createCell(cellNum++).setCellValue(petitionCase.getPetitionContent());
            row.createCell(cellNum++).setCellValue(petitionCase.getEntryMethod() != null ? petitionCase.getEntryMethod().toString() : "");
            row.createCell(cellNum++).setCellValue(petitionCase.getTransportInBeijing() != null ? petitionCase.getTransportInBeijing().toString() : "");
        } else {
            cellNum += 3;
        }

        // 评估结果
        RiskAssessment assessment = petitioner.getRiskAssessment();
        if (assessment != null && assessment.getRiskLevel() != null) {
            row.createCell(cellNum).setCellValue(assessment.getRiskLevel().toString());
        }
    }

    /**
     * 导出所有数据到CSV
     *
     * @param filePath 导出文件路径
     * @return 导出的记录数
     * @throws IOException 文件操作异常
     */
    public int exportToCsv(String filePath) throws IOException {
        List<Petitioner> allData = dataManager.loadAll();
        return exportToCsv(filePath, allData);
    }

    /**
     * 导出指定数据到CSV
     *
     * @param filePath 导出文件路径
     * @param petitioners 要导出的数据列表
     * @return 导出的记录数
     * @throws IOException 文件操作异常
     */
    public int exportToCsv(String filePath, List<Petitioner> petitioners) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8)) {

            // 写入BOM头（让Excel正确识别UTF-8编码）
            writer.write('\uFEFF');

            // 写入表头
            writer.write("姓名,身份证号,性别,籍贯,学历,婚姻状态,联系电话,职业,常住地址,上访次数,");
            writer.write("在京关系人,关系,诉求内容,进京方式,在京通行方式,危险等级\n");

            // 写入数据行
            for (Petitioner p : petitioners) {
                writer.write(toCsvRow(p));
                writer.write("\n");
            }

            return petitioners.size();
        }
    }

    /**
     * 将上访人员对象转换为CSV行
     *
     * @param petitioner 上访人员对象
     * @return CSV行字符串
     */
    private String toCsvRow(Petitioner petitioner) {
        StringBuilder sb = new StringBuilder();

        // 个人信息
        PersonalInfo info = petitioner.getPersonalInfo();
        if (info != null) {
            sb.append(csvEscape(info.getName())).append(",");
            sb.append(csvEscape(info.getIdCard())).append(",");
            sb.append(csvEscape(info.getGender() != null ? info.getGender().toString() : "")).append(",");
            sb.append(csvEscape(info.getNativePlace())).append(",");
            sb.append(csvEscape(info.getEducation() != null ? info.getEducation().toString() : "")).append(",");
            sb.append(csvEscape(info.getMaritalStatus() != null ? info.getMaritalStatus().toString() : "")).append(",");
            sb.append(csvEscape(info.getPhones() != null ? String.join(";", info.getPhones()) : "")).append(",");
            sb.append(csvEscape(info.getOccupation())).append(",");
            sb.append(csvEscape(info.getHomeAddress())).append(",");
            sb.append(info.getVisitCount() != null ? info.getVisitCount() : 0).append(",");
        } else {
            sb.append(",,,,,,,,,,");
        }

        // 在京关系人
        BeijingContact contact = petitioner.getBeijingContact();
        if (contact != null) {
            sb.append(csvEscape(contact.getContactName())).append(",");
            sb.append(csvEscape(contact.getRelationship())).append(",");
        } else {
            sb.append(",,");
        }

        // 信访案件
        PetitionCase petitionCase = petitioner.getPetitionCase();
        if (petitionCase != null) {
            sb.append(csvEscape(petitionCase.getPetitionContent())).append(",");
            sb.append(csvEscape(petitionCase.getEntryMethod() != null ? petitionCase.getEntryMethod().toString() : "")).append(",");
            sb.append(csvEscape(petitionCase.getTransportInBeijing() != null ? petitionCase.getTransportInBeijing().toString() : "")).append(",");
        } else {
            sb.append(",,,");
        }

        // 评估结果
        RiskAssessment assessment = petitioner.getRiskAssessment();
        if (assessment != null && assessment.getRiskLevel() != null) {
            sb.append(csvEscape(assessment.getRiskLevel().toString()));
        }

        return sb.toString();
    }

    /**
     * CSV字段转义
     * 处理包含逗号、引号、换行符的字段
     *
     * @param value 原始值
     * @return 转义后的值
     */
    private String csvEscape(String value) {
        if (value == null) {
            return "";
        }

        // 如果包含逗号、引号或换行符，需要用引号包围，并将引号转义为两个引号
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    /**
     * 导出选定的数据
     * 根据ID列表导出
     *
     * @param filePath 导出文件路径
     * @param ids 要导出的数据ID列表
     * @param format 导出格式（"excel"或"csv"）
     * @return 导出的记录数
     * @throws IOException 文件操作异常
     */
    public int exportSelected(String filePath, List<String> ids, String format) throws IOException {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        // 加载所有数据并筛选
        List<Petitioner> allData = dataManager.loadAll();
        List<Petitioner> selectedData = allData.stream()
                .filter(p -> ids.contains(p.getId()))
                .toList();

        // 根据格式导出
        if ("csv".equalsIgnoreCase(format)) {
            return exportToCsv(filePath, selectedData);
        } else {
            return exportToExcel(filePath, selectedData);
        }
    }
}
