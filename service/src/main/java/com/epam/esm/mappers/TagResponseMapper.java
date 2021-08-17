package com.epam.esm.mappers;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagResponseMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
    })
    TagResponseDto entityToRequest(GiftTag giftTag);


    List<TagResponseDto> entitiesToRequests(List<GiftTag> tags);
}
