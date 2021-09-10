package com.epam.esm.repository;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateFilter {

    @Pattern(regexp = "asc|desc", message = "400161")
    private String sortOrder = "asc";

    @Pattern(regexp = "id|name|create_date", message = "400161")
    private String sortField = "id";

    private String keyword;

    private String tagName;

    @Positive(message = "400221")
    @Max(value = 100, message = "400222")
    private int pageSize = 10;

    @PositiveOrZero(message = "40021")
    private int offset = 0;
}
