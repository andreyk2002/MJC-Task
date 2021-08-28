package com.epam.esm.mappers;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.validation.TagRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class TagMapperTest {

    private final TagMapper mapper = new TagMapperImpl();

    @Test
    void entityToResponseShouldMapValidEntity() {
        GiftTag tag = new GiftTag(1, "name");
        TagResponseDto response = new TagResponseDto(1, "name");
        TagResponseDto tagResponseDto = mapper.entityToResponse(tag);
        Assertions.assertEquals(response, tagResponseDto);
    }

    @Test
    void entityToResponseShouldReturnNullWhenEntityIsNull() {
        TagResponseDto tagResponseDto = mapper.entityToResponse(null);
        Assertions.assertNull(tagResponseDto);
    }

    @Test
    void entitiesToResponsesShouldMapValidEntities() {
        GiftTag firstTag = new GiftTag(1, "name");
        TagResponseDto firstTagResponse = new TagResponseDto(1, "name");
        GiftTag secondTag = new GiftTag(2, "second");
        TagResponseDto secondResponse = new TagResponseDto(2, "second");
        List<TagResponseDto> tags = mapper.entitiesToRequests(Arrays.asList(firstTag, secondTag));
        Assertions.assertEquals(Arrays.asList(firstTagResponse, secondResponse), tags);
    }

    @Test
    void entityToResponseShouldReturnNullWhenEntitiesIsNull() {
        List<TagResponseDto> tags = mapper.entitiesToRequests(null);
        Assertions.assertNull(tags);
    }

    @Test
    void requestToEntityShouldMapValidRequest() {
        GiftTag firstTag = new GiftTag(1, "name");
        GiftTag tag = mapper.requestToEntity(new TagRequestDto(1, "name"));
        Assertions.assertEquals(firstTag, tag);
    }

    @Test
    void entityToResponseShouldReturnNullWhenRequestIsNull() {
        GiftTag tags = mapper.requestToEntity(null);
        Assertions.assertNull(tags);
    }
}