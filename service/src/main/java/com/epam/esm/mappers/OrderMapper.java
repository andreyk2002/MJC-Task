package com.epam.esm.mappers;

import com.epam.esm.entity.Order;
import com.epam.esm.response.OrderResponseDto;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Mapper(componentModel = "spring")
@Setter
public abstract class OrderMapper {

    @Autowired
    protected CertificateMapper certificateMapper;

    @Autowired
    protected UserMapper userMapper;


    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "certificates",
                    expression = "java(certificateMapper.entitiesToResponses(order.getCertificates()))"),
            @Mapping(target = "user",
                    expression = "java(userMapper.entityToResponse(order.getUser()))"),
            @Mapping(target = "purchaseDate", source = "purchaseDate"),
            @Mapping(target = "orderPrice", source = "orderPrice"),

    })
    public abstract OrderResponseDto entityToResponse(Order order);

    public abstract List<OrderResponseDto> entitiesToResponse(List<Order> orders);
}
