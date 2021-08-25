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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;


class CertificateServiceTest {

    private static final int NOT_EXISTED_ID = 666;
    private static final int FIRST_ID = 1;
    private static final GiftCertificate FIRST_CERTIFICATE = GiftCertificate.builder()
            .id(FIRST_ID).name("first").build();
    private static final CertificateResponseDto FIRST_RESPONSE = CertificateResponseDto.builder()
            .id(FIRST_ID).name("first").build();
    private static final int SECOND_ID = 2;
    private static final int TAG_ID = 1;
    private static final Long ADDED_ID = 3L;
    private static final CertificateResponseDto SECOND_RESPONSE = CertificateResponseDto.builder().id(SECOND_ID).name("first").build();
    private static final GiftCertificate SECOND_CERTIFICATE = GiftCertificate.builder().id(SECOND_ID).name("first").build();
    private static final List<GiftCertificate> CERTIFICATES = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE);
    private static final List<CertificateResponseDto> CERTIFICATE_RESPONSES = Arrays.asList(FIRST_RESPONSE, SECOND_RESPONSE);
    private static final List<TagRequestDto> TAG_REQUESTS = Collections.singletonList(new TagRequestDto(TAG_ID, "tag"));
    private static final TagResponseDto TAG_RESPONSE = new TagResponseDto(TAG_ID, "tag");
    private static final List<TagResponseDto> TAG_RESPONSES = Collections.singletonList(TAG_RESPONSE);


    @Mock
    private final CertificateRepository certificateRepo = Mockito.mock(CertificateRepository.class);

    @Mock
    private final GiftTagService tagService = Mockito.mock(GiftTagService.class);

    @Mock
    private final CertificateMapper mapper = Mockito.mock(CertificateMapper.class);

    @Mock
    private final CertificateTagRepository certificateTagRepository = Mockito.mock(CertificateTagRepository.class);

    @Mock
    private final NullableFieldsFinder nullableFieldsFinder = Mockito.mock(NullableFieldsFinder.class);

    @InjectMocks
    private CertificateService service;

    @Test
    void addCertificateShouldAddCertificate() {
        long id = 1;
        CertificateResponseDto responseDto = buildCertificateResponse(id);
        GiftCertificate addedCertificate = buildCertificate(id);
        CertificateRequestDto requestDto = buildCertificateRequest();
        when(mapper.requestToEntity(any())).thenReturn(addedCertificate);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(certificateRepo.addCertificate(any())).thenReturn(id);
        when(certificateRepo.getById(anyLong())).thenReturn(Optional.of(addedCertificate));
        when(tagService.updateTag(any())).thenReturn(new TagResponseDto(1, "tag"));
        service = new CertificateService(certificateRepo, tagService, mapper, certificateTagRepository, nullableFieldsFinder);

        CertificateResponseDto result = service.addCertificate(requestDto);
        Assertions.assertEquals(responseDto, result);

        verify(mapper).requestToEntity(requestDto);
        verify(mapper).entityToResponse(addedCertificate);
        verify(certificateRepo).addCertificate(addedCertificate);
        verify(certificateRepo).getById(id);
    }

    @Test
    void deleteByIdShouldThrowWhenCertificateNotExist() {
        long id = 10;
        when(certificateRepo.getById(anyLong())).thenReturn(Optional.empty());
        service = new CertificateService(certificateRepo, tagService, mapper, certificateTagRepository, nullableFieldsFinder);
        Assertions.assertThrows(CertificateNotFoundException.class, () -> service.deleteById(id));
        verify(certificateRepo).getById(id);
    }

    @Test
    void deleteByIdShouldDeleteWhenCertificatePresent() {
        long id = 1;
        CertificateResponseDto responseDto = buildCertificateResponse(id);
        GiftCertificate addedCertificate = buildCertificate(id);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(certificateRepo.getById(anyLong())).thenReturn(Optional.of(addedCertificate));
        service = new CertificateService(certificateRepo, tagService, mapper, certificateTagRepository, nullableFieldsFinder);

        CertificateResponseDto certificateResponseDto = service.deleteById(id);
        Assertions.assertEquals(responseDto, certificateResponseDto);

        verify(mapper).entityToResponse(eq(addedCertificate));
        verify(certificateRepo).getById(id);
    }


    @Test
    void updateCertificateShouldUpdateCertificateIfExisted() {
        long id = 421;
        CertificateResponseDto responseDto = buildCertificateResponse(id);
        GiftCertificate addedCertificate = buildCertificate(id);
        CertificateRequestDto requestDto = buildCertificateRequest();
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(certificateRepo.getById(anyLong())).thenReturn(Optional.of(addedCertificate));
        when(tagService.addTag(any())).thenReturn(new TagResponseDto(1, "tag"));
        service = new CertificateService(certificateRepo, tagService, mapper, certificateTagRepository, nullableFieldsFinder);

        CertificateResponseDto updated = service.updateCertificate(id, requestDto);
        Assertions.assertEquals(responseDto, updated);

        verify(mapper).entityToResponse(addedCertificate);
        verify(certificateRepo, times(2)).getById(id);
    }

    @Test
    void getByIdShouldThrowWhenNotFound() {
        long id = 1;
        when(certificateRepo.getById(anyLong())).thenReturn(Optional.empty());
        service = new CertificateService(certificateRepo, tagService, mapper, certificateTagRepository, nullableFieldsFinder);

        Assertions.assertThrows(CertificateNotFoundException.class, () -> service.getById(id));
        verify(certificateRepo).getById(id);
    }

    @Test
    void getByIdShouldReturnCertificateWhenExists() {
        long id = 1;
        CertificateResponseDto responseDto = buildCertificateResponse(id);
        GiftCertificate giftCertificate = buildCertificate(id);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(certificateRepo.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
        service = new CertificateService(certificateRepo, tagService, mapper, certificateTagRepository, nullableFieldsFinder);

        CertificateResponseDto result = service.getById(id);
        Assertions.assertEquals(responseDto, result);
        verify(mapper).entityToResponse(giftCertificate);
        verify(certificateRepo).getById(id);
    }

    @Test
    void getCertificatesShouldReturnAllFittingCertificates() {
        when(mapper.entitiesToResponses(any())).thenReturn(CERTIFICATE_RESPONSES);
        when(certificateRepo.getAllSorted(anyString(), anyString(), anyString(), anyString())).thenReturn(CERTIFICATES);
        service = new CertificateService(certificateRepo, tagService, mapper, certificateTagRepository, nullableFieldsFinder);

        String testTagName = "";
        String testKeyword = "";
        String testSortString = "a,b";
        List<CertificateResponseDto> certificates = service.getCertificates(testTagName, testKeyword, testSortString);
        Assertions.assertEquals(CERTIFICATE_RESPONSES, certificates);

        verify(mapper).entitiesToResponses(CERTIFICATES);
        String field = "b";
        String order = "a";
        verify(certificateRepo).getAllSorted(testKeyword, testTagName, field, order);
    }

    private GiftCertificate buildCertificate(long id) {
        return GiftCertificate.builder()
                .id(id).name("newName").description("newDescription").price(new BigDecimal(1)).duration(10)
                .build();
    }

    private CertificateResponseDto buildCertificateResponse(long id) {
        return CertificateResponseDto.builder()
                .id(id).name("newName").description("newDescription").price(new BigDecimal(1)).duration(10)
                .tags(TAG_RESPONSES).build();
    }

    private CertificateRequestDto buildCertificateRequest() {
        return new CertificateRequestDto("newName", "newDescription", new BigDecimal(1), 10, TAG_REQUESTS);
    }
}