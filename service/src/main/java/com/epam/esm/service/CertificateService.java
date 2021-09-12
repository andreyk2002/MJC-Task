package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.CertificateMapper;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.CertificateFilter;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.request.CertificateRequestDto;
import com.epam.esm.request.TagRequestDtoCertificate;
import com.epam.esm.response.CertificateResponseDto;
import com.epam.esm.response.TagResponseDto;
import com.epam.esm.service.excepiton.CertificateNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides set of operations with {@link GiftCertificate} entities
 */

@Service
@AllArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final GiftTagService tagService;
    private final CertificateMapper mapper;
    private final NullableFieldsFinder nullableFieldsFinder;
    private final TagMapper tagMapper;

    /**
     * Adds a requested instance of certificate and its tags to repository
     *
     * @param certificateDto - instance needed to be added
     * @return instance of {@link CertificateResponseDto} which is already added to repository
     */
    @Transactional
    public CertificateResponseDto addCertificate(CertificateRequestDto certificateDto) {
        List<TagRequestDtoCertificate> tags = certificateDto.getTags();
        List<TagResponseDto> updatedTags = tags.stream().map(tagService::updateTag).collect(Collectors.toList());
        List<GiftTag> giftTags = tagMapper.responsesToEntities(updatedTags);
        GiftCertificate giftCertificate = mapper.requestToEntity(certificateDto);
        giftCertificate.setTags(new HashSet<>(giftTags));
        GiftCertificate certificate = certificateRepo.addCertificate(giftCertificate);
        return mapper.entityToResponse(certificate);
    }

    /**
     * Removes certificate by its id
     *
     * @param id ID of certificate needed to be deleted
     * @return instance of {@link CertificateResponseDto} which is already removed from repository
     */
    @Transactional
    public CertificateResponseDto deleteById(long id) {
        CertificateResponseDto certificate = getById(id);
        certificateRepo.deleteById(id);
        return certificate;
    }


    /**
     * Updates instance of specified certificate (Only non-nullable fields will be updated)
     *
     * @param certificateId      ID of certificate, which should be updated
     * @param certificateRequest Contains new state of field, which will be updated
     * @return instance of {@link CertificateResponseDto} which is already update in repository
     */
    @Transactional
    public CertificateResponseDto updateCertificate(long certificateId, CertificateRequestDto certificateRequest) {
        Optional<GiftCertificate> optionalUpdated = certificateRepo.getById(certificateId);
        return optionalUpdated.map(updated -> {
            GiftCertificate newCertificate = mapper.requestToEntity(certificateRequest);
            String[] nullPropertyNames = nullableFieldsFinder.getNullPropertyNames(newCertificate);
            BeanUtils.copyProperties(newCertificate, updated, nullPropertyNames);
            updated.setId(certificateId);
            List<TagRequestDtoCertificate> tags = certificateRequest.getTags();
            if (tags != null) {
                List<TagResponseDto> updatedTags = tags.stream().map(tagService::updateTag).collect(Collectors.toList());
                List<GiftTag> giftTags = tagMapper.responsesToEntities(updatedTags);
                updated.setTags(new HashSet<>(giftTags));
            }
            GiftCertificate certificate = certificateRepo.updateCertificate(updated);
            return mapper.entityToResponse(certificate);
        }).orElseGet(() -> addCertificate(certificateRequest));
    }

    /**
     * Get certificate from repository by its id
     *
     * @param id - id of required certificate
     * @return instance or required tag
     * @throws CertificateNotFoundException if certificate not found in repository
     */
    public CertificateResponseDto getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateRepo.getById(id);
        return optionalGiftCertificate.map(mapper::entityToResponse)
                .orElseThrow(() -> new CertificateNotFoundException(id));
    }

    /**
     * Searches list of all certificates depends on keyword (part of name or description) or/and tag name
     * in specified order
     *
     * @param filter stores information about which criterias of selecting certificates.
     *               May include tagName -  name of the tag which certificate should contain,
     *               keyword - part of the name or description which certificate should have
     *               sortOrder -  type of sort order (ascending, descending),
     *               sortField - name of the field by which certificates should be ordered
     * @return List of certificates which applied to the mentioned criterias
     */
    public List<CertificateResponseDto> getCertificates(CertificateFilter filter) {
        List<GiftCertificate> certificates = certificateRepo.getAllSorted(filter);
        return mapper.entitiesToResponses(certificates);
    }

    /**
     * Searches certificates which tags ids are present in specified list
     *
     * @param tagsString - String which contain tags ids in format id1,id2,id3,id4...
     * @return all certificates, which contain all needed tags
     */
    public List<CertificateResponseDto> findByTags(String tagsString) {
        String[] tags = tagsString.split(",");
        List<Long> ids = Arrays.stream(tags).map(Long::parseLong).collect(Collectors.toList());
        List<GiftCertificate> certificates = certificateRepo.findByTags(ids);
        return mapper.entitiesToResponses(certificates);
    }
}

