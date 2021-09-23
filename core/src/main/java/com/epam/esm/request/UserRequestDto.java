package com.epam.esm.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class UserRequestDto {


    @Pattern(regexp = "[a-zA-z]{3,50}", message = "40051")
    private String name;

    @Size(min = 3, max = 50, message = "40052")
    private String login;

    @NotEmpty(message = "40053")
    private String password;

}
