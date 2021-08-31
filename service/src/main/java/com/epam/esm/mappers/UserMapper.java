package com.epam.esm.mappers;

import com.epam.esm.dto.UserResponseDto;
import com.epam.esm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
    })
    UserResponseDto entityToResponse(User user);

    List<UserResponseDto> entitiesToResponses(List<User> all);
}
