package com.epam.esm.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter

public class TagRequestDtoCertificate {


    private long id;

    @NotEmpty(message = "40001")
    private String name;

}
