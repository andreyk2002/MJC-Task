package com.epam.esm.service;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {


    @Mock
    private CertificateRepository certificateRepo;

    @Mock
    private GiftTagService tagService;

    @Mock
    private CertificateMapper mapper;

    @Mock
    private CertificateTagRepository certificateTagRepository;

    @Mock
    private NullableFieldsFinder nullableFieldsFinder;

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


        CertificateResponseDto updated = service.updateCertificate(id, requestDto);
        Assertions.assertEquals(responseDto, updated);

        verify(mapper).entityToResponse(addedCertificate);
        verify(certificateRepo, times(2)).getById(id);
    }

    @Test
    void getByIdShouldThrowWhenNotFound() {
        long id = 1;
        when(certificateRepo.getById(anyLong())).thenReturn(Optional.empty());


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


        CertificateResponseDto result = service.getById(id);
        Assertions.assertEquals(responseDto, result);
        verify(mapper).entityToResponse(giftCertificate);
        verify(certificateRepo).getById(id);
    }

    @Test
    void getCertificatesShouldReturnAllFittingCertificates() {
        long firstId = 1;
        long secondId = 2;
        GiftCertificate firstCertificate = GiftCertificate.builder()
                .id(firstId).name("first").build();
        GiftCertificate secondCertificate = GiftCertificate.builder()
                .id(secondId).name("fad").build();
        CertificateResponseDto firstResponse = CertificateResponseDto.builder()
                .id(firstId).name("first").build();
        CertificateResponseDto secondResponse = CertificateResponseDto.builder()
                .id(secondId).name("fad").build();
        List<CertificateResponseDto> responses = Arrays.asList(firstResponse, secondResponse);
        List<GiftCertificate> certificates = Arrays.asList(firstCertificate, secondCertificate);

        when(mapper.entitiesToResponses(any())).thenReturn(responses);
        when(certificateRepo.getAllSorted(anyString(), anyString(), anyString(), anyString())).thenReturn(certificates);


        String testTagName = "";
        String testKeyword = "";
        String testSortString = "a,b";
        List<CertificateResponseDto> results = service.getCertificates(testTagName, testKeyword, testSortString);
        Assertions.assertEquals(responses, results);

        verify(mapper).entitiesToResponses(certificates);
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
        List<TagResponseDto> responses = Arrays.asList(new TagResponseDto(1, "fda"),
                new TagResponseDto(2, "fff"));
        return CertificateResponseDto.builder()
                .id(id).name("newName").description("newDescription").price(new BigDecimal(1)).duration(10)
                .tags(responses).build();
    }

    private CertificateRequestDto buildCertificateRequest() {
        List<TagRequestDto> requests = Arrays.asList(new TagRequestDto(1, "fda"), new TagRequestDto(2, "fff"));
        return new CertificateRequestDto("newName", "newDescription", new BigDecimal(1),
                10, requests);
    }
}