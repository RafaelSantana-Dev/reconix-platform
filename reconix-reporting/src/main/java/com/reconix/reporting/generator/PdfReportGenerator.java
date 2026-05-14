package com.reconix.reporting.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.reconix.reporting.dto.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class PdfReportGenerator implements ReportGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    @Override
    public byte[] generate(ReportRequest request) {
        log.info("Generating PDF report for tenant: {}", request.getTenantId());
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Título
            addTitle(document, request);
            
            // Informações do período
            addPeriodInfo(document, request);
            
            // Tabela de dados
            addDataTable(document, request);
            
            document.close();
            
            log.info("PDF report generated successfully");
            return baos.toByteArray();
            
        } catch (Exception e) {
            log.error("Error generating PDF report", e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void addTitle(Document document, ReportRequest request) throws DocumentException {
        Paragraph title = new Paragraph("Relatório de " + request.getReportType(), TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

    private void addPeriodInfo(Document document, ReportRequest request) throws DocumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        Paragraph period = new Paragraph(
            String.format("Período: %s até %s",
                request.getStartDate().format(formatter),
                request.getEndDate().format(formatter)
            ),
            NORMAL_FONT
        );
        period.setSpacingAfter(20);
        document.add(period);
    }

    private void addDataTable(Document document, ReportRequest request) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        
        // Cabeçalhos
        addTableHeader(table, "ID");
        addTableHeader(table, "Descrição");
        addTableHeader(table, "Valor");
        addTableHeader(table, "Data");
        addTableHeader(table, "Status");
        
        // TODO: Adicionar dados reais
        // Por enquanto, adiciona dados de exemplo
        for (int i = 1; i <= 10; i++) {
            addTableCell(table, "TRX-" + i);
            addTableCell(table, "Transação de exemplo " + i);
            addTableCell(table, "R$ 1.000,00");
            addTableCell(table, "15/01/2025");
            addTableCell(table, "MATCHED");
        }
        
        document.add(table);
    }

    private void addTableHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setPadding(5);
        table.addCell(cell);
    }

    @Override
    public String getFormat() {
        return "PDF";
    }
}
