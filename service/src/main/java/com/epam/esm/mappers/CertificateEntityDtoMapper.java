package com.epam.esm.mappers;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.service.CertificateTagService;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;



@Mapper(componentModel = "spring")
public abstract class CertificateEntityDtoMapper {

    @Autowired
    protected CertificateTagService service;

    @Mappings({
            @Mapping(target = "name" , source = "name"),
            @Mapping(target = "price" , source = "price"),
            @Mapping(target = "duration" , source = "duration"),
            @Mapping(target = "createDate" , source = "createDate"),
            @Mapping(target = "lastUpdateDate" , source = "lastUpdateDate"),
    })
    public abstract GiftCertificate certificateToCertificateDto(GiftCertificateDto certificateDto);

    @Mappings({
            @Mapping(target = "name" , source = "name"),
            @Mapping(target = "price" , source = "price"),
            @Mapping(target = "duration" , source = "duration"),
            @Mapping(target = "createDate" , source = "createDate"),
            @Mapping(target = "lastUpdateDate" , source = "lastUpdateDate"),
            @Mapping(target = "tags", expression = "java(service.getTagsByCertificateId(giftCertificate.getId()))")
    })
    public abstract GiftCertificateDto certificateToCertificateDto(GiftCertificate giftCertificate);

    public abstract List<GiftCertificateDto> certificatesToCertificatesDto(List<GiftCertificate> certificates);
}
