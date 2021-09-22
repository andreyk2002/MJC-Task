package com.epam.esm.mappers;

import com.epam.esm.UserRole;
import com.epam.esm.entity.User;
import com.epam.esm.response.UserResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void entityToResponse() {
        User user = User.builder().name("alex").id(1).role(UserRole.ADMIN).build();
        UserResponseDto userResponseDto = userMapper.entityToResponse(user);
        UserResponseDto expected = new UserResponseDto(1, "alex", UserRole.ADMIN);
        Assertions.assertEquals(expected, userResponseDto);
    }

    @Test
    void entitiesToResponses() {
        User firstUser = User.builder().name("alex").id(1).role(UserRole.ADMIN).build();
        User secondUser = User.builder().name("james").id(2).role(UserRole.USER).build();
        List<User> users = Arrays.asList(firstUser, secondUser);

        UserResponseDto firstUserResponse = new UserResponseDto(1, "alex", UserRole.ADMIN);
        UserResponseDto secondUserResponse = new UserResponseDto(2, "james", UserRole.USER);
        List<UserResponseDto> userResponses = Arrays.asList(firstUserResponse, secondUserResponse);

        List<UserResponseDto> result = userMapper.entitiesToResponses(users);
        Assertions.assertEquals(userResponses, result);
    }
}