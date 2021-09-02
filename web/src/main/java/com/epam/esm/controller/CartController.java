package com.epam.esm.controller;


import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/basket")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;


    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable long userId,
                                                        @RequestBody @Valid @NotEmpty
                                                                List<Integer> certificates) {
        OrderResponseDto order = cartService.createOrder(userId, certificates);
        order.add(
                linkTo(methodOn(CartController.class).createOrder(userId, certificates)).withSelfRel()
        );
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
