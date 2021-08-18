package com.epam.esm.service;

import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mappers.CertificateRequestMapper;
import com.epam.esm.mappers.CertificateResponseMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.validation.CertificateRequestDto;
import com.epam.esm.validation.TagRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final GiftTagService tagService;
    private final CertificateResponseMapper certificateMapper;
    private final CertificateRequestMapper requestMapper;
    private final CertificateTagRepository certificateTagRepository;


    @Transactional
    public CertificateResponseDto addCertificate(CertificateRequestDto certificateDto) {
        GiftCertificate giftCertificate = requestMapper.responseToEntity(certificateDto);
        long insertId = certificateRepo.addCertificate(giftCertificate);
        List<TagRequestDto> tags = certificateDto.getTags();
        for (TagRequestDto tagRequestDto : tags) {
            TagResponseDto tagResponseDto = tagService.addTag(tagRequestDto);
            long tagId = tagResponseDto.getId();
            certificateTagRepository.addCertificateTag(tagId, insertId);
        }
        Optional<CertificateResponseDto> certificate = getById(insertId);
        return certificate.get();
    }

    @Transactional
    public Optional<CertificateResponseDto> deleteById(long id) {
        Optional<CertificateResponseDto> certificateForDeleting = getById(id);
        certificateTagRepository.deleteByCertificateId(id);
        certificateRepo.deleteById(id);
        return certificateForDeleting;
    }


    @Transactional
    public CertificateResponseDto updateCertificate(long certificateId, CertificateRequestDto giftCertificate) {
        Optional<GiftCertificate> optionalUpdated = certificateRepo.getById(certificateId);
        if (optionalUpdated.isEmpty()) {
            return addCertificate(giftCertificate);
        }
        GiftCertificate updated = optionalUpdated.get();
        String newName = giftCertificate.getName();
        if (newName != null) {
            updated.setName(newName);
        }
        String newDescription = giftCertificate.getDescription();
        if (newDescription != null) {
            updated.setDescription(newDescription);
        }
        BigDecimal price = giftCertificate.getPrice();
        if (price != null) {
            updated.setPrice(price);
        }
        Integer duration = giftCertificate.getDuration();
        if (duration != null) {
            updated.setDuration(duration);
        }
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
        Optional<CertificateResponseDto> result = getById(certificateId);
        return result.get();
    }

    public List<CertificateResponseDto> getAll() {
        List<GiftCertificate> certificates = certificateRepo.getAll();
        return certificateMapper.entitiesToResponses(certificates);
    }

    public Optional<CertificateResponseDto> getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateRepo.getById(id);
        if (optionalGiftCertificate.isEmpty()) {
            return Optional.empty();
        }
        GiftCertificate giftCertificate = optionalGiftCertificate.get();
        CertificateResponseDto certificateResponseDto = certificateMapper.entityToResponse(giftCertificate);
        return Optional.of(certificateResponseDto);
    }

    public List<CertificateResponseDto> getCertificates(String tagName, String keyword, String sortString) {
        String[] sort = sortString.split(",");
        String sortOrder = sort[1];
        String field = sort[0];
        List<GiftCertificate> certificates;
        if (tagName == null && keyword == null) {
            certificates = certificateRepo.getAllSorted(sortOrder, field);
        } else if (keyword == null) {
            certificates = certificateRepo.getByTagName(tagName, sortOrder, field);
        } else if (tagName == null) {
            certificates = certificateRepo.getByKeyword(keyword, sortOrder, field);
        } else {
            certificates = certificateRepo.getByTagNameAndKeyword(keyword, tagName, sortOrder, field);
        }
        return certificateMapper.entitiesToResponses(certificates);
    }
}
