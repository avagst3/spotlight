package com.spotlight.back.spotlight.models.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.TagDto;
import com.spotlight.back.spotlight.models.entities.TagProject;

@Component
public class TagInProjectConverter implements Converter<TagProject, TagDto> {
    
    @Override
    public TagDto convert(TagProject tagProject) {
        return TagDto.builder()
                .id(tagProject.getTag().getId())
                .label(tagProject.getTag().getLabel())
                .build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(TagProject.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(TagDto.class);
    }

}
