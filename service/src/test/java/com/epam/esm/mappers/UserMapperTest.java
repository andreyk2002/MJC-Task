package com.epam.esm.mappers;

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
        User user = new User(1, "alex");
        UserResponseDto userResponseDto = userMapper.entityToResponse(user);
        UserResponseDto expected = new UserResponseDto(1, "alex");
        Assertions.assertEquals(expected, userResponseDto);
    }

    @Test
    void entitiesToResponses() {
        User firstUser = new User(1, "alex");
        User secondUser = new User(2, "james");
        List<User> users = Arrays.asList(firstUser, secondUser);

        UserResponseDto firstUserResponse = new UserResponseDto(1, "alex");
        UserResponseDto secondUserResponse = new UserResponseDto(2, "james");
        List<UserResponseDto> userResponses = Arrays.asList(firstUserResponse, secondUserResponse);

        List<UserResponseDto> result = userMapper.entitiesToResponses(users);
        Assertions.assertEquals(userResponses, result);
    }
}