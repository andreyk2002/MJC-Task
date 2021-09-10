package com.epam.esm.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto extends RepresentationModel<OrderResponseDto> {

    private long id;

    private UserResponseDto user;

    private List<CertificateResponseDto> certificates;

    private LocalDateTime purchaseDate;

    private BigDecimal orderPrice;
}
