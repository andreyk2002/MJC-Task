package com.epam.esm.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagRequestDto {

    private long id;

    @NotEmpty(message = "40001")
    private String name;

}
