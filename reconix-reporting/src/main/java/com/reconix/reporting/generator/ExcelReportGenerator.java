package com.reconix.reporting.generator;

import com.reconix.reporting.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ExcelReportGenerator implements ReportGenerator {

    @Override
    public byte[] generate(ReportRequest request) {
        log.info("Generating Excel report for tenant: {}", request.getTenantId());
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Relatório");
            
            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            
            // Título e período
            createTitleRows(sheet, request, headerStyle);
            
            // Cabeçalhos da tabela
            createTableHeaders(sheet, 3, headerStyle);
            
            // Dados
            createDataRows(sheet, 4, dataStyle);
            
            // Ajusta largura das colunas
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(baos);
            
            log.info("Excel report generated successfully");
            return baos.toByteArray();
            
        } catch (Exception e) {
            log.error("Error generating Excel report", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private void createTitleRows(Sheet sheet, ReportRequest request, CellStyle style) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Relatório de " + request.getReportType());
        titleCell.setCellStyle(style);
        
        // Período
        Row periodRow = sheet.createRow(1);
        Cell periodCell = periodRow.createCell(0);
        periodCell.setCellValue(String.format("Período: %s até %s",
            request.getStartDate().format(formatter),
            request.getEndDate().format(formatter)
        ));
    }

    private void createTableHeaders(Sheet sheet, int rowNum, CellStyle style) {
        Row headerRow = sheet.createRow(rowNum);
        
        String[] headers = {"ID", "Descrição", "Valor", "Data", "Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private void createDataRows(Sheet sheet, int startRow, CellStyle style) {
        // TODO: Adicionar dados reais
        // Por enquanto, adiciona dados de exemplo
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(startRow + i);
            
            row.createCell(0).setCellValue("TRX-" + (i + 1));
            row.createCell(1).setCellValue("Transação de exemplo " + (i + 1));
            row.createCell(2).setCellValue("R$ 1.000,00");
            row.createCell(3).setCellValue("15/01/2025");
            row.createCell(4).setCellValue("MATCHED");
            
            for (int j = 0; j < 5; j++) {
                row.getCell(j).setCellStyle(style);
            }
        }
    }

    @Override
    public String getFormat() {
        return "EXCEL";
    }
}
