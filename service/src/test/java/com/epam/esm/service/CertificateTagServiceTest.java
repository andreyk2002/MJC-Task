package com.epam.esm.service;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.CertificateTagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

class CertificateTagServiceTest {


    @InjectMocks
    private CertificateTagService service;

    @Mock
    private final TagMapper mapper = mock(TagMapper.class);

    @Mock
    private final CertificateTagRepository certificateTagRepository = mock(CertificateTagRepository.class);

    @Test
    void getTagsByCertificateId() {
        int certificateId = 1;
        TagResponseDto first = new TagResponseDto(1, "first");
        TagResponseDto second = new TagResponseDto(2, "second");
        List<TagResponseDto> responseDtoList = Arrays.asList(first, second);
        List<GiftTag> giftTags = Arrays.asList(
                new GiftTag(1, "first"), new GiftTag(2, "second")
        );

        when(mapper.entitiesToRequests(anyList())).thenReturn(responseDtoList);
        when(certificateTagRepository.getTagByCertificateId(anyLong())).thenReturn(giftTags);
        service = new CertificateTagService(certificateTagRepository, mapper);

        List<TagResponseDto> tags = service.getTagsByCertificateId(certificateId);
        Assertions.assertEquals(tags, responseDtoList);
        verify(mapper).entitiesToRequests(giftTags);
        verify(certificateTagRepository).getTagByCertificateId(certificateId);
    }
}