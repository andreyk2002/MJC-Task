package com.epam.esm.mappers;

import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.CertificateTagService;
import com.epam.esm.validation.CertificateRequestDto;
import com.epam.esm.validation.TagRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class GiftCertificateMapperTest {

    private final CertificateMapper mapper = new CertificateMapperImpl();

    private final CertificateTagService service = mock(CertificateTagService.class);


    @Test
    void testRequestToEntityShouldMapValidRequest() {
        List<TagRequestDto> tags = Arrays.asList(
                new TagRequestDto(1, "name"),
                new TagRequestDto(2, "name")
        );
        CertificateRequestDto requestDto = new CertificateRequestDto(
                "test", "description", new BigDecimal(1), 10, tags
        );
        GiftCertificate certificate = GiftCertificate.builder()
                .name("test").description("description").price(new BigDecimal(1)).duration(10).build();

        GiftCertificate giftCertificate = mapper.requestToEntity(requestDto);
        Assertions.assertEquals(certificate, giftCertificate);
    }

    @Test
    void testRequestToEntityShouldReturnNullWhenRequestIsNull() {
        GiftCertificate giftCertificate = mapper.requestToEntity(null);
        Assertions.assertNull(giftCertificate);
    }

    @Test
    void testEntityToResponseShouldMapValidEntity() {
        List<TagResponseDto> responses = Arrays.asList(
                new TagResponseDto(1, "name"),
                new TagResponseDto(2, "name")
        );
        GiftCertificate certificate = GiftCertificate.builder()
                .name("test").description("description").price(new BigDecimal(1)).duration(10).build();

        CertificateResponseDto RESPONSE_DTO = CertificateResponseDto.builder()
                .name("test").description("description").price(new BigDecimal(1))
                .duration(10).tags(Collections.emptyList()).build();

        int secondId = 2;
        when(service.getTagsByCertificateId(secondId)).thenReturn(responses);
        mapper.service = service;
        CertificateResponseDto certificateResponseDto = mapper.entityToResponse(certificate);
        Assertions.assertEquals(RESPONSE_DTO, certificateResponseDto);
    }

    @Test
    void testRequestToEntityShouldReturnNullWhenEntityIsNull() {
        CertificateResponseDto giftCertificate = mapper.entityToResponse(null);
        Assertions.assertNull(giftCertificate);
    }

    @Test
    void testEntitiesToRequestsShouldMapValidEntities() {
        List<TagResponseDto> responses = Arrays.asList(
                new TagResponseDto(1, "name"),
                new TagResponseDto(2, "name")
        );
        GiftCertificate certificate = GiftCertificate.builder()
                .name("test").description("description").price(new BigDecimal(1)).duration(10).build();

        CertificateResponseDto responseDto = CertificateResponseDto.builder()
                .name("test").description("description").price(new BigDecimal(1))
                .duration(10).tags(Collections.emptyList()).build();
        int secondId = 2;
        GiftCertificate secondCertificate = GiftCertificate.builder()
                .id(secondId).name("new").build();
        CertificateResponseDto secondResponse = CertificateResponseDto.builder()
                .id(secondId).name("new").tags(responses).build();

        when(service.getTagsByCertificateId(secondId)).thenReturn(responses);
        mapper.service = service;
        List<CertificateResponseDto> results = mapper.entitiesToResponses(Arrays.asList(certificate, secondCertificate));
        Assertions.assertEquals(Arrays.asList(responseDto, secondResponse), results);
        verify(service).getTagsByCertificateId(secondId);
    }

    @Test
    void testRequestToEntityShouldReturnNullWhenEntitiesIsNull() {
        when(service.getTagsByCertificateId(anyLong())).thenReturn(Collections.emptyList());
        List<CertificateResponseDto> giftCertificate = mapper.entitiesToResponses(null);
        Assertions.assertNull(giftCertificate);
    }
}