package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.model.EquipmentDto;
import com.vlsu.inventory.repository.EquipmentRepository;
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
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/export")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ExportController {
    ExcelExportService excelExportService;
    EquipmentRepository equipmentRepository;
    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> getExcelReport() {
        try {
            List<EquipmentDto.Response.Default> equipmentList =
                    equipmentRepository.findAll().stream().map(EquipmentMappingUtils::toDto).toList();
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
