package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file, String directory) {
        try {
            // Используем абсолютный путь на основе текущей директории приложения
            String basePath = System.getProperty("user.dir");
            Path dirPath = Paths.get(basePath, uploadDir, directory).toAbsolutePath();
            Files.createDirectories(dirPath);

            // Генерируем уникальное имя файла
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID() + fileExtension;

            // Сохраняем файл
            Path filePath = dirPath.resolve(newFileName);
            Files.write(filePath, file.getBytes());

            // Возвращаем относительный путь для хранения в БД
            return "/uploads/" + directory + "/" + newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            String basePath = System.getProperty("user.dir");
            Path path = Paths.get(basePath, uploadDir, filePath.replaceFirst("^/", "")).toAbsolutePath();
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    @Override
    public String copyFile(String sourcePath, String directory) {
        try {
            // Проверяем, что исходный файл существует
            Path sourceFile = Paths.get(sourcePath).toAbsolutePath();
            if (!Files.exists(sourceFile)) {
                throw new RuntimeException("Source file not found: " + sourcePath);
            }

            // Создаём директорию назначения
            String basePath = System.getProperty("user.dir");
            Path dirPath = Paths.get(basePath, uploadDir, directory).toAbsolutePath();
            Files.createDirectories(dirPath);

            // Генерируем уникальное имя файла
            String originalFileName = sourceFile.getFileName().toString();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID() + fileExtension;

            // Копируем файл
            Path targetPath = dirPath.resolve(newFileName);
            Files.copy(sourceFile, targetPath);

            // Возвращаем относительный путь для хранения в БД
            return "/uploads/" + directory + "/" + newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy file: " + e.getMessage());
        }
    }
}
