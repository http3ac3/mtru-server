package com.vlsu.inventory.controller;

import com.google.zxing.WriterException;
import com.vlsu.inventory.service.QrCodeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

@RestController
@RequestMapping("api/v1/qr")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QrCodeController {
    QrCodeService qrCodeService;
    @GetMapping(value = "/{inventoryNumber}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> getQrInventoryNumber(@PathVariable String inventoryNumber) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename(inventoryNumber)
                    .build());

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(qrCodeService.generateQr(inventoryNumber));
        } catch (WriterException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
