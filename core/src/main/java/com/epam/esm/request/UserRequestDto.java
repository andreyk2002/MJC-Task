package com.epam.esm.request;

import lombok.Data;

import javax.validation.constraints.Size;


//TODO: error codes
@Data
public class UserRequestDto {

    @Size(min = 3, max = 50, message = "")
    private String name;

    @Size(min = 3, max = 50)
    private String login;

    @Size(min = 6, max = 50)
    private String passwordHash;


}
