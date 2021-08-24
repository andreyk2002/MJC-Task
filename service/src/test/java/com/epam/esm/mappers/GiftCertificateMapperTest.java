package com.epam.esm.mappers;

import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.CertificateTagService;
import com.epam.esm.validation.CertificateRequestDto;
import com.epam.esm.validation.TagRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

class GiftCertificateMapperTest {

    private static final List<TagRequestDto> TAGS = Arrays.asList(
            new TagRequestDto(1, "name"),
            new TagRequestDto(2, "name")
    );

    private static final List<TagResponseDto> TAG_RESPONSES = Arrays.asList(
            new TagResponseDto(1, "name"),
            new TagResponseDto(2, "name")
    );
    private static final CertificateRequestDto REQUEST_DTO = new CertificateRequestDto(
            "test", "description", new BigDecimal(1), 10, TAGS
    );
    private static final GiftCertificate GIFT_CERTIFICATE = GiftCertificate.builder()
            .name("test").description("description").price(new BigDecimal(1)).duration(10).build();

    private static final CertificateResponseDto RESPONSE_DTO = CertificateResponseDto.builder()
            .name("test").description("description").price(new BigDecimal(1))
            .duration(10).tags(Collections.EMPTY_LIST).build();
    private static final int SECOND_ID = 2;
    private static final GiftCertificate SECOND_CERTIFICATE = GiftCertificate.builder()
            .id(SECOND_ID).name("new").build();
    private static final CertificateResponseDto SECOND_RESPONSE = CertificateResponseDto.builder()
            .id(SECOND_ID).name("new").tags(TAG_RESPONSES).build();

    private CertificateMapper mapper;

    @BeforeEach
    void setUp() {
        CertificateTagService service = Mockito.mock(CertificateTagService.class);
        when(service.getTagsByCertificateId(SECOND_ID)).thenReturn(TAG_RESPONSES);
        mapper = new CertificateMapperImpl();
        mapper.service = service;
    }

    @Test
    void requestToEntityShouldMapValidRequest() {
        GiftCertificate giftCertificate = mapper.requestToEntity(REQUEST_DTO);
        Assertions.assertEquals(GIFT_CERTIFICATE, giftCertificate);
    }

    @Test
    void requestToEntityShouldReturnNullWhenRequestIsNull() {
        GiftCertificate giftCertificate = mapper.requestToEntity(null);
        Assertions.assertNull(giftCertificate);
    }

    @Test
    void entityToResponseShouldMapValidEntity() {
        CertificateResponseDto certificateResponseDto = mapper.entityToResponse(GIFT_CERTIFICATE);
        Assertions.assertEquals(RESPONSE_DTO, certificateResponseDto);
    }

    @Test
    void requestToEntityShouldReturnNullWhenEntityIsNull() {
        CertificateResponseDto giftCertificate = mapper.entityToResponse(null);
        Assertions.assertNull(giftCertificate);
    }

    @Test
    void entitiesToRequestsShouldMapValidEntities() {
        List<CertificateResponseDto> responses = mapper.entitiesToResponses(Arrays.asList(GIFT_CERTIFICATE, SECOND_CERTIFICATE));
        Assertions.assertEquals(Arrays.asList(RESPONSE_DTO, SECOND_RESPONSE), responses);
    }

    @Test
    void requestToEntityShouldReturnNullWhenEntitiesIsNull() {
        List<CertificateResponseDto> giftCertificate = mapper.entitiesToResponses(null);
        Assertions.assertNull(giftCertificate);
    }
}