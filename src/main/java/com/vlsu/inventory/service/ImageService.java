package com.vlsu.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageService {
    private final String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Inventory Images";
    public String save(MultipartFile image, Long inventoryNumber) throws Exception {
        createDirectoryIfNotExists();

        if (image.isEmpty()) throw new Exception("Невозможно сохранить пустой файл");

        Path imagePath = Paths.get(path, inventoryNumber + "." + image.getContentType().substring(6));
        image.transferTo(imagePath);
        return imagePath.toString();
    }

    public String getImageBase64String(String path) throws IOException {
        File image = new File(path);
        return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(path)));
    }

    public void deleteImage(String path) {
        new File(path).delete();
    }

    private void createDirectoryIfNotExists() {
        File imageDirectory = new File(path);
        if (!imageDirectory.exists()) imageDirectory.mkdirs();
    }
}
