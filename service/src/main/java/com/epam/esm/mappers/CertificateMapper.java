package com.epam.esm.mappers;


import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.CertificateTagService;
import com.epam.esm.validation.CertificateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * Provides functionality for transforming certificate entities from/to request and response dtos
 */
@Mapper(componentModel = "spring")
public abstract class CertificateMapper {

    @Autowired
    protected CertificateTagService service;

    /**
     * Transforms instance of {@link GiftCertificate} type to instance of {@link CertificateResponseDto}
     *
     * @param giftCertificate - instance of {@link GiftCertificate}, needed to be transformed
     * @return instance of {@link CertificateResponseDto}, which has the same state that has passed instance
     */
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

    /**
     * Transforms list of {@link GiftCertificate} instances to list of {@link CertificateResponseDto} instances
     *
     * @param certificates - list of {@link GiftCertificate} instances, needed to be transformed
     * @return list of {@link CertificateResponseDto} instances, which have the same state that have passed instances
     */
    public abstract List<CertificateResponseDto> entitiesToResponses(List<GiftCertificate> certificates);


    /**
     * Transforms instance of {@link CertificateRequestDto} type to instance of {@link GiftCertificate}
     *
     * @param certificate - instance of {@link CertificateRequestDto}, needed to be transformed
     * @return instance of {@link GiftCertificate}, which has the same state that has passed instance
     */
    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "duration", source = "duration"),
    })
    public abstract GiftCertificate requestToEntity(CertificateRequestDto certificate);
}
