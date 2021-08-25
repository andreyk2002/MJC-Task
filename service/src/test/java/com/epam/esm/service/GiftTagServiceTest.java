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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;


class GiftTagServiceTest {

    @InjectMocks
    private GiftTagService service;

    @Mock
    private final TagMapper mapper = mock(TagMapper.class);

    @Mock
    private final TagRepository tagRepository = mock(TagRepository.class);

    @Mock
    private final CertificateTagRepository certificateTagRepository = mock(CertificateTagRepository.class);


    @Test
    void getAllTagsShouldReturnAllTags() {
        long firstId = 1;
        long secondId = 2;
        GiftTag firstGiftTag = new GiftTag(firstId, "first");
        GiftTag secondGiftTag = new GiftTag(secondId, "second");
        TagResponseDto firstResponse = new TagResponseDto(firstId, "first");
        TagResponseDto secondResponse = new TagResponseDto(secondId, "second");
        when(tagRepository.getAll()).thenReturn(Arrays.asList(firstGiftTag, secondGiftTag));
        when(mapper.entitiesToRequests(anyList())).thenReturn(Arrays.asList(firstResponse, secondResponse));

        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);
        List<TagResponseDto> all = Arrays.asList(firstResponse, secondResponse);
        List<TagResponseDto> allTags = service.getAllTags();
        Assertions.assertEquals(allTags, all);
        verify(mapper).entitiesToRequests(Arrays.asList(firstGiftTag, secondGiftTag));
    }

    @Test
    void deleteByIdShouldReturnDeletedEntityIfExists() {
        long id = 100;
        TagResponseDto tag = new TagResponseDto(id, "first");
        GiftTag firstGiftTag = new GiftTag(id, "first");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.of(firstGiftTag));
        when(mapper.entityToResponse(any())).thenReturn(tag);
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);
        TagResponseDto tagResponseDto = service.deleteById(id);
        Assertions.assertEquals(tag, tagResponseDto);
        verify(tagRepository).getById(id);
        verify(mapper).entityToResponse(firstGiftTag);
    }

    @Test
    void deleteByIdShouldThrowIfNotFound() {
        long invalidId = 666;
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty());
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);
        Assertions.assertThrows(TagNotFoundException.class, () -> service.deleteById(invalidId));
        verify(tagRepository).getById(invalidId);
    }

    @Test
    void getByIdShouldReturnValidTagIfExists() {
        long id = 65;
        TagResponseDto response = new TagResponseDto(id, "some tag");
        GiftTag giftTag = new GiftTag(id, "some tag");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.of(giftTag));
        when(mapper.entityToResponse(any())).thenReturn(response);
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);

        TagResponseDto result = service.getById(id);
        Assertions.assertEquals(response, result);
        verify(tagRepository).getById(id);
        verify(mapper).entityToResponse(giftTag);
    }

    @Test
    void getByIdShouldReturnThrowIfTagNotExists() {
        long notExitingId = 65;
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty());
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);
        Assertions.assertThrows(TagNotFoundException.class, () -> service.getById(notExitingId));
        verify(tagRepository).getById(notExitingId);
    }

    @Test
    void addTagShouldThrowIfAlreadyExists() {
        long id = 15;
        GiftTag tag = new GiftTag(id, "fsdaf");
        TagRequestDto requestDto = new TagRequestDto(id, "fsdaf");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.of(tag));
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);
        Assertions.assertThrows(TagAlreadyExistException.class, () -> service.addTag(requestDto));
        verify(tagRepository).getById(id);
    }

    @Test
    void addTagShouldAddIfNotPresent() {
        long id = 15;
        GiftTag tag = new GiftTag(id, "fsdaf");
        TagRequestDto requestDto = new TagRequestDto(id, "fsdaf");
        TagResponseDto responseDto = new TagResponseDto(id, "fsdaf");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty()).thenReturn(Optional.of(tag));
        when(tagRepository.addTag(any())).thenReturn(id);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.requestToEntity(any())).thenReturn(tag);
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);

        TagResponseDto result = service.addTag(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(tagRepository, times(2)).getById(id);
        verify(tagRepository).addTag(tag);
        verify(mapper).entityToResponse(tag);
        verify(mapper).requestToEntity(requestDto);
    }

    @Test
    void updateTagShouldUpdateIfExists() {
        long id = 555;
        GiftTag tag = new GiftTag(id, "fdasfasdf");
        TagRequestDto requestDto = new TagRequestDto(id, "fdasfasdf");
        TagResponseDto responseDto = new TagResponseDto(id, "fdasfasdf");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.of(tag));
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.requestToEntity(any())).thenReturn(tag);
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);


        TagResponseDto result = service.updateTag(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(tagRepository, times(2)).getById(id);
        verify(mapper).entityToResponse(tag);
        verify(mapper).requestToEntity(requestDto);

    }

    @Test
    void updateTagShouldAddIfNotPresent() {
        long id = 424242;
        GiftTag tag = new GiftTag(id, "123");
        TagRequestDto requestDto = new TagRequestDto(id, "123");
        TagResponseDto responseDto = new TagResponseDto(id, "123");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty()).thenReturn(Optional.of(tag));
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.requestToEntity(any())).thenReturn(tag);
        service = new GiftTagService(tagRepository, certificateTagRepository, mapper);

        TagResponseDto result = service.updateTag(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(tagRepository).getById(id);
        verify(mapper).entityToResponse(tag);
        verify(mapper).requestToEntity(requestDto);
    }
}