package com.epam.esm.service;

import com.epam.esm.request.AuthRequestDto;
import com.epam.esm.response.UserResponseDto;
import com.epam.esm.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public Map<String, String> authenticate(AuthRequestDto authDto) {
        String login = authDto.getLogin();
        String password = authDto.getPassword();
        String passwordHash = DigestUtils.md5Hex(password);
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(login, passwordHash);
        authenticationManager.authenticate(authentication);
        UserResponseDto userResponseDto = userService.findByLogin(login);
        String token = jwtTokenProvider.createToken(login, userResponseDto.getRole());
        return Map.of("login", authDto.getLogin(), "token", token);
    }


}
