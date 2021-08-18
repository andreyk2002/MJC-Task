package com.epam.esm.mappers;


import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.CertificateTagService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class CertificateResponseMapper {

    @Autowired
    protected CertificateTagService service;


    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "duration", source = "duration"),
            @Mapping(target = "createDate", source = "createDate"),
            @Mapping(target = "lastUpdateDate", source = "lastUpdateDate"),
            @Mapping(target = "tags", expression = "java(service.getTagsByCertificateId(giftCertificate.getId()))")
    })
    public abstract CertificateResponseDto entityToResponse(GiftCertificate giftCertificate);

    public abstract List<CertificateResponseDto> entitiesToResponses(List<GiftCertificate> certificates);
}
