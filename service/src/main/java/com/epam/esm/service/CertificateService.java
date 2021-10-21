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
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Provides set of operations with {@link GiftCertificate} entities
 */

@Service
@AllArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final GiftTagService tagService;
    private final CertificateMapper mapper;
    private final CertificateTagRepository certificateTagRepository;
    private final NullableFieldsFinder nullableFieldsFinder;


    /**
     * Adds a requested instance of certificate and its tags to repository
     *
     * @param certificateDto - instance needed to be added
     * @return instance of {@link CertificateResponseDto} which is already added to repository
     */
    @Transactional
    public CertificateResponseDto addCertificate(CertificateRequestDto certificateDto) {
        GiftCertificate giftCertificate = mapper.requestToEntity(certificateDto);
        long insertId = certificateRepo.addCertificate(giftCertificate);
        List<TagRequestDto> tags = certificateDto.getTags();
        for (TagRequestDto tagRequestDto : tags) {
            TagResponseDto tagResponseDto = tagService.updateTag(tagRequestDto);
            long tagId = tagResponseDto.getId();
            certificateTagRepository.addCertificateTag(tagId, insertId);
        }
        return getById(insertId);
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
        certificateTagRepository.deleteByCertificateId(id);
        certificateRepo.deleteById(id);
        return certificate;
    }

    /**
     * Updates instance of specified certificate (Only non-nullable fields will be updated)
     *
     * @param certificateId   ID of certificate, which should be updated
     * @param giftCertificate Contains new state of field, which will be updated
     * @return instance of {@link CertificateResponseDto} which is already update in repository
     */
    @Transactional
    public CertificateResponseDto updateCertificate(long certificateId, CertificateRequestDto giftCertificate) {
        Optional<GiftCertificate> optionalUpdated = certificateRepo.getById(certificateId);
        return optionalUpdated.map(updated -> {
            String[] nullPropertyNames = nullableFieldsFinder.getNullPropertyNames(giftCertificate);
            BeanUtils.copyProperties(giftCertificate, updated, nullPropertyNames);
            List<TagRequestDto> tags = giftCertificate.getTags();
            if (tags != null) {
                certificateTagRepository.deleteByCertificateId(certificateId);
                for (TagRequestDto tagResponseDto : tags) {
                    TagResponseDto inserted = tagService.addTag(tagResponseDto);
                    long tagId = inserted.getId();
                    certificateTagRepository.addCertificateTag(tagId, certificateId);
                }
            }
            certificateRepo.updateCertificate(updated);
            return getById(certificateId);
        }).orElseGet(() -> addCertificate(giftCertificate));
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
     * @param tagName    - name of the tag which required certificates should contain. If null value passed, all certificates
     *                   applied to that criteria
     * @param keyword    - part of certificate name/description which required certificates should contain. If null value passed, all certificates
     *                   *                applied to that criteria
     * @param sortString - specifies the way, how list of certificates should be sorted. Should apply to regex (.*), (.*),
     *                   where first part contain information about field, by which sorting will performed, and second part
     *                   specifies the sort order (acs/desc)
     * @return List of all certificates which applied to all limits, sorted in specified order
     */
    public List<CertificateResponseDto> getCertificates(String tagName, String keyword, String sortString) {
        String[] sort = sortString.split(",");
        String sortOrder = sort[1];
        String field = sort[0];
        List<GiftCertificate> certificates;
        certificates = certificateRepo.getAllSorted(keyword, tagName, sortOrder, field);
        return mapper.entitiesToResponses(certificates);
    }
}

