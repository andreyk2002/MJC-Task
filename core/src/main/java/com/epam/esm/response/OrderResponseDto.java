package com.epam.esm.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto extends RepresentationModel<OrderResponseDto> {

    private long id;

    private UserResponseDto user;

    private List<CertificateResponseDto> certificates;

    private LocalDateTime purchaseDate;

    private BigDecimal orderPrice;
}
