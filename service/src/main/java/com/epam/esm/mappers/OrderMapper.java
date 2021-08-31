package com.epam.esm.mappers;

import com.epam.esm.entity.Order;
import com.epam.esm.response.OrderResponseDto;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


@AllArgsConstructor
@Mapper(componentModel = "spring")
public abstract class OrderMapper {


    protected CertificateMapper certificateMapper;

    protected UserMapper userMapper;


    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "giftCertificate",
                    expression = "java(certificateMapper.entityToResponse(order.getGiftCertificate()))"),
            @Mapping(target = "userResponseDto",
                    expression = "java(userMapper.entityToResponse(order.getUser()))"),
            @Mapping(target = "purchaseDate", source = "purchaseDate"),
            @Mapping(target = "orderPrice", source = "orderPrice"),

    })
    public abstract OrderResponseDto entityToResponse(Order order);

    public abstract List<OrderResponseDto> entitiesToResponse(List<Order> orders);
}
