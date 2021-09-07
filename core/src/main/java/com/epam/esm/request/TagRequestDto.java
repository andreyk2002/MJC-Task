package com.epam.esm.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
