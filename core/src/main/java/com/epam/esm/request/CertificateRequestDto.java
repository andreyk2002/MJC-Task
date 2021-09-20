package com.epam.esm.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CertificateRequestDto {

    @PositiveOrZero
    private long id;

    @NotEmpty(message = "40011")
    private String name;

    @NotNull(message = "40012")
    private String description;

    @PositiveOrZero(message = "40013")
    private BigDecimal price;

    @PositiveOrZero(message = "40014")
    private Integer duration;

    @NotNull(message = "40015")
    private List<TagRequestDtoCertificate> tags;

}
