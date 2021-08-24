package com.epam.esm.service;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.CertificateTagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

class CertificateTagServiceTest {

    private static final int CERTIFICATE_ID = 1;
    private static final TagResponseDto FIRST_TAG = new TagResponseDto(1, "first");
    private static final TagResponseDto SECOND_TAG = new TagResponseDto(2, "second");
    private static final List<TagResponseDto> CERTIFICATE_TAG_RESPONSE = Arrays.asList(FIRST_TAG, SECOND_TAG);
    private static final List<GiftTag> CERTIFICATE_TAGS = Arrays.asList(
            new GiftTag(1, "first"), new GiftTag(2, "second")
    );

    private CertificateTagService service;

    @BeforeEach
    void setUp() {
        TagMapper mapper = Mockito.mock(TagMapper.class);
        when(mapper.entitiesToRequests(CERTIFICATE_TAGS)).thenReturn(CERTIFICATE_TAG_RESPONSE);
        CertificateTagRepository certificateTagRepository = Mockito.mock(CertificateTagRepository.class);
        when(certificateTagRepository.getTagByCertificateId(anyLong())).thenReturn(CERTIFICATE_TAGS);
        service = new CertificateTagService(certificateTagRepository, mapper);
    }

    @Test
    void getTagsByCertificateId() {
        List<TagResponseDto> tags = service.getTagsByCertificateId(CERTIFICATE_ID);
        Assertions.assertEquals(tags, CERTIFICATE_TAG_RESPONSE);
    }
}