package com.vlsu.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {
    private final String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Inventory Images";
    public String save(MultipartFile image, String inventoryNumber) throws Exception {
        createDirectoryIfNotExists();

        if (image.isEmpty()) throw new Exception("Невозможно сохранить пустой файл");

        Path imagePath = Paths.get(path, inventoryNumber + "." + image.getContentType().substring(6));
        image.transferTo(imagePath);
        return imagePath.toString();
    }

    private void createDirectoryIfNotExists() {
        File imageDirectory = new File(path);
        if (!imageDirectory.exists()) imageDirectory.mkdirs();
    }
}
