package com.epam.esm.mapping;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagEntityDtoMapper {

    @Mappings({
            @Mapping(target="name", source= "name"),
    })
    TagDto tagToTagDto(GiftTag giftTag);

    @Mappings({
            @Mapping(target="name", source="name"),
    })
    GiftTag tagDtoToTag(TagDto tagDto);

    List<TagDto> tagsToTagsDto(List<GiftTag>tags);
}
