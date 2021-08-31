package com.epam.esm.controller;

import com.epam.esm.response.UserResponseDto;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<UserResponseDto> allUsers = userService.getAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable long id) {
        UserResponseDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
