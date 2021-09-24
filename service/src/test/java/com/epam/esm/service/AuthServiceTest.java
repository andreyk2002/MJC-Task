package com.epam.esm.service;

import com.epam.esm.UserRole;
import com.epam.esm.request.AuthRequestDto;
import com.epam.esm.response.UserResponseDto;
import com.epam.esm.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testAuthenticateShouldAuthenticate() {

        String userLogin = "login";
        String password = "password";
        AuthRequestDto auth = new AuthRequestDto(userLogin, password);
        String mockedToken = "mockedToken";
        UserResponseDto userResponseDto = new UserResponseDto(1, "bla", UserRole.USER);
        Map<String, String> login = Map.of("login", userLogin, "token", mockedToken);
        when(userService.findByLogin(any())).thenReturn(userResponseDto);
        when(jwtTokenProvider.createToken(any(), any())).thenReturn(mockedToken);

        Map<String, String> result = authService.authenticate(auth);

        Assertions.assertEquals(result, login);

        verify(userService).findByLogin(userLogin);
        verify(jwtTokenProvider).createToken(userLogin, UserRole.USER);
    }
}