package com.spotlight.back.spotlight.models.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.spotlight.back.spotlight.models.dtos.TagDto;
import com.spotlight.back.spotlight.models.entities.Tag;

@Component
public class TagConverter implements Converter<Tag, TagDto> {
    
    @Override
    public TagDto convert(Tag tag) {
        if (tag == null) return null;

        TagDto.TagDtoBuilder builder = TagDto.builder()
                .id(tag.getId())
                .label(tag.getLabel())
                .xCoordinate(tag.getXCoordinate())
                .yCoordinate(tag.getYCoordinate())
                .fontSize(tag.getFontSize())
                .fontColor(tag.getFontColor())
                .backgroundColor(tag.getBackgroundColor());

        return builder.build();
    }

    @Override
    public JavaType getInputType(TypeFactory arg0) {
        return arg0.constructType(Tag.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory arg0) {
        return arg0.constructType(TagDto.class);
    }

}
