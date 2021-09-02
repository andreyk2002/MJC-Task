package com.epam.esm.controller;

import com.epam.esm.response.UserResponseDto;
import com.epam.esm.service.UserService;
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
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<CollectionModel<UserResponseDto>> getPage(@RequestParam @Positive(message = "40021") int size,
                                                                    @RequestParam @PositiveOrZero(message = "40021") int offset) {
        List<UserResponseDto> page = userService.getPage(size, offset);
        page.forEach(user -> user.add(linkTo(methodOn(UserController.class).getById(user.getId())).withRel("getByID")));
        List<Link> links = Arrays.asList(
                linkTo(methodOn(UserController.class).getPage(size, offset)).withSelfRel(),
                linkTo(methodOn(UserController.class).getPage(size, offset + size)).withRel("nextPage"),
                linkTo(methodOn(UserController.class).getPage(size, offset - size)).withRel("prevPage")
        );
        CollectionModel<UserResponseDto> usersWithLinks = CollectionModel.of(page, links);
        return new ResponseEntity<>(usersWithLinks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable long id) {
        UserResponseDto user = userService.getById(id);
        user.add(
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel()
        );
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}

