package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapping.CertificateEntityDtoMapper;
import com.epam.esm.repository.CertificateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


//May be not working (autowired)
@AllArgsConstructor
@Service
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final CertificateEntityDtoMapper mapper;

    public void addCertificate(GiftCertificateDto certificateDto) {
        GiftCertificate giftCertificate = mapper.certificateToCertificateDto(certificateDto);
        certificateRepo.addCertificate(giftCertificate);
    }

    public void deleteById(long id) {
        certificateRepo.deleteById(id);
    }


    //TODO: update only not nullable fields
    public void updateCertificate(GiftCertificateDto giftCertificate) {

    }

    public List<GiftCertificateDto> getAll() {
        List<GiftCertificate> certificates = certificateRepo.getAll();
        return mapper.certificatesToCertificatesDto(certificates);
    }

    public GiftCertificateDto getById(long id) {
        GiftCertificate certificate = certificateRepo.getById(id);
        return mapper.certificateToCertificateDto(certificate);
    }
}
