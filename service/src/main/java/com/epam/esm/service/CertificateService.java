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
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final GiftTagService tagService;
    private final CertificateMapper mapper;
    private final CertificateTagRepository certificateTagRepository;
    private final NullableFieldsFinder nullableFieldsFinder;


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

    @Transactional
    public CertificateResponseDto deleteById(long id) {
        CertificateResponseDto certificate = getById(id);
        certificateTagRepository.deleteByCertificateId(id);
        certificateRepo.deleteById(id);
        return certificate;
    }


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


    public CertificateResponseDto getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateRepo.getById(id);
        return optionalGiftCertificate.map(mapper::entityToResponse)
                .orElseThrow(() -> new CertificateNotFoundException(id));
    }

    public List<CertificateResponseDto> getCertificates(String tagName, String keyword, String sortString) {
        String[] sort = sortString.split(",");
        String sortOrder = sort[1];
        String field = sort[0];
        List<GiftCertificate> certificates;
        certificates = certificateRepo.getAllSorted(keyword, tagName, sortOrder, field);
        return mapper.entitiesToResponses(certificates);
    }
}

