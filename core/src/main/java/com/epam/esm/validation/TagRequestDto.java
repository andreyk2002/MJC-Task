package com.epam.esm.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagRequestDto {

    //
    @Min(value = 0, message = "40002")
    @Max(value = 0, message = "40002")
    private long id;

    @NotEmpty(message = "40001")
    private String name;

}
