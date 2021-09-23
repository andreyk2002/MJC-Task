package com.epam.esm.controller;

import com.epam.esm.request.AuthRequestDto;
import com.epam.esm.request.UserRequestDto;
import com.epam.esm.response.UserResponseDto;
import com.epam.esm.service.AuthService;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDto authDto) {
        Map<String, String> tokenAndLogin = authService.authenticate(authDto);
        return ResponseEntity.ok(tokenAndLogin);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto user) {
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        UserResponseDto registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

}
