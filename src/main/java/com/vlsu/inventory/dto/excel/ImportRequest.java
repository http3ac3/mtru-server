package com.vlsu.inventory.dto.excel;

import com.vlsu.inventory.dto.model.PlacementDto;
import com.vlsu.inventory.dto.model.ResponsibleDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record ImportRequest(
        MultipartFile file,
        Long autoResponsibleId,
        Long autoPlacementId,
        LocalDate autoCommissioningDate,
        String autoCommissioningActNumber
        ) {}
