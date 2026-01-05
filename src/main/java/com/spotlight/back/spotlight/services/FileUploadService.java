package com.spotlight.back.spotlight.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileUploadService {
    
    private final Path uploadPath = Paths.get("uploads");
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> fileProgress = new ConcurrentHashMap<>();
    
    public FileUploadService() throws IOException {
        Files.createDirectories(uploadPath);
    }
    
    public SseEmitter createUploadEmitter(String uploadId) {
        SseEmitter emitter = new SseEmitter(3600000L);
        emitters.put(uploadId, emitter);
        fileProgress.put(uploadId, 0L);
        
        emitter.onCompletion(() -> {
            emitters.remove(uploadId);
            fileProgress.remove(uploadId);
        });
        
        emitter.onTimeout(() -> {
            emitters.remove(uploadId);
            fileProgress.remove(uploadId);
        });
        
        return emitter;
    }
    
    public String uploadFile(MultipartFile file, String uploadId) throws IOException {
        String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);
        
        long totalBytes = file.getSize();
        long bytesCopied = 0;
        
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(filePath, 
                 StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            
            byte[] buffer = new byte[1024 * 1024];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                bytesCopied += bytesRead;

                int progress = (int) ((bytesCopied * 100) / totalBytes);
                fileProgress.put(uploadId, bytesCopied);

                SseEmitter emitter = emitters.get(uploadId);
                if (emitter != null) {
                    try {
                        emitter.send(SseEmitter.event()
                            .name("progress")
                            .data("{\"progress\":" + progress + ",\"bytesCopied\":" + bytesCopied + ",\"totalBytes\":" + totalBytes + "}"));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                }
            }
        }
        
        SseEmitter emitter = emitters.get(uploadId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name("complete")
                    .data("{\"filePath\":\"" + filePath.toString() + "\",\"fileName\":\"" + fileName + "\"}"));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
        
        return filePath.toString();
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot);
    }
}