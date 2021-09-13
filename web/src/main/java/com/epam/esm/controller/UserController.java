package com.epam.esm.controller;

import com.epam.esm.response.UserResponseDto;
import com.epam.esm.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
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

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {

    private static final int MAX_PAGE = 100;

    private final UserService userService;
    private final OffsetCreator offsetCreator;

    @GetMapping("")
    @ApiOperation("Return a page of users within specified range")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Page were successfully find"),
            @ApiResponse(code = 40021, message = "Page offset were negative"),
            @ApiResponse(code = 400221, message = "Page size were not positive"),
            @ApiResponse(code = 400222, message = "Page size exceeded maximal limit"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<CollectionModel<UserResponseDto>> getPage(
            @ApiParam("number of order from which page starts") @RequestParam @PositiveOrZero(message = "40021") int offset,
            @ApiParam("maximal number of orders in one page") @RequestParam
            @Positive(message = "400221") @Max(value = MAX_PAGE, message = "400222") int size) {
        List<UserResponseDto> page = userService.getPage(size, offset);
        page.forEach(user -> user.add(linkTo(methodOn(UserController.class).getById(user.getId())).withRel("getByID")));
        int nextOffset = offset + size;
        int prevOffset = offsetCreator.createPreviousOffset(offset, size);
        List<Link> links = Arrays.asList(
                linkTo(methodOn(UserController.class).getPage(offset, size)).withSelfRel(),
                linkTo(methodOn(UserController.class).getPage(nextOffset, size)).withRel("nextPage"),
                linkTo(methodOn(UserController.class).getPage(prevOffset, size)).withRel("prevPage")
        );
        CollectionModel<UserResponseDto> usersWithLinks = CollectionModel.of(page, links);
        return new ResponseEntity<>(usersWithLinks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Searches user by specified id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order was successfully created"),
            @ApiResponse(code = 40421, message = "Order with specified id not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<UserResponseDto> getById(@ApiParam("id of the user to be find") @PathVariable long id) {
        UserResponseDto user = userService.getById(id);
        user.add(
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel()
        );
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}

