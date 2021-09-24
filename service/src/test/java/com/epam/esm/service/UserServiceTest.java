package com.epam.esm.service;

import com.epam.esm.UserRole;
import com.epam.esm.entity.User;
import com.epam.esm.mappers.UserMapper;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.request.UserRequestDto;
import com.epam.esm.response.UserResponseDto;
import com.epam.esm.service.excepiton.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;


    @Test
    void testFindByLoginShouldThrowFindByNotExistingLogin() {
        String notExistingLogin = "sfda";
        when(userRepository.findByLogin(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findByLogin(notExistingLogin));
        verify(userRepository).findByLogin(notExistingLogin);
    }

    @Test
    void testFindByLoginShouldFindByExistingLogin() {
        String existing = "lll";
        User userByLogin = User.builder().id(1).login(existing).name("Alex").role(UserRole.USER).build();
        UserResponseDto responseDto = new UserResponseDto(1, "Alex", UserRole.USER);
        when(userRepository.findByLogin(any())).thenReturn(Optional.of(userByLogin));
        when(userMapper.entityToResponse(any())).thenReturn(responseDto);

        UserResponseDto result = userService.findByLogin(existing);
        Assertions.assertEquals(result, responseDto);
        verify(userRepository).findByLogin(existing);
        verify(userMapper).entityToResponse(userByLogin);
    }

    @Test
    void testRegisterUserShouldRegisterUser() {
        UserRequestDto userToRegister = new UserRequestDto("name", "login", "password");
        User mappedUser = User.builder().name("name").login("login").passwordHash("password").build();
        User addedUser = User.builder()
                .id(1)
                .name("name")
                .role(UserRole.USER)
                .passwordHash("password")
                .login("login")
                .build();
        UserResponseDto registeredUser = new UserResponseDto(1, "name", UserRole.USER);
        when(userMapper.requestToEntity(any())).thenReturn(mappedUser);
        when(userRepository.createUser(any())).thenReturn(addedUser);
        when(userMapper.entityToResponse(any())).thenReturn(registeredUser);

        UserResponseDto result = userService.registerUser(userToRegister);
        Assertions.assertEquals(result, registeredUser);

        verify(userMapper).requestToEntity(userToRegister);
        verify(userRepository).createUser(mappedUser);
        verify(userMapper).entityToResponse(addedUser);


    }


    @Test
    void testGetPageShouldReturnValidPage() {
        User firstUser = User.builder().name("alex").id(1).role(UserRole.ADMIN).build();
        User secondUser = User.builder().name("james").id(2).role(UserRole.USER).build();
        List<User> users = Arrays.asList(firstUser, secondUser);

        UserResponseDto firstUserDto = new UserResponseDto(1, "alex", UserRole.ADMIN);
        UserResponseDto secondUserDto = new UserResponseDto(1, "james", UserRole.USER);
        List<UserResponseDto> allExpected = Arrays.asList(firstUserDto, secondUserDto);


        int size = 2;
        int offset = 0;
        when(userRepository.getPage(size, offset)).thenReturn(users);
        when(userMapper.entitiesToResponses(anyList())).thenReturn(allExpected);

        List<UserResponseDto> all = userService.getPage(size, offset);

        Assertions.assertEquals(allExpected, all);
        verify(userMapper).entitiesToResponses(users);
    }

    @Test
    void testGetByIdShouldReturnUserWhenExists() {
        long existingId = 1;
        User user = User.builder().name("alex").id(existingId).role(UserRole.ADMIN).build();
        UserResponseDto expectedResult = new UserResponseDto(existingId, "alex", UserRole.ADMIN);

        when(userRepository.getById(existingId)).thenReturn(Optional.of(user));
        when(userMapper.entityToResponse(any())).thenReturn(expectedResult);

        UserResponseDto result = userService.getById(existingId);
        Assertions.assertEquals(expectedResult, result);

        verify(userRepository).getById(existingId);
        verify(userMapper).entityToResponse(user);

    }

    @Test
    void testGetByIdShouldThrowWhenNotFound() {
        long notExistingId = 666;
        when(userRepository.getById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getById(notExistingId));

        verify(userRepository).getById(notExistingId);
    }

}