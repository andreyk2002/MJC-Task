package com.epam.esm.controller;

import com.epam.esm.request.UserRequestDto;
import com.epam.esm.response.UserResponseDto;
import com.epam.esm.security.AuthRequestDto;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDto authDto) {
        String login = authDto.getLogin();
        String password = authDto.getPassword();
        String passwordHash = DigestUtils.md5Hex(password);
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(login, passwordHash);
        authenticationManager.authenticate(authentication);
        UserResponseDto userResponseDto = userService.findByLogin(login);
        String token = jwtTokenProvider.createToken(login, userResponseDto.getUserRole());
        Map<String, String> result = new HashMap<>();
        result.put("login", authDto.getLogin());
        result.put("token", token);
        return ResponseEntity.ok(result);
    }

    //TODO: logic to service
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto user) {
        user.setPasswordHash(DigestUtils.md5Hex(user.getPasswordHash()));
        UserResponseDto registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

}
