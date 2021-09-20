package com.epam.esm.mappers;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.request.CertificateRequestDto;
import com.epam.esm.request.TagRequestDtoCertificate;
import com.epam.esm.response.CertificateResponseDto;
import com.epam.esm.response.TagResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateMapperTest {

    @InjectMocks
    private final CertificateMapper mapper = new CertificateMapperImpl();
    @Mock
    private final TagMapper tagMapper = mock(TagMapper.class);


    @Test
    void testRequestToEntityShouldMapValidRequest() {

        List<TagRequestDtoCertificate> tags = Arrays.asList(
                new TagRequestDtoCertificate(1, "name"),
                new TagRequestDtoCertificate(2, "name")
        );
        CertificateRequestDto requestDto = new CertificateRequestDto(0,
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

        GiftCertificate certificate = GiftCertificate.builder()
                .name("test").description("description").price(new BigDecimal(1)).duration(10).build();

        CertificateResponseDto responseDto = CertificateResponseDto.builder()
                .name("test").description("description").price(new BigDecimal(1))
                .duration(10).tags(Collections.emptyList()).build();

        CertificateResponseDto certificateResponseDto = mapper.entityToResponse(certificate);
        Assertions.assertEquals(responseDto, certificateResponseDto);
    }

    @Test
    void testRequestToEntityShouldReturnNullWhenEntityIsNull() {
        CertificateResponseDto giftCertificate = mapper.entityToResponse(null);
        Assertions.assertNull(giftCertificate);
    }

    @Test
    void testEntitiesToResponsesShouldMapValidEntities() {
        List<TagResponseDto> tagResponses = Arrays.asList(
                new TagResponseDto(1, "name"),
                new TagResponseDto(2, "name")
        );

        Set<GiftTag> tags = Set.of(
                new GiftTag(1, "name"),
                new GiftTag(2, "name")
        );


        GiftCertificate certificate = GiftCertificate.builder()
                .name("test").description("description").tags(Collections.emptySet())
                .price(new BigDecimal(1)).duration(10).build();

        CertificateResponseDto responseDto = CertificateResponseDto.builder()
                .name("test").description("description").price(new BigDecimal(1))
                .duration(10).tags(Collections.emptyList()).build();
        int secondId = 2;
        GiftCertificate secondCertificate = GiftCertificate.builder()
                .tags(tags).id(secondId).name("new").build();
        CertificateResponseDto secondResponse = CertificateResponseDto.builder()
                .id(secondId).name("new").tags(tagResponses).build();
        when(tagMapper.entitiesToResponses(anySet())).thenReturn(tagResponses);
        when(tagMapper.entitiesToResponses(Collections.emptySet())).thenReturn(Collections.emptyList());

        List<CertificateResponseDto> results = mapper.entitiesToResponses(Arrays.asList(certificate, secondCertificate));

        Assertions.assertEquals(Arrays.asList(responseDto, secondResponse), results);
        verify(tagMapper).entitiesToResponses(tags);
    }

    @Test
    void testRequestToEntityShouldReturnNullWhenEntitiesIsNull() {
        List<CertificateResponseDto> giftCertificate = mapper.entitiesToResponses(null);
        Assertions.assertNull(giftCertificate);
    }
}