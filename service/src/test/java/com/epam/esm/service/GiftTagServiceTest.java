package com.epam.esm.service;

import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.request.TagRequestDtoCertificate;
import com.epam.esm.response.TagResponseDto;
import com.epam.esm.service.excepiton.TagAlreadyExistException;
import com.epam.esm.service.excepiton.TagNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftTagServiceTest {

    @InjectMocks
    private GiftTagService service;

    @Mock
    private TagMapper mapper;

    @Mock
    private TagRepository tagRepository;


    @Test
    void testGetPageShouldReturnTagsPage() {
        long firstId = 1;
        long secondId = 2;
        int offset = 0;
        int size = 10;
        GiftTag firstGiftTag = new GiftTag(firstId, "first");
        GiftTag secondGiftTag = new GiftTag(secondId, "second");
        TagResponseDto firstResponse = new TagResponseDto(firstId, "first");
        TagResponseDto secondResponse = new TagResponseDto(secondId, "second");
        when(tagRepository.getPage(anyInt(), anyInt())).thenReturn(Arrays.asList(firstGiftTag, secondGiftTag));
        when(mapper.entitiesToResponses(anyList())).thenReturn(Arrays.asList(firstResponse, secondResponse));


        List<TagResponseDto> all = Arrays.asList(firstResponse, secondResponse);
        List<TagResponseDto> allTags = service.getPage(offset, size);
        Assertions.assertEquals(allTags, all);
        verify(mapper).entitiesToResponses(Arrays.asList(firstGiftTag, secondGiftTag));
        verify(tagRepository).getPage(offset, size);
    }

    @Test
    void testDeleteByIdShouldReturnDeletedEntityIfExists() {
        long id = 100;
        TagResponseDto tag = new TagResponseDto(id, "first");
        GiftTag firstGiftTag = new GiftTag(id, "first");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.of(firstGiftTag));
        when(mapper.entityToResponse(any())).thenReturn(tag);

        TagResponseDto tagResponseDto = service.deleteById(id);
        Assertions.assertEquals(tag, tagResponseDto);
        verify(tagRepository).getById(id);
        verify(mapper).entityToResponse(firstGiftTag);
    }

    @Test
    void testDeleteByIdShouldThrowIfNotFound() {
        long invalidId = 666;
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(TagNotFoundException.class, () -> service.deleteById(invalidId));
        verify(tagRepository).getById(invalidId);
    }

    @Test
    void testGetByIdShouldReturnValidTagIfExists() {
        long id = 65;
        TagResponseDto response = new TagResponseDto(id, "some tag");
        GiftTag giftTag = new GiftTag(id, "some tag");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.of(giftTag));
        when(mapper.entityToResponse(any())).thenReturn(response);


        TagResponseDto result = service.getById(id);
        Assertions.assertEquals(response, result);
        verify(tagRepository).getById(id);
        verify(mapper).entityToResponse(giftTag);
    }

    @Test
    void testGetByIdShouldReturnThrowIfTagNotExists() {
        long notExitingId = 65;
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(TagNotFoundException.class, () -> service.getById(notExitingId));
        verify(tagRepository).getById(notExitingId);
    }

    @Test
    void testAddTagShouldThrowIfAlreadyExists() {
        long id = 15;
        GiftTag tag = new GiftTag(id, "fsdaf");
        TagRequestDtoCertificate requestDto = new TagRequestDtoCertificate(id, "fsdaf");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.of(tag));

        Assertions.assertThrows(TagAlreadyExistException.class, () -> service.addTag(requestDto));
        verify(tagRepository).getById(id);
    }

    @Test
    void testAddTagShouldAddIfNotPresent() {
        long id = 15;
        GiftTag tag = new GiftTag(id, "fsdaf");
        TagRequestDtoCertificate requestDto = new TagRequestDtoCertificate(id, "fsdaf");
        TagResponseDto responseDto = new TagResponseDto(id, "fsdaf");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty());
        when(tagRepository.addTag(any())).thenReturn(tag);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.certificateRequestToEntity(any())).thenReturn(tag);


        TagResponseDto result = service.addTag(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(tagRepository).getById(id);
        verify(tagRepository).addTag(tag);
        verify(mapper).entityToResponse(tag);
        verify(mapper).certificateRequestToEntity(requestDto);
    }

    @Test
    void testUpdateTagShouldUpdateIfExists() {
        long id = 555;
        GiftTag tag = new GiftTag(id, "fdasfasdf");
        TagRequestDtoCertificate requestDto = new TagRequestDtoCertificate(id, "fdasfasdf");
        TagResponseDto responseDto = new TagResponseDto(id, "fdasfasdf");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.of(tag));
        when(mapper.entityToResponse(any())).thenReturn(responseDto);


        TagResponseDto result = service.updateTag(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(tagRepository, times(2)).getById(id);
        verify(mapper).entityToResponse(tag);
        verify(mapper).certificateRequestToEntity(requestDto);

    }

    @Test
    void testUpdateTagShouldAddIfNotPresent() {
        long id = 424242;
        GiftTag tag = new GiftTag(id, "123");
        TagRequestDtoCertificate requestDto = new TagRequestDtoCertificate(id, "123");
        TagResponseDto responseDto = new TagResponseDto(id, "123");
        when(tagRepository.getById(anyLong())).thenReturn(Optional.empty()).thenReturn(Optional.of(tag));
        when(tagRepository.addTag(any())).thenReturn(tag);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.certificateRequestToEntity(any())).thenReturn(tag);


        TagResponseDto result = service.updateTag(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(tagRepository).addTag(tag);
        verify(tagRepository).getById(id);
        verify(mapper).entityToResponse(tag);
        verify(mapper).certificateRequestToEntity(requestDto);
    }


}