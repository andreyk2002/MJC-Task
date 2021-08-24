package com.epam.esm.service;

import com.epam.esm.NullableFieldsFinder;
import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mappers.CertificateMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.service.excepiton.CertificateNotFoundException;
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
import java.util.Optional;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class CertificateServiceTest {

    private static final int NOT_EXISTED_ID = 666;
    private static final int FIRST_ID = 1;
    private static final GiftCertificate FIRST_CERTIFICATE = GiftCertificate.builder()
            .id(FIRST_ID).name("first").build();
    private static final CertificateResponseDto FIRST_RESPONSE = CertificateResponseDto.builder()
            .id(FIRST_ID).name("first").build();
    private static final int SECOND_ID = 2;
    public static final int TAG_ID = 1;
    private static final Long ADDED_ID = 3L;
    private static final CertificateResponseDto SECOND_RESPONSE = CertificateResponseDto.builder().id(SECOND_ID).name("first").build();
    private static final GiftCertificate SECOND_CERTIFICATE = GiftCertificate.builder().id(SECOND_ID).name("first").build();
    private static final List<GiftCertificate> CERTIFICATES = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE);
    private static final List<CertificateResponseDto> CERTIFICATE_RESPONSES = Arrays.asList(FIRST_RESPONSE, SECOND_RESPONSE);
    private static final List<TagRequestDto> TAG_REQUESTS = Collections.singletonList(new TagRequestDto(TAG_ID, "tag"));
    public static final TagResponseDto TAG_RESPONSE = new TagResponseDto(TAG_ID, "tag");
    private static final List<TagResponseDto> TAG_RESPONSES = Collections.singletonList(TAG_RESPONSE);
    private static final CertificateRequestDto TEST_CERTIFICATE = new CertificateRequestDto(
            "newName", "newDescription", new BigDecimal(1), 10, TAG_REQUESTS);
    private static final CertificateResponseDto ADDED_CERTIFICATE = CertificateResponseDto.builder()
            .id(FIRST_ID).name("newName").description("newDescription").price(new BigDecimal(1)).duration(10)
            .tags(TAG_RESPONSES).build();
    private static final GiftCertificate UPDATE_CERTIFICATE_ENTITY = GiftCertificate.builder()
            .id(FIRST_ID).name("newName").description("newDescription").price(new BigDecimal(1)).duration(10)
            .build();
    private static final GiftCertificate ADDED_CERTIFICATE_ENTITY = GiftCertificate.builder()
            .id(ADDED_ID).name("newName").description("newDescription").price(new BigDecimal(1)).duration(10)
            .build();
    private CertificateService service;

    @BeforeEach
    void setUp() {
        CertificateRepository certificateRepo = Mockito.mock(CertificateRepository.class);
        when(certificateRepo.getById(anyLong())).thenReturn(Optional.empty());
        when(certificateRepo.getById(FIRST_ID)).thenReturn(Optional.of(FIRST_CERTIFICATE));
        when(certificateRepo.getById(ADDED_ID)).thenReturn(Optional.of(ADDED_CERTIFICATE_ENTITY));
        doThrow(new RuntimeException("test exception")).when(certificateRepo).updateCertificate(any());
        when(certificateRepo.getAllSorted(anyString(), anyString(), anyString(), anyString())).thenReturn(CERTIFICATES);
        when(certificateRepo.addCertificate(any())).thenReturn(ADDED_ID);

        GiftTagService tagService = Mockito.mock(GiftTagService.class);
        when(tagService.addTag(any())).thenReturn(TAG_RESPONSE);
        when(tagService.updateTag(any())).thenReturn(TAG_RESPONSE);

        CertificateMapper mapper = Mockito.mock(CertificateMapper.class);
        when(mapper.entityToResponse(FIRST_CERTIFICATE)).thenReturn(FIRST_RESPONSE);
        when(mapper.entityToResponse(SECOND_CERTIFICATE)).thenReturn(SECOND_RESPONSE);
        when(mapper.entitiesToResponses(CERTIFICATES)).thenReturn(CERTIFICATE_RESPONSES);
        when(mapper.requestToEntity(TEST_CERTIFICATE)).thenReturn(ADDED_CERTIFICATE_ENTITY);
        when(mapper.entityToResponse(ADDED_CERTIFICATE_ENTITY)).thenReturn(ADDED_CERTIFICATE);

        CertificateTagRepository certificateTagRepository = Mockito.mock(CertificateTagRepository.class);
        NullableFieldsFinder nullableFieldsFinder = Mockito.mock(NullableFieldsFinder.class);
        when(nullableFieldsFinder.getNullPropertyNames(any())).thenReturn(new String[]{});

        service = new CertificateService(certificateRepo, tagService, mapper, certificateTagRepository, nullableFieldsFinder);
    }

    @Test
    void addCertificateShouldAddCertificate() {
        CertificateResponseDto certificateResponseDto = service.addCertificate(TEST_CERTIFICATE);
        Assertions.assertEquals(certificateResponseDto, ADDED_CERTIFICATE);
    }

    @Test
    void deleteByIdShouldThrowWhenCertificateNotExist() {
        Assertions.assertThrows(CertificateNotFoundException.class, () -> service.deleteById(NOT_EXISTED_ID));
    }

    @Test
    void deleteByIdShouldDeleteWhenCertificatePresent() {
        CertificateResponseDto certificateResponseDto = service.deleteById(FIRST_ID);
        Assertions.assertEquals(FIRST_RESPONSE, certificateResponseDto);
    }

    //How to deal with such kind of tests
    @Test
    void updateCertificateShouldUpdateCertificateIfExisted() {
        Assertions.assertThrows(RuntimeException.class, () -> service.updateCertificate(FIRST_ID, TEST_CERTIFICATE));
    }

    @Test
    void getByIdShouldThrowWhenNotFound() {
        Assertions.assertThrows(CertificateNotFoundException.class, () -> service.getById(NOT_EXISTED_ID));
    }

    @Test
    void getByIdShouldReturnCertificateWhenExists() {
        CertificateResponseDto certificate = service.getById(FIRST_ID);
        Assertions.assertEquals(FIRST_RESPONSE, certificate);
    }

    @Test
    void getCertificatesShouldReturnAllFittingCertificates() {
        String testTagName = "";
        String testKeyword = "";
        String testSortString = "a,b";
        List<CertificateResponseDto> certificates = service.getCertificates(testTagName, testKeyword, testSortString);
        Assertions.assertEquals(CERTIFICATE_RESPONSES, certificates);
    }
}