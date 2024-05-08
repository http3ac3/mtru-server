package com.vlsu.inventory.controller;

import com.vlsu.inventory.service.ImportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/import")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ImportController {
    ImportService importService;
    @PostMapping("/excel")
    public ResponseEntity<?> fromExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println(file.getContentType());
            importService.fromExcel(file);
            return ResponseEntity.ok("Данные успешно загружены");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
