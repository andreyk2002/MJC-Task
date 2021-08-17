package com.epam.esm.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateRequestDto {
    private long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Description cannot be null")
    private String description;

    @PositiveOrZero(message = "price can't be less than 0")
    private BigDecimal price;

    @PositiveOrZero(message = "duration can't be less than 0")
    private Integer duration;

    @NotNull(message = "Tags should be not null")
    private List<TagRequestDto> tags;

}
