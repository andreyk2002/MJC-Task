package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.CertificateEntityDtoMapper;
import com.epam.esm.mappers.TagEntityDtoMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final TagEntityDtoMapper tagMapper;
    private final TagRepository tagRepo;
    private final CertificateEntityDtoMapper certificateMapper;
    private final CertificateTagRepository certificateTagRepository;

    @Transactional
    public void addCertificate(GiftCertificateDto certificateDto) {
        List<TagDto> tags = certificateDto.getTags();
        for (TagDto tagDto : tags) {
            GiftTag giftTag = tagMapper.tagDtoToTag(tagDto);
            tagRepo.saveTag(giftTag);
        }
        GiftCertificate giftCertificate = certificateMapper.certificateToCertificateDto(certificateDto);
        certificateRepo.addCertificate(giftCertificate);
    }

    @Transactional
    public void deleteById(long id) {
        certificateTagRepository.deleteByCertificateId(id);
        certificateRepo.deleteById(id);
    }


    //TODO: update only not nullable fields
    @Transactional
    public void updateCertificate(long id, GiftCertificateDto giftCertificate) {
        Optional<GiftCertificate> optionalUpdated = certificateRepo.getById(id);
        if (optionalUpdated.isEmpty()) {
            addCertificate(giftCertificate);
            return;
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
        //...
    }

    public List<GiftCertificateDto> getAll() {
        List<GiftCertificate> certificates = certificateRepo.getAll();
        return certificateMapper.certificatesToCertificatesDto(certificates);
    }

    public Optional<GiftCertificateDto> getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateRepo.getById(id);
        if (optionalGiftCertificate.isEmpty()) {
            return Optional.empty();
        }
        GiftCertificate giftCertificate = optionalGiftCertificate.get();
        GiftCertificateDto giftCertificateDto = certificateMapper.certificateToCertificateDto(giftCertificate);
        return Optional.of(giftCertificateDto);
    }

    public List<GiftCertificateDto> getCertificates(String tagName, String keyword, String sortString) {
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
        return certificateMapper.certificatesToCertificatesDto(certificates);
    }
}
