package com.epam.esm.mapping;

import com.epam.esm.dto.TagDto;
import com.epam.esm.validation.TagModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TagModelDtoMapper {

    @Mappings({
            @Mapping(target="name", source= "name"),
    })
    TagDto tagModelToDto(TagModel tagModel);

}
