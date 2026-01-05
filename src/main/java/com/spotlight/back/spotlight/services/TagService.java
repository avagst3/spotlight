package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.dtos.TagDto;
import com.spotlight.back.spotlight.models.entities.Tag;
import com.spotlight.back.spotlight.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {
    
    private final TagRepository tagRepository;
    
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    
    public Tag createTag(TagDto dto) {
        Tag tag = new Tag(dto.label, dto.xCoordinate, dto.yCoordinate);
        
        if (dto.fontSize != null) tag.setFontSize(dto.fontSize);
        if (dto.fontColor != null) tag.setFontColor(dto.fontColor);
        if (dto.backgroundColor != null) tag.setBackgroundColor(dto.backgroundColor);
        
        return tagRepository.save(tag);
    }
    
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
    
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id).orElse(null);
    }
    
    public Tag updateTag(UUID id, TagDto dto) {
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) return null;
        
        tag.setLabel(dto.label);
        tag.setXCoordinate(dto.xCoordinate);
        tag.setYCoordinate(dto.yCoordinate);
        if (dto.fontSize != null) tag.setFontSize(dto.fontSize);
        if (dto.fontColor != null) tag.setFontColor(dto.fontColor);
        if (dto.backgroundColor != null) tag.setBackgroundColor(dto.backgroundColor);
        
        return tagRepository.save(tag);
    }
    
    public boolean deleteTag(UUID id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            return true;
        }
        return false;
    }
}