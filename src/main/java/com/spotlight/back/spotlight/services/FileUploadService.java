package com.spotlight.back.spotlight.services;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spotlight.back.spotlight.exceptions.BadRequestException;
import com.spotlight.back.spotlight.exceptions.InternalServerException;
import com.spotlight.back.spotlight.exceptions.errors.FileUploadError;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService implements InitializingBean{
    
    private final String basePath; 
    private Path rootLocation;

    public FileUploadService(@Value("${FILE_STORAGE_PATH:./uploads}") String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            this.rootLocation = Paths.get(basePath);
            Files.createDirectories(rootLocation.resolve("user"));
            Files.createDirectories(rootLocation.resolve("team"));
            Files.createDirectories(rootLocation.resolve("project/raw"));
            Files.createDirectories(rootLocation.resolve("project/data"));
            Files.createDirectories(rootLocation.resolve("project/video"));
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
            Path filePath = rootLocation.resolve(subDirectory);
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
            
            Path destination = filePath.resolve(filename);
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination);
            }

            return subDirectory + "/" + filename;
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