package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.models.dtos.TagDto;
import com.spotlight.back.spotlight.models.entities.Tag;
import com.spotlight.back.spotlight.services.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    
    private final TagService tagService;
    
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody TagDto dto) {
        Tag tag = tagService.createTag(dto);
        return ResponseEntity.ok(tag);
    }
    
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable UUID id) {
        Tag tag = tagService.getTagById(id);
        return tag != null ? ResponseEntity.ok(tag) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable UUID id, @RequestBody TagDto dto) {
        Tag tag = tagService.updateTag(id, dto);
        return tag != null ? ResponseEntity.ok(tag) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        boolean deleted = tagService.deleteTag(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}