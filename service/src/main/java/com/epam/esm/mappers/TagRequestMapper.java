package com.epam.esm.mappers;

import com.epam.esm.entity.GiftTag;
import com.epam.esm.validation.TagRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TagRequestMapper {

    @Mappings({
            @Mapping(target = "name", source = "name"),
    })
    GiftTag requestToEntity(TagRequestDto tagRequestDto);

}
