package com.epam.esm.mappers;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.validation.CertificateModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface CertificateModelDtoMapper {

    @Mappings({
            @Mapping(target = "name" , source = "name"),
            @Mapping(target = "price" , source = "price"),
            @Mapping(target = "duration" , source = "duration"),
            @Mapping(target = "createDate" , source = "createDate"),
            @Mapping(target = "lastUpdateDate" , source = "lastUpdateDate"),
    })
    GiftCertificateDto certificateModelToDto(CertificateModel model);
}
