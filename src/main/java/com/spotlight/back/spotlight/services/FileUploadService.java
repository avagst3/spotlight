package com.spotlight.back.spotlight.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spotlight.back.spotlight.exceptions.BadRequestException;
import com.spotlight.back.spotlight.exceptions.InternalServerException;
import com.spotlight.back.spotlight.exceptions.errors.FileUploadError;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {
    
    private final Path uploadPath = Paths.get("uploads");

    public FileUploadService() {
        try {
            Files.createDirectories(uploadPath.resolve("user"));
            Files.createDirectories(uploadPath.resolve("team"));
            Files.createDirectories(uploadPath.resolve("project/raw"));
            Files.createDirectories(uploadPath.resolve("project/data"));
            Files.createDirectories(uploadPath.resolve("project/video"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory", e);
        }
    }

    public String uploadFile(MultipartFile file, String subDirectory) {
        if (file.isEmpty()) {
            throw new BadRequestException(FileUploadError.FILE_EMPTY, file.getOriginalFilename());
        }
        try {
            String filename = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(subDirectory);
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
            
            Path destination = filePath.resolve(filename);
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination);
            }

            return filename;
        } catch (Exception e) {
            throw new InternalServerException(FileUploadError.UPLOAD_FAILED, e);
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot);
    }
}