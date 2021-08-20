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


    @NotEmpty(message = "40011")
    private String name;

    @NotNull(message = "40012")
    private String description;

    @PositiveOrZero(message = "40013")
    private BigDecimal price;

    @PositiveOrZero(message = "40014")
    private Integer duration;

    @NotNull(message = "40015")
    private List<TagRequestDto> tags;

}
