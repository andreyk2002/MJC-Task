package com.epam.esm.mappers;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.validation.TagRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Provides functionality for transforming tag entities from/to request and response dtos
 */
@Mapper(componentModel = "spring")
public interface TagMapper {

    /**
     * Transforms instance of {@link TagRequestDto} type to instance of {@link GiftTag}
     *
     * @param tagRequestDto - instance of {@link TagRequestDto}, needed to be transformed
     * @return instance of {@link GiftTag}, which has the same state that has passed instance
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
    })
    GiftTag requestToEntity(TagRequestDto tagRequestDto);

    /**
     * Transforms instance of {@link GiftTag} type to instance of {@link TagResponseDto}
     *
     * @param giftTag - instance of {@link GiftTag}, needed to be transformed
     * @return instance of {@link TagResponseDto}, which has the same state that has passed instance
     */
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
    })
    TagResponseDto entityToResponse(GiftTag giftTag);


    /**
     * Transforms list of {@link GiftTag} instances to list of {@link TagResponseDto} instances
     *
     * @param tags - list of {@link GiftTag} instances, needed to be transformed
     * @return list of {@link TagResponseDto} instances, which have the same state that have passed instances
     */
    List<TagResponseDto> entitiesToRequests(List<GiftTag> tags);

}
