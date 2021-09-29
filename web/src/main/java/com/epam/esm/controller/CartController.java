package com.epam.esm.controller;


import com.epam.esm.certificates.Certificates;
import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.CartService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;


    //TODO: user from token
    @PostMapping("")
    @ApiOperation(value = "Adds order for user with specified id")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Order was successfully created"),
            @ApiResponse(code = 40041, message = "List of order certificates were null or empty"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<OrderResponseDto> createOrder(
            @ApiParam("id of the user to which order belongs to")
                    Principal userPrincipal,
            @ApiParam("list of certificates included in the order") @RequestBody
            @Valid @NotEmpty(message = "40041") Certificates certificates) {
        String login = userPrincipal.getName();
        OrderResponseDto order = cartService.createOrder(login, certificates.getCertificates());
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
