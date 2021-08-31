package com.epam.esm.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private long id;

    private UserResponseDto userResponseDto;

    private CertificateResponseDto giftCertificate;

    private LocalDateTime purchaseDate;

    private BigDecimal orderPrice;
}
