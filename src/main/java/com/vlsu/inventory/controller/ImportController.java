package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.excel.ImportRequest;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.service.ImportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/import")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ImportController {
    ImportService importService;
    @PostMapping("/excel")
    public ResponseEntity<?> fromExcelFile(
            @ModelAttribute ImportRequest request,
            @AuthenticationPrincipal User principal
    ) {
        try {
            return ResponseEntity.ok(importService.fromExcel(request, principal));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
