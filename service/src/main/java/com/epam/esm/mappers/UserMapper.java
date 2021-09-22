package com.epam.esm.mappers;

import com.epam.esm.entity.User;
import com.epam.esm.request.UserRequestDto;
import com.epam.esm.response.UserResponseDto;
import com.epam.esm.security.SecurityUser;
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


    @Mappings({
            @Mapping(target = "login", source = "login"),
            @Mapping(target = "passwordHash", source = "passwordHash"),
            @Mapping(target = "authorities", expression = "java(user.getRole().authorities())")
    })
    SecurityUser entityToSecurityUser(User user);


    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "login", source = "login"),
            @Mapping(target = "passwordHash", source = "passwordHash")
    })
    User requestToEntity(UserRequestDto userRequestDto);


}
