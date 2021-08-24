package com.epam.esm.service;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.excepiton.TagAlreadyExistException;
import com.epam.esm.service.excepiton.TagNotFoundException;
import com.epam.esm.validation.TagRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class GiftTagServiceTest {

    private final static long FIRST_ID = 1;
    private final static long SECOND_ID = 2;
    private final static long INVALID_ID = 666;
    public static final TagResponseDto ADDED_RESPONSE = new TagResponseDto(3, "added");
    public static final long ADDED_ID = 3L;
    private GiftTagService service;
    private static final TagResponseDto FIRST_TAG_RESPONSE = new TagResponseDto(FIRST_ID, "first");
    private static final TagResponseDto SECOND_TAG_RESPONSE = new TagResponseDto(SECOND_ID, "second");
    private static final TagRequestDto FIRST_TAG_REQUEST = new TagRequestDto(FIRST_ID, "first");
    private static final GiftTag FIRST_TAG = new GiftTag(FIRST_ID, "first");
    private static final GiftTag SECOND_TAG = new GiftTag(SECOND_ID, "second");
    private static final TagRequestDto ADDED_TAG_REQUEST = new TagRequestDto(3, "added");
    private static final GiftTag ADDED_TAG = new GiftTag(3, "added");
    public static final List<GiftTag> ALL_TAGS = Arrays.asList(FIRST_TAG, SECOND_TAG);
    private static final List<TagResponseDto> ALL_TAGS_RESPONSES
            = Arrays.asList(FIRST_TAG_RESPONSE, SECOND_TAG_RESPONSE);

    @BeforeEach
    void setUp() {
        TagMapper mapper = Mockito.mock(TagMapper.class);
        when(mapper.entityToResponse(FIRST_TAG)).thenReturn(FIRST_TAG_RESPONSE);
        when(mapper.entityToResponse(SECOND_TAG)).thenReturn(SECOND_TAG_RESPONSE);
        when(mapper.entitiesToRequests(ALL_TAGS)).thenReturn(ALL_TAGS_RESPONSES);
        when(mapper.requestToEntity(ADDED_TAG_REQUEST)).thenReturn(ADDED_TAG);
        when(mapper.requestToEntity(FIRST_TAG_REQUEST)).thenReturn(FIRST_TAG);
        when(mapper.entityToResponse(ADDED_TAG)).thenReturn(ADDED_RESPONSE);
        TagRepository tagRepository = Mockito.mock(TagRepository.class);
        CertificateTagRepository certificateTagRepository = Mockito.mock(CertificateTagRepository.class);
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty());
        when(tagRepository.getById(FIRST_ID)).thenReturn(Optional.of(FIRST_TAG));
        when(tagRepository.getById(SECOND_ID)).thenReturn(Optional.of(SECOND_TAG));
        when(tagRepository.addTag(ADDED_TAG)).thenThrow(new RuntimeException());
        doThrow(new RuntimeException()).when(tagRepository).updateTag(FIRST_TAG);
        when(tagRepository.getAll()).thenReturn(ALL_TAGS);
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);
    }

    @Test
    public void getAllTagsShouldReturnAllTags() {
        List<TagResponseDto> allTags = service.getAllTags();
        Assertions.assertEquals(allTags, ALL_TAGS_RESPONSES);
    }

    @Test
    public void deleteByIdShouldReturnDeletedEntityIfExists() {
        TagResponseDto tagResponseDto = service.deleteById(FIRST_ID);
        Assertions.assertEquals(FIRST_TAG_RESPONSE, tagResponseDto);
    }

    @Test
    public void deleteByIdShouldThrowIfNotFound() {
        Assertions.assertThrows(TagNotFoundException.class, () -> service.deleteById(INVALID_ID));
    }

    @Test
    public void getByIdShouldReturnValidTagIfExists() {
        TagResponseDto result = service.getById(FIRST_ID);
        Assertions.assertEquals(FIRST_TAG_RESPONSE, result);
    }

    @Test
    public void getByIdShouldReturnThrowIfTagNotExists() {
        Assertions.assertThrows(TagNotFoundException.class, () -> service.getById(INVALID_ID));
    }

    @Test
    public void addTagShouldThrowIfAlreadyExists() {
        Assertions.assertThrows(TagAlreadyExistException.class, () -> service.addTag(FIRST_TAG_REQUEST));
    }

    @Test
    public void addTagShouldAddIfNotPresent() {
        Assertions.assertThrows(RuntimeException.class, () -> service.addTag(ADDED_TAG_REQUEST));
    }

    @Test
    public void updateTagShouldAddIfNotPresent() {
        Assertions.assertThrows(RuntimeException.class, () -> service.updateTag(ADDED_TAG_REQUEST));
    }

    @Test
    public void updateTagShouldUpdateIfExists() {
        Assertions.assertThrows(RuntimeException.class, () -> service.updateTag(FIRST_TAG_REQUEST));
    }
}