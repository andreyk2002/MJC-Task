package com.epam.esm.controller;


import com.epam.esm.service.GitTagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private final GitTagService tagService;


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        tagService.deleteById(id);
    }

}
