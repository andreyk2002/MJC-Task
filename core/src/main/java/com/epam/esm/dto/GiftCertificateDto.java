package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class GiftCertificateDto {
    
    private String name;

    private String description;
    
    private BigDecimal price;
    
    private int duration;

}
