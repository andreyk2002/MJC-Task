package com.epam.esm.security;

import lombok.Data;

@Data
public class AuthRequestDto {

    private String login;

    private String password;
}
