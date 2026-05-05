package com.alok.studentTracker.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(MultipartFile file, String directory);
    String copyFile(String sourcePath, String directory);
    void deleteFile(String filePath);
}
