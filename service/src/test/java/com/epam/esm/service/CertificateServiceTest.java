package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.CertificateMapper;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.specification.CertificateSpecification;
import com.epam.esm.request.CertificateRequestDto;
import com.epam.esm.request.TagRequestDtoCertificate;
import com.epam.esm.response.CertificateResponseDto;
import com.epam.esm.response.TagResponseDto;
import com.epam.esm.service.excepiton.CertificateNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;

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
    private NullableFieldsFinder nullableFieldsFinder;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private CertificateService service;

    @Test
    void testAddCertificateShouldAddCertificate() {
        TagResponseDto firstTagResponse = new TagResponseDto(1, "fda");
        TagResponseDto secondTagResponse = new TagResponseDto(2, "fff");

        GiftTag firstTag = new GiftTag(1, "fda");
        GiftTag secondTag = new GiftTag(2, "fff");
        List<TagResponseDto> tagResponses = Arrays.asList(firstTagResponse,
                secondTagResponse);
        Set<GiftTag> tagEntities = Set.of(firstTag, secondTag);
        long id = 1;
        CertificateResponseDto responseDto = buildCertificateResponse(id, tagResponses);
        GiftCertificate addedCertificate = buildCertificate(id, tagEntities);
        CertificateRequestDto requestDto = buildCertificateRequest();
        when(mapper.requestToEntity(any())).thenReturn(addedCertificate);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(certificateRepo.save(any())).thenReturn(addedCertificate);
        when(tagService.updateTag(any())).thenReturn(firstTagResponse)
                .thenReturn(secondTagResponse);
        when(tagMapper.responsesToEntities(any())).thenReturn(new ArrayList<>(tagEntities));

        CertificateResponseDto result = service.addCertificate(requestDto);
        Assertions.assertEquals(responseDto, result);


        verify(mapper).requestToEntity(requestDto);
        verify(mapper).entityToResponse(addedCertificate);
        verify(certificateRepo).save(addedCertificate);
        verify(tagMapper).responsesToEntities(tagResponses);
    }

    @Test
    void testDeleteByIdShouldThrowWhenCertificateNotExist() {
        long id = 10;
        when(certificateRepo.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(CertificateNotFoundException.class, () -> service.deleteById(id));
        verify(certificateRepo).findById(id);
    }

    @Test
    void testDeleteByIdShouldDeleteWhenCertificatePresent() {
        List<TagResponseDto> responses = Arrays.asList(new TagResponseDto(1, "fda"),
                new TagResponseDto(2, "ffjf"));
        Set<GiftTag> tagEntities = Set.of(new GiftTag(1, "fda"), new GiftTag(2, "ffjf"));
        long id = 1;
        CertificateResponseDto responseDto = buildCertificateResponse(id, responses);
        GiftCertificate addedCertificate = buildCertificate(id, tagEntities);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(certificateRepo.findById(anyLong())).thenReturn(Optional.of(addedCertificate));


        CertificateResponseDto certificateResponseDto = service.deleteById(id);
        Assertions.assertEquals(responseDto, certificateResponseDto);

        verify(mapper).entityToResponse(eq(addedCertificate));
        verify(certificateRepo).findById(id);
    }


    @Test
    void testUpdateCertificateShouldUpdateCertificateIfExisted() {
        long id = 421;
        TagResponseDto firstTagResponse = new TagResponseDto(1, "jjj");
        TagResponseDto secondTagResponse = new TagResponseDto(2, "fff");

        GiftTag firstTag = new GiftTag(1, "fda");
        GiftTag secondTag = new GiftTag(2, "fff");
        List<TagResponseDto> tagResponses = Arrays.asList(firstTagResponse,
                secondTagResponse);
        Set<GiftTag> tagEntities = Set.of(firstTag, secondTag);
        CertificateResponseDto responseDto = buildCertificateResponse(id, tagResponses);
        GiftCertificate addedCertificate = buildCertificate(id, tagEntities);
        CertificateRequestDto requestDto = buildCertificateRequest();
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.requestToEntity(any())).thenReturn(addedCertificate);
        when(certificateRepo.findById(anyLong())).thenReturn(Optional.of(addedCertificate));
        when(certificateRepo.save(any())).thenReturn(addedCertificate);
        when(tagService.updateTag(any())).thenReturn(firstTagResponse).thenReturn(secondTagResponse);
        when(tagMapper.responsesToEntities(any())).thenReturn(new ArrayList<>(tagEntities));

        CertificateResponseDto updated = service.updateCertificate(id, requestDto);
        Assertions.assertEquals(responseDto, updated);

        verify(certificateRepo).save(addedCertificate);
        verify(mapper).requestToEntity(requestDto);
        verify(mapper).entityToResponse(addedCertificate);
        verify(certificateRepo).findById(id);
        verify(tagMapper).responsesToEntities(tagResponses);
    }

    @Test
    void testUpdateCertificateShouldUpdateCertificateIfAndTagsAreNull() {
        long id = 421;

        CertificateResponseDto responseDto = buildCertificateResponse(id, null);
        GiftCertificate addedCertificate = buildCertificate(id, null);
        CertificateRequestDto requestDto = buildCertificateRequest();
        requestDto.setTags(null);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(mapper.requestToEntity(any())).thenReturn(addedCertificate);
        when(certificateRepo.findById(anyLong())).thenReturn(Optional.of(addedCertificate));
        when(certificateRepo.save(any())).thenReturn(addedCertificate);

        CertificateResponseDto updated = service.updateCertificate(id, requestDto);
        Assertions.assertEquals(responseDto, updated);

        verify(certificateRepo).save(addedCertificate);
        verify(mapper).requestToEntity(requestDto);
        verify(mapper).entityToResponse(addedCertificate);
        verify(certificateRepo).findById(id);
    }

    @Test
    void testGetByIdShouldThrowWhenNotFound() {
        long id = 1;
        when(certificateRepo.findById(anyLong())).thenReturn(Optional.empty());


        Assertions.assertThrows(CertificateNotFoundException.class, () -> service.getById(id));
        verify(certificateRepo).findById(id);
    }

    @Test
    void testGetByIdShouldReturnCertificateWhenExists() {
        List<TagResponseDto> responses = Arrays.asList(new TagResponseDto(1, "fac"),
                new TagResponseDto(2, "fff"));
        Set<GiftTag> tagEntities = Set.of(new GiftTag(1, "fac"), new GiftTag(2, "fff"));
        long id = 1;
        CertificateResponseDto responseDto = buildCertificateResponse(id, responses);
        GiftCertificate giftCertificate = buildCertificate(id, tagEntities);
        when(mapper.entityToResponse(any())).thenReturn(responseDto);
        when(certificateRepo.findById(anyLong())).thenReturn(Optional.of(giftCertificate));


        CertificateResponseDto result = service.getById(id);
        Assertions.assertEquals(responseDto, result);
        verify(mapper).entityToResponse(giftCertificate);
        verify(certificateRepo).findById(id);
    }

    @Test
    void testGetCertificatesShouldReturnAllFittingCertificates() {
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
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        String keyword = "";
        String tagName = "";
        Specification<GiftCertificate> specification = new CertificateSpecification(keyword, tagName);
        when(mapper.entitiesToResponses(any())).thenReturn(responses);
        when(certificateRepo.findAll(specification, pageable)).thenReturn(new PageImpl<>(certificates));

        List<CertificateResponseDto> results = service.getCertificates(pageable, keyword, tagName);
        Assertions.assertEquals(responses, results);

        verify(mapper).entitiesToResponses(certificates);
    }

    @Test
    void findByTagShouldFind() {
        String tagsString = "1,2,5";
        List<Long> tagIds = Arrays.asList(1L, 2L, 5L);
        GiftTag firstTag = new GiftTag(1, "tag");
        GiftTag secondTag = new GiftTag(2, "www");
        GiftTag thirdTag = new GiftTag(5, "ghg");
        Set<GiftTag> tags = Set.of(firstTag, secondTag, thirdTag);
        int certificateId = 1;
        GiftCertificate certificate = buildCertificate(certificateId, tags);

        TagResponseDto firstTagResponse = new TagResponseDto(1, "tag");
        TagResponseDto secondTagResponse = new TagResponseDto(2, "www");
        TagResponseDto thirdTagResponse = new TagResponseDto(5, "ghg");
        List<TagResponseDto> tagResponses =
                Arrays.asList(firstTagResponse, secondTagResponse, thirdTagResponse);
        CertificateResponseDto responseDto = buildCertificateResponse(certificateId, tagResponses);
        when(certificateRepo.findByTags(anyList())).thenReturn(Collections.singletonList(certificate));
        when(mapper.entitiesToResponses(anyList())).thenReturn(Collections.singletonList(responseDto));

        List<CertificateResponseDto> result = service.findByTags(tagsString);
        Assertions.assertEquals(result, Collections.singletonList(responseDto));

        verify(mapper).entitiesToResponses(Collections.singletonList(certificate));
        verify(certificateRepo).findByTags(tagIds);
    }

    private GiftCertificate buildCertificate(long id, Set<GiftTag> tags) {
        return GiftCertificate.builder()
                .id(id)
                .name("newName")
                .tags(tags)
                .description("newDescription")
                .price(new BigDecimal(1))
                .duration(10)
                .build();
    }

    private CertificateResponseDto buildCertificateResponse(long id, List<TagResponseDto> tagResponses) {

        return CertificateResponseDto.builder()
                .id(id)
                .name("newName")
                .description("newDescription")
                .price(new BigDecimal(1))
                .duration(10)
                .tags(tagResponses)
                .build();
    }

    private CertificateRequestDto buildCertificateRequest() {
        List<TagRequestDtoCertificate> requests = Arrays.asList(new TagRequestDtoCertificate(1, "fda"), new TagRequestDtoCertificate(2, "fff"));
        return new CertificateRequestDto(0, "newName", "newDescription", new BigDecimal(1),
                10, requests);
    }
}