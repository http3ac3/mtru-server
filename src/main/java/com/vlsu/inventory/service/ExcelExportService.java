package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.EquipmentDto;
import com.vlsu.inventory.model.Equipment;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.List;

@Service
public class ExcelExportService {
    public Workbook createReport(List<EquipmentDto.Response.Default> equipmentList) throws IOException, InvalidFormatException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Оборудование");
        DataFormat fmt = workbook.createDataFormat();
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(fmt.getFormat("dd.mm.yyyy"));
        createHeader(sheet);

        for (int i = 0; i < equipmentList.size(); i++) {
            createEquipmentRow(i + 1, equipmentList.get(i), sheet, dateCellStyle);
        }

        return workbook;
    }

    private void createEquipmentRow(int position, EquipmentDto.Response.Default equipment, Sheet sheet, CellStyle dateCellStyle) {

        Row row = sheet.createRow(position);
        Cell cell = row.createCell(0);
        cell.setCellValue(equipment.getInventoryNumber());

        cell = row.createCell(1);
        cell.setCellValue(equipment.getName());

        cell = row.createCell(2);
        cell.setCellValue(equipment.getInitialCost().toString());

        Cell dateCell = row.createCell(3);
        dateCell.setCellStyle(dateCellStyle);
        dateCell.setCellValue(equipment.getCommissioningDate());

        cell = row.createCell(4);
        cell.setCellValue(equipment.getCommissioningActNumber());

        dateCell = row.createCell(5);
        dateCell.setCellStyle(dateCellStyle);
        dateCell.setCellValue(equipment.getDecommissioningDate());

        cell = row.createCell(6);
        cell.setCellValue(equipment.getDecommissioningActNumber());

        cell = row.createCell(7);
        cell.setCellValue(
                equipment.getResponsible().getLastName() + " " +
                equipment.getResponsible().getFirstName().charAt(0) + ". " +
                equipment.getResponsible().getPatronymic().charAt(0) + "."
        );

        cell = row.createCell(8);
        cell.setCellValue(equipment.getPlacement().getName());

        cell = row.createCell(9);
        cell.setCellValue(equipment.getSubcategory().getName());
    }
    private void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);

        Cell cell = header.createCell(0);
        cell.setCellValue("Инвентарный номер");

        cell = header.createCell(1);
        cell.setCellValue("Наименование");

        cell = header.createCell(2);
        cell.setCellValue("Начальная стоимость, руб");

        cell = header.createCell(3);
        cell.setCellValue("Дата ввода в эксплуатацию");

        cell = header.createCell(4);
        cell.setCellValue("Номер акта о вводе в эксплуатацию");

        cell = header.createCell(5);
        cell.setCellValue("Дата вывода из эксплуатации");

        cell = header.createCell(6);
        cell.setCellValue("Номер акта о выводе из эксплуатации");

        cell = header.createCell(7);
        cell.setCellValue("Ответственный");

        cell = header.createCell(8);
        cell.setCellValue("Помещение");

        cell = header.createCell(9);
        cell.setCellValue("Подкатегория");
    }
}
