package com.epam.esm.controller;

import com.epam.esm.paging.OffsetCreator;
import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@AllArgsConstructor
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final JwtTokenProvider jwtTokenProvider;
    private final OrderService orderService;
    private final OffsetCreator offsetCreator;
    private final static int MAX_PAGE = 100;


    @GetMapping("")
    @ApiOperation("Return a page of orders within specified range")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Page were successfully find"),
            @ApiResponse(code = 40021, message = "Page offset were negative"),
            @ApiResponse(code = 400221, message = "Page size were not positive"),
            @ApiResponse(code = 400222, message = "Page size exceeded maximal limit"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<CollectionModel<OrderResponseDto>> getPage(
            @ApiParam("maximal number of orders in one page")
            @RequestParam(defaultValue = "10") @Max(value = MAX_PAGE, message = "400222")
            @Positive(message = "400221") int size,
            @ApiParam("number of order from which page starts")
            @RequestParam(defaultValue = "0") @PositiveOrZero(message = "40021") int page,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        String token = jwtTokenProvider.resolveToken(request);
        String login = jwtTokenProvider.getLogin(token);
        List<OrderResponseDto> ordersPage = orderService.getPage(pageable, login);
        ordersPage.forEach(order -> order.add(linkTo(methodOn(OrderController.class).getById(order.getId())).withRel("getById")));
        int nextPage = page + 1;
        int prevPage = offsetCreator.createPreviousOffset(page);
        List<Link> links = Arrays.asList(
                linkTo(methodOn(OrderController.class).getPage(size, page, request)).withSelfRel(),
                linkTo(methodOn(OrderController.class).getPage(size, nextPage, request)).withRel("nextPage"),
                linkTo(methodOn(OrderController.class).getPage(size, prevPage, request)).withRel("prevPage")
        );
        CollectionModel<OrderResponseDto> ordersWithLinks = CollectionModel.of(ordersPage, links);
        return new ResponseEntity<>(ordersWithLinks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Searches order by specified id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order was successfully created"),
            @ApiResponse(code = 40431, message = "Order with specified id not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<OrderResponseDto> getById(@ApiParam("id of the order to be find") @PathVariable long id) {
        OrderResponseDto order = orderService.getById(id);
        order.add(
                linkTo(methodOn(OrderController.class).getById(id)).withSelfRel()
        );
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
