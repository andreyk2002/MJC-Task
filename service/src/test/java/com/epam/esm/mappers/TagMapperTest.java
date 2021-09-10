package com.epam.esm.mappers;

import com.epam.esm.entity.GiftTag;
import com.epam.esm.request.TagRequestDto;
import com.epam.esm.request.TagRequestDtoCertificate;
import com.epam.esm.response.TagResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

class TagMapperTest {

    private final TagMapper mapper = new TagMapperImpl();

    @Test
    void testEntityToResponseShouldMapValidEntity() {
        GiftTag tag = new GiftTag(1, "name");
        TagResponseDto response = new TagResponseDto(1, "name");
        TagResponseDto tagResponseDto = mapper.entityToResponse(tag);
        Assertions.assertEquals(response, tagResponseDto);
    }

    @Test
    void testEntityToResponseShouldReturnNullWhenEntityIsNull() {
        TagResponseDto tagResponseDto = mapper.entityToResponse(null);
        Assertions.assertNull(tagResponseDto);
    }

    @Test
    void testRequestsToEntitiesShouldParseRequestsToEntities() {
        TagRequestDtoCertificate firstRequest = new TagRequestDtoCertificate(1, "name");
        TagRequestDtoCertificate secondRequest = new TagRequestDtoCertificate(2, "fff");
        GiftTag firstTag = new GiftTag(1, "name");
        GiftTag secondTag = new GiftTag(2, "fff");

        List<TagRequestDtoCertificate> givenTags = Arrays.asList(firstRequest, secondRequest);
        Set<GiftTag> expected = Set.of(firstTag, secondTag);
        Set<GiftTag> result = mapper.requestsToEntities(givenTags);
        Assertions.assertEquals(expected, result);
    }


    @Test
    void testRequestsToEntitiesShouldParseRequestsSetToEntities() {
        TagRequestDtoCertificate firstRequest = new TagRequestDtoCertificate(1, "name");
        TagRequestDtoCertificate secondRequest = new TagRequestDtoCertificate(2, "fff");
        GiftTag firstTag = new GiftTag(1, "name");
        GiftTag secondTag = new GiftTag(2, "fff");

        Set<TagRequestDtoCertificate> givenTags = Set.of(firstRequest, secondRequest);
        Set<GiftTag> expected = Set.of(firstTag, secondTag);
        Set<GiftTag> result = mapper.requestsToEntities(givenTags);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testRequestsToEntitiesShouldReturnNullWhenEntitiesIsNull() {
        Set<GiftTag> tagResponseDto = mapper.requestsToEntities((List<TagRequestDtoCertificate>) null);
        Assertions.assertNull(tagResponseDto);
    }

    @Test
    void testResponseToEntityShouldMapResponseToEntity() {
        TagResponseDto tagResponseDto = new TagResponseDto(1, "re");
        GiftTag expected = new GiftTag(1, "re");
        GiftTag giftTag = mapper.responseToEntity(tagResponseDto);
        Assertions.assertEquals(expected, giftTag);
    }

    @Test
    void testResponseToEntityShouldReturnNullWhenRequestIsNull() {
        GiftTag tagResponseDto = mapper.responseToEntity(null);
        Assertions.assertNull(tagResponseDto);
    }

    @Test
    void testRequestToEntityShouldMapResponseToEntity() {
        TagRequestDto tagResponseDto = new TagRequestDto("re");
        GiftTag expected = new GiftTag(0, "re");
        GiftTag giftTag = mapper.requestToEntity(tagResponseDto);
        Assertions.assertEquals(expected, giftTag);
    }

    @Test
    void testResponsesToEntitiesShouldParseResponsesToEntities() {
        TagResponseDto firstRequest = new TagResponseDto(1, "name");
        TagResponseDto secondRequest = new TagResponseDto(2, "fff");
        GiftTag firstTag = new GiftTag(1, "name");
        GiftTag secondTag = new GiftTag(2, "fff");

        List<TagResponseDto> givenTags = Arrays.asList(firstRequest, secondRequest);
        List<GiftTag> expected = Arrays.asList(firstTag, secondTag);
        List<GiftTag> result = mapper.responsesToEntities(givenTags);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testRequestToEntityShouldReturnNullWhenRequestIsNull() {
        GiftTag tagResponseDto = mapper.requestToEntity(null);
        Assertions.assertNull(tagResponseDto);
    }

    @Test
    void testEntitiesToResponsesShouldMapValidEntities() {
        GiftTag firstTag = new GiftTag(1, "name");
        TagResponseDto firstTagResponse = new TagResponseDto(1, "name");
        GiftTag secondTag = new GiftTag(2, "second");
        TagResponseDto secondResponse = new TagResponseDto(2, "second");
        List<TagResponseDto> tags = mapper.entitiesToResponses(Arrays.asList(firstTag, secondTag));
        Assertions.assertEquals(Arrays.asList(firstTagResponse, secondResponse), tags);
    }

    @Test
    void testEntityToResponseShouldReturnNullWhenEntitiesIsNull() {
        List<TagResponseDto> tags = mapper.entitiesToResponses((List<GiftTag>) null);
        Assertions.assertNull(tags);
    }

    @Test
    void testRequestToEntityShouldMapValidRequest() {
        GiftTag firstTag = new GiftTag(1, "name");
        GiftTag tag = mapper.certificateRequestToEntity(new TagRequestDtoCertificate(1, "name"));
        Assertions.assertEquals(firstTag, tag);
    }

    @Test
    void testEntityToResponseShouldReturnNullWhenRequestIsNull() {
        GiftTag tags = mapper.certificateRequestToEntity(null);
        Assertions.assertNull(tags);
    }
}