package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.CertificateEntityDtoMapper;
import com.epam.esm.mappers.TagEntityDtoMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


//May be not working (autowired)
@AllArgsConstructor
@Service
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final CertificateEntityDtoMapper certificateMapper;
    private final CertificateTagRepository certificateTagRepository;
    private final TagEntityDtoMapper tagMapper;

    public void addCertificate(GiftCertificateDto certificateDto) {
        GiftCertificate giftCertificate = certificateMapper.certificateToCertificateDto(certificateDto);
        certificateRepo.addCertificate(giftCertificate);
    }

    public void deleteById(long id) {
        //Transactions
        certificateTagRepository.deleteByCertificateId(id);
        certificateRepo.deleteById(id);
    }


    //TODO: update only not nullable fields
    public void updateCertificate(GiftCertificateDto giftCertificate) {

    }

    public List<GiftCertificateDto> getAll() {
        List<GiftCertificate> certificates = certificateRepo.getAll();
        return certificateMapper.certificatesToCertificatesDto(certificates);
    }

    public GiftCertificateDto getById(long id) {
        GiftCertificate certificate = certificateRepo.getById(id);
        return certificateMapper.certificateToCertificateDto(certificate);
    }
}
