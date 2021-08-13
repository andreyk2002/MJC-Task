package com.epam.esm.mapping;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CertificateEntityDtoMapper {

    @Mappings({
            @Mapping(target = "name" , source = "name"),
            @Mapping(target = "price" , source = "price"),
            @Mapping(target = "duration" , source = "duration"),
            @Mapping(target = "createDate" , source = "createDate"),
            @Mapping(target = "lastUpdateDate" , source = "lastUpdateDate"),
    })
    GiftCertificate certificateToCertificateDto(GiftCertificateDto certificateDto);

    @Mappings({
            @Mapping(target = "name" , source = "name"),
            @Mapping(target = "price" , source = "price"),
            @Mapping(target = "duration" , source = "duration"),
            @Mapping(target = "createDate" , source = "createDate"),
            @Mapping(target = "lastUpdateDate" , source = "lastUpdateDate"),
    })
    GiftCertificateDto certificateToCertificateDto(GiftCertificate giftCertificate);

    List<GiftCertificateDto> certificatesToCertificatesDto(List<GiftCertificate> certificates);
}
