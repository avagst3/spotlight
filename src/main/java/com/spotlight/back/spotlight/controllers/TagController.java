package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.models.dtos.TagCreationDto;
import com.spotlight.back.spotlight.models.dtos.TagDto;
import com.spotlight.back.spotlight.services.TagProjectService;
import com.spotlight.back.spotlight.services.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    
    private final TagService tagService;
    private final TagProjectService tagProjectService;
    
    
    @Operation(summary = "Create a new tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag created successfully",content = @Content(schema = @Schema(implementation = TagDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/new")
    public ResponseEntity<TagDto> createTag(@RequestBody TagCreationDto dto) {
        TagDto tagDto = tagProjectService.createTagAndAddToProject(dto);
        return ResponseEntity.ok(tagDto);
    }
    
    @Operation(summary = "Get all tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tags retrieved successfully",content = @Content(schema = @Schema(implementation = TagDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
    
    @Operation(summary = "Get a tag by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag retrieved successfully",content = @Content(schema = @Schema(implementation = TagDto.class))),
        @ApiResponse(responseCode = "404", description = "Tag not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<TagDto> getTag(@PathVariable UUID id) {
        TagDto tagDto = tagService.getTagById(id);
        return tagDto != null ? ResponseEntity.ok(tagDto) : ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "Update a tag by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag updated successfully",content = @Content(schema = @Schema(implementation = TagDto.class))),
        @ApiResponse(responseCode = "404", description = "Tag not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<TagDto> updateTag(@PathVariable UUID id, @RequestBody TagDto dto) {
        TagDto tagDto = tagService.updateTag(id, dto);
        return tagDto != null ? ResponseEntity.ok(tagDto) : ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "Delete a tag by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tag deleted successfully",content = @Content(schema = @Schema(implementation = TagDto.class))),
        @ApiResponse(responseCode = "404", description = "Tag not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        boolean deleted = tagService.deleteTag(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}