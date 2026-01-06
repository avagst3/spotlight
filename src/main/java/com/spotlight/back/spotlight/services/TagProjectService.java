package com.spotlight.back.spotlight.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spotlight.back.spotlight.exceptions.NotFoundException;
import com.spotlight.back.spotlight.exceptions.errors.ProjectError;
import com.spotlight.back.spotlight.models.converters.TagInProjectConverter;
import com.spotlight.back.spotlight.models.dtos.TagCreationDto;
import com.spotlight.back.spotlight.models.dtos.TagDto;
import com.spotlight.back.spotlight.models.entities.Tag;
import com.spotlight.back.spotlight.models.entities.TagProject;
import com.spotlight.back.spotlight.repositories.ProjectRepository;
import com.spotlight.back.spotlight.repositories.TagProjectRepository;
import com.spotlight.back.spotlight.repositories.TagRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagProjectService {
    
    private final TagProjectRepository tagProjectRepository;
    private final TagRepository tagRepository;
    private final TagInProjectConverter tagInProjectConverter;
    private final ProjectRepository projectRepository;
    
    @Transactional
    public TagDto createTagAndAddToProject(TagCreationDto tagDto) {
        TagProject tagProject = TagProject.builder()
            .tag(tagRepository.save(Tag.builder()
                .label(tagDto.getLabel())
                .fontColor(tagDto.getFontColor())
                .backgroundColor(tagDto.getBackgroundColor())
                .xCoordinate(tagDto.getXCoordinate())
                .yCoordinate(tagDto.getYCoordinate())
                .build()))
            .project(projectRepository.findById(tagDto.getProjectId()).orElseThrow(() -> new NotFoundException(ProjectError.PROJECT_NOT_FOUND, tagDto.getProjectId())))
            .build();
        return tagInProjectConverter.convert(tagProjectRepository.save(tagProject));
    }

    @Transactional
    public void deleteTagProject(TagProject tagProject) {
        tagProjectRepository.delete(tagProject);
    }

    @Transactional(readOnly = true)
    public List<TagDto> getTagsByProjectId(UUID projectId) {
        return tagProjectRepository.findByProjectId(projectId).stream()
            .map(tagProject -> TagDto.builder()
                .id(tagProject.getTag().getId())
                .label(tagProject.getTag().getLabel())
                .fontColor(tagProject.getTag().getFontColor())
                .backgroundColor(tagProject.getTag().getBackgroundColor())
                .xCoordinate(tagProject.getTag().getXCoordinate())
                .yCoordinate(tagProject.getTag().getYCoordinate())
                .build())
            .toList();
    }
}
