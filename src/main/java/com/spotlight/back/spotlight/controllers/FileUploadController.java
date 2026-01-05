package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.services.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    
    private final FileUploadService fileUploadService;
    
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    
    @PostMapping("/start")
    public ResponseEntity<String> startUpload() {
        String uploadId = UUID.randomUUID().toString();
        return ResponseEntity.ok(uploadId);
    }
    
    @GetMapping("/progress/{uploadId}")
    public SseEmitter getProgress(@PathVariable String uploadId) {
        return fileUploadService.createUploadEmitter(uploadId);
    }
    
    @PostMapping("/file/{uploadId}")
    public ResponseEntity<String> uploadFile(
            @PathVariable String uploadId,
            @RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileUploadService.uploadFile(file, uploadId);
            return ResponseEntity.ok(filePath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}