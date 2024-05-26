package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.EquipmentDto;
import com.vlsu.inventory.dto.model.PlacementDto;
import com.vlsu.inventory.dto.model.ResponsibleDto;
import com.vlsu.inventory.dto.model.SubcategoryDto;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExportServiceTest {
    private ExcelExportService excelExportService = new ExcelExportService();

    @Test
    void createReport_importCorrectly() throws IOException, InvalidFormatException {
        List<EquipmentDto.Response.Default> equipmentList = List.of(
                getTestEquipmentWithId(1L),
                getTestEquipmentWithId(2L),
                getTestEquipmentWithId(3L)
        );
        Workbook wb = this.excelExportService.createReport(equipmentList);
        String firstRowInventoryNumber = wb.getSheetAt(0).getRow(1).getCell(0).getStringCellValue();
        String secondRowInventoryNumber = wb.getSheetAt(0).getRow(2).getCell(0).getStringCellValue();
        String thirdRowInventoryNumber = wb.getSheetAt(0).getRow(3).getCell(0).getStringCellValue();

        String firstRowName = wb.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();
        String secondRowName = wb.getSheetAt(0).getRow(2).getCell(1).getStringCellValue();
        String thirdRowName = wb.getSheetAt(0).getRow(3).getCell(1).getStringCellValue();

        Assertions.assertEquals(firstRowInventoryNumber, equipmentList.get(0).getInventoryNumber());
        Assertions.assertEquals(secondRowInventoryNumber, equipmentList.get(1).getInventoryNumber());
        Assertions.assertEquals(thirdRowInventoryNumber, equipmentList.get(2).getInventoryNumber());

        Assertions.assertEquals(firstRowName, equipmentList.get(0).getName());
        Assertions.assertEquals(secondRowName, equipmentList.get(1).getName());
        Assertions.assertEquals(thirdRowName, equipmentList.get(2).getName());

        Assertions.assertEquals(equipmentList.size(), wb.getSheetAt(0).getLastRowNum());
    }

    private EquipmentDto.Response.Default getTestEquipmentWithId(Long id) {
        return EquipmentDto.Response.Default.builder()
                .id(id)
                .name("Оборудование №" + id)
                .inventoryNumber("00000000" + id)
                .commissioningDate(LocalDate.now())
                .commissioningActNumber("АМТС-" + id)
                .initialCost(BigDecimal.valueOf(10000D))
                .responsible(ResponsibleDto.Response.Default.builder().
                        lastName("Иванов")
                        .firstName("Иван")
                        .patronymic("Иванович").build())
                .placement(new PlacementDto.Response.Default(1L, "Аудитория №" + id))
                .subcategory(new SubcategoryDto.Response.WithoutCategory(1L, "Подкатегория №" + id))
                .build();
    }
}
