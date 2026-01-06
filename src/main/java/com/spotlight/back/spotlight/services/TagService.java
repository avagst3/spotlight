package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.converters.TagConverter;
import com.spotlight.back.spotlight.models.dtos.TagDto;
import com.spotlight.back.spotlight.models.entities.Tag;
import com.spotlight.back.spotlight.repositories.TagRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;
    
    @Transactional
    public TagDto createTag(TagDto dto) {
        Tag tag = Tag.builder()
                .label(dto.label)
                .xCoordinate(dto.xCoordinate)
                .yCoordinate(dto.yCoordinate)
                .fontSize(dto.fontSize)
                .fontColor(dto.fontColor)
                .backgroundColor(dto.backgroundColor)
                .build();
        
        return tagConverter.convert(tagRepository.save(tag));
    }
    
    @Transactional(readOnly = true)
    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
            .map(tagConverter::convert)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public TagDto getTagById(UUID id) {
        return tagConverter.convert(tagRepository.findById(id).orElse(null));
    }
    
    @Transactional
    public TagDto updateTag(UUID id, TagDto dto) {
        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag == null) return null;
        
        tag.setLabel(dto.label);
        tag.setXCoordinate(dto.xCoordinate);
        tag.setYCoordinate(dto.yCoordinate);
        if (dto.fontSize != null) tag.setFontSize(dto.fontSize);
        if (dto.fontColor != null) tag.setFontColor(dto.fontColor);
        if (dto.backgroundColor != null) tag.setBackgroundColor(dto.backgroundColor);
        
        return tagConverter.convert(tagRepository.save(tag));
    }
    
    @Transactional
    public boolean deleteTag(UUID id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            return true;
        }
        return false;
    }
}