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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        GiftTag firstGiftTag = new GiftTag(firstId, "first");
        GiftTag secondGiftTag = new GiftTag(secondId, "second");
        TagResponseDto firstResponse = new TagResponseDto(firstId, "first");
        TagResponseDto secondResponse = new TagResponseDto(secondId, "second");

        when(tagRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(
                Arrays.asList(firstGiftTag, secondGiftTag)
        ));
        when(mapper.entitiesToResponses(anyList())).thenReturn(Arrays.asList(firstResponse, secondResponse));


        List<TagResponseDto> all = Arrays.asList(firstResponse, secondResponse);
        List<TagResponseDto> allTags = service.getPage(pageable);
        Assertions.assertEquals(allTags, all);
        verify(mapper).entitiesToResponses(Arrays.asList(firstGiftTag, secondGiftTag));
        verify(tagRepository).findAll(pageable);
    }

    @Test
    void testDeleteByIdShouldReturnDeletedEntityIfExists() {
        long id = 100;
        TagResponseDto tag = new TagResponseDto(id, "first");
        GiftTag firstGiftTag = new GiftTag(id, "first");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(firstGiftTag));
        when(mapper.entityToResponse(any())).thenReturn(tag);

        TagResponseDto tagResponseDto = service.deleteById(id);
        Assertions.assertEquals(tag, tagResponseDto);
        verify(tagRepository).findById(id);
        verify(mapper).entityToResponse(firstGiftTag);
    }

    @Test
    void testDeleteByIdShouldThrowIfNotFound() {
        long invalidId = 666;
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(TagNotFoundException.class, () -> service.deleteById(invalidId));
        verify(tagRepository).findById(invalidId);
    }

    @Test
    void testGetByIdShouldReturnValidTagIfExists() {
        long id = 65;
        TagResponseDto response = new TagResponseDto(id, "some tag");
        GiftTag giftTag = new GiftTag(id, "some tag");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(giftTag));
        when(mapper.entityToResponse(any())).thenReturn(response);


        TagResponseDto result = service.getById(id);
        Assertions.assertEquals(response, result);
        verify(tagRepository).findById(id);
        verify(mapper).entityToResponse(giftTag);
    }

    @Test
    void testGetByIdShouldReturnThrowIfTagNotExists() {
        long notExitingId = 65;
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(TagNotFoundException.class, () -> service.getById(notExitingId));
        verify(tagRepository).findById(notExitingId);
    }

    @Test
    void testAddTagShouldThrowIfAlreadyExists() {
        long id = 15;
        GiftTag tag = new GiftTag(id, "fsdaf");
        TagRequestDtoCertificate requestDto = new TagRequestDtoCertificate(id, "fsdaf");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));

        Assertions.assertThrows(TagAlreadyExistException.class, () -> service.addTag(requestDto));
        verify(tagRepository).findById(id);
    }

    @Test
    void testAddTagShouldAddIfNotPresent() {
        long id = 15;
        GiftTag tag = new GiftTag(id, "fsdaf");
        TagRequestDtoCertificate requestDto = new TagRequestDtoCertificate(id, "fsdaf");
        TagResponseDto responseDto = new TagResponseDto(id, "fsdaf");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(tagRepository.save(any())).thenReturn(tag);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.certificateRequestToEntity(any())).thenReturn(tag);


        TagResponseDto result = service.addTag(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(tagRepository).findById(id);
        verify(tagRepository).save(tag);
        verify(mapper).entityToResponse(tag);
        verify(mapper).certificateRequestToEntity(requestDto);
    }


    @Test
    void testUpdateTagShouldAddIfNotPresent() {
        long id = 424242;
        GiftTag tag = new GiftTag(id, "123");
        TagRequestDtoCertificate requestDto = new TagRequestDtoCertificate(id, "123");
        TagResponseDto responseDto = new TagResponseDto(id, "123");
        when(tagRepository.save(any())).thenReturn(tag);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.certificateRequestToEntity(any())).thenReturn(tag);


        TagResponseDto result = service.updateTag(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(tagRepository).save(tag);
        verify(mapper).entityToResponse(tag);
        verify(mapper).certificateRequestToEntity(requestDto);
    }


}