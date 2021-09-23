package com.epam.esm.request;

import lombok.Data;

@Data
public class AuthRequestDto {

    private String login;

    private String password;
}
