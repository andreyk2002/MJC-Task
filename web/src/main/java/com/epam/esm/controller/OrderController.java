package com.epam.esm.controller;

import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("")
    public ResponseEntity<CollectionModel<OrderResponseDto>> getPage(@RequestParam @Positive(message = "40021") int size,
                                                                     @RequestParam @PositiveOrZero(message = "40022") int offset) {
        List<OrderResponseDto> page = orderService.getPage(size, offset);
        page.forEach(order -> order.add(linkTo(methodOn(OrderController.class).getById(order.getId())).withRel("getById")));
        List<Link> links = Arrays.asList(
                linkTo(methodOn(OrderController.class).getPage(size, offset)).withSelfRel(),
                linkTo(methodOn(OrderController.class).getPage(size, offset + size)).withRel("nextPage"),
                linkTo(methodOn(OrderController.class).getPage(size, offset - size)).withRel("prevPage")
        );
        CollectionModel<OrderResponseDto> ordersWithLinks = CollectionModel.of(page, links);
        return new ResponseEntity<>(ordersWithLinks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable long id) {
        OrderResponseDto order = orderService.getById(id);
        order.add(
                linkTo(methodOn(OrderController.class).getById(id)).withSelfRel()
        );
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}
