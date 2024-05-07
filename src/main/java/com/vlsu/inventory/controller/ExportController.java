package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.model.EquipmentDto;
import com.vlsu.inventory.repository.EquipmentRepository;
import com.vlsu.inventory.service.EquipmentService;
import com.vlsu.inventory.service.ExcelExportService;
import com.vlsu.inventory.util.mapping.EquipmentMappingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/export")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ExportController {
    ExcelExportService excelExportService;
    EquipmentService equipmentService;
    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> getExcelReport(
            @RequestParam(required = false) String inventoryNumber,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal initialCostFrom,
            @RequestParam(required = false) BigDecimal initialCostTo,
            @RequestParam(required = false) LocalDate commissioningDateFrom,
            @RequestParam(required = false) LocalDate commissioningDateTo,
            @RequestParam(required = false) LocalDate decommissioningDateFrom,
            @RequestParam(required = false) LocalDate decommissioningDateTo,
            @RequestParam(required = false) String commissioningActNumber,
            @RequestParam(required = false) String decommissioningActNumber,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) Long responsibleId,
            @RequestParam(required = false) Long placementId
    ) {
        try {
            List<EquipmentDto.Response.Default> equipmentList = equipmentService.getAllByParams(inventoryNumber, name, initialCostFrom, initialCostTo,
                    commissioningDateFrom, commissioningDateTo, decommissioningDateFrom, decommissioningDateTo,
                    commissioningActNumber, decommissioningActNumber, subcategoryId, responsibleId, placementId);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx");
            Workbook workbook = excelExportService.createReport(equipmentList);
            workbook.write(stream);
            workbook.close();

            return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
