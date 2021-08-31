package com.epam.esm.controller;

import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("")
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        List<OrderResponseDto> all = orderService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable long id) {
        OrderResponseDto order = orderService.getById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
