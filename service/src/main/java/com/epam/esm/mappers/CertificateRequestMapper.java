package com.epam.esm.mappers;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.validation.CertificateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface CertificateRequestMapper {

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "duration", source = "duration"),
    })
    GiftCertificate responseToEntity(CertificateRequestDto model);
}
