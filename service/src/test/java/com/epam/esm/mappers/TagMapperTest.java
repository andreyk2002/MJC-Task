package com.epam.esm.mappers;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.validation.TagRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class TagMapperTest {

    private static final GiftTag FIRST_TAG = new GiftTag(1, "name");
    private static final TagResponseDto RESPONSE = new TagResponseDto(1, "name");
    private static final GiftTag SECOND_TAG = new GiftTag(2, "second");
    private static final TagResponseDto SECOND_RESPONSE = new TagResponseDto(2, "second");
    private final TagMapper mapper = new TagMapperImpl();

    @Test
    void entityToResponseShouldMapValidEntity() {
        TagResponseDto tagResponseDto = mapper.entityToResponse(FIRST_TAG);
        Assertions.assertEquals(RESPONSE, tagResponseDto);
    }

    @Test
    void entityToResponseShouldReturnNullWhenEntityIsNull() {
        TagResponseDto tagResponseDto = mapper.entityToResponse(null);
        Assertions.assertNull(tagResponseDto);
    }

    @Test
    void entitiesToResponsesShouldMapValidEntities() {
        List<TagResponseDto> tags = mapper.entitiesToRequests(Arrays.asList(FIRST_TAG, SECOND_TAG));
        Assertions.assertEquals(Arrays.asList(RESPONSE, SECOND_RESPONSE), tags);
    }

    @Test
    void entityToResponseShouldReturnNullWhenEntitiesIsNull() {
        List<TagResponseDto> tags = mapper.entitiesToRequests(null);
        Assertions.assertNull(tags);
    }

    @Test
    void requestToEntityShouldMapValidRequest() {
        GiftTag tag = mapper.requestToEntity(new TagRequestDto(1, "name"));
        Assertions.assertEquals(FIRST_TAG, tag);
    }

    @Test
    void entityToResponseShouldReturnNullWhenRequestIsNull() {
        GiftTag tags = mapper.requestToEntity(null);
        Assertions.assertNull(tags);
    }
}