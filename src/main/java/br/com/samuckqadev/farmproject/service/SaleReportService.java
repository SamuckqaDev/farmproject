package br.com.samuckqadev.farmproject.service;

import br.com.samuckqadev.farmproject.dto.duck.DuckReportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SaleReportService {

    public byte[] exportDetailedSalesToExcel(List<DuckReportDTO> ducks, LocalDateTime start, LocalDateTime end)
            throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Relatório");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // --- ESTILOS ---
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleStyle.setAlignment(HorizontalAlignment.LEFT);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setColor(IndexedColors.WHITE.getIndex());
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            styleBorders(titleStyle);

            // Estilo específico para as datas dentro do header azul (letra branca, menor e
            // alinhada à direita)
            CellStyle headerInfoStyle = workbook.createCellStyle();
            headerInfoStyle.cloneStyleFrom(titleStyle);
            headerInfoStyle.setAlignment(HorizontalAlignment.RIGHT);
            Font infoFont = workbook.createFont();
            infoFont.setColor(IndexedColors.WHITE.getIndex());
            infoFont.setFontHeightInPoints((short) 10);
            headerInfoStyle.setFont(infoFont);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            styleBorders(headerStyle);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle dataStyle = workbook.createCellStyle();
            styleBorders(dataStyle);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);

            // --- CABEÇALHO AZUL ---
            // Criamos as 2 primeiras linhas e aplicamos o estilo em todas as células de 0 a
            // 6
            for (int i = 0; i < 2; i++) {
                Row row = sheet.createRow(i);
                for (int j = 0; j <= 6; j++) {
                    Cell c = row.createCell(j);
                    c.setCellStyle(titleStyle);
                }
            }

            // Título principal à esquerda
            sheet.getRow(0).getCell(0).setCellValue("RELATÓRIO DE VENDA");
            // Mesclamos o título (colunas 0 a 3, linhas 0 a 1)
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 3));

            // Informações de data DENTRO do header azul (colunas 4 a 6)
            Row row0 = sheet.getRow(0);
            Cell cellGerado = row0.getCell(4);
            cellGerado.setCellValue("Gerado em: " + LocalDateTime.now().format(formatter));
            cellGerado.setCellStyle(headerInfoStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6)); // Mescla "Gerado em"

            Row row1 = sheet.getRow(1);
            Cell cellDatas = row1.getCell(4);
            String periodo = "Período: " + (start != null ? start.format(dateFormatter) : "-") +
                    " até " + (end != null ? end.format(dateFormatter) : "-");
            cellDatas.setCellValue(periodo);
            cellDatas.setCellStyle(headerInfoStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 6)); // Mescla "Período"

            // --- CABEÇALHO DA TABELA ---
            Row headerRow = sheet.createRow(2);
            String[] columns = { "Nome", "Status", "Cliente", "Tipo do Cliente", "Valor", "Data/hora", "Vendedor" };
            for (int i = 0; i < columns.length; i++) {
                createCell(headerRow, i, columns[i], headerStyle);
            }

            // --- PREENCHIMENTO DOS DADOS ---
            int rowIdx = 3;
            for (DuckReportDTO d : ducks) {
                Row row = sheet.createRow(rowIdx++);
                createCell(row, 0, d.duckName(), dataStyle);
                createCell(row, 1, d.status(), dataStyle);
                createCell(row, 2, d.customerName(), dataStyle);
                createCell(row, 3, d.customerType(), dataStyle);
                createCell(row, 4, d.value(), dataStyle);
                createCell(row, 5, d.saleDate(), dataStyle);
                createCell(row, 6, d.sellerName(), dataStyle);
            }

            for (int i = 0; i <= 6; i++)
                sheet.autoSizeColumn(i);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void createCell(Row row, int col, String val, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(val != null ? val : "-");
        cell.setCellStyle(style);
    }

    private void styleBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}