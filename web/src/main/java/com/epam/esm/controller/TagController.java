package com.epam.esm.controller;


import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.service.GiftTagService;
import com.epam.esm.validation.TagRequestDto;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
@Validated
public class TagController {

    private final GiftTagService tagService;


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        TagResponseDto deleteTag = tagService.deleteById(id);
        return new ResponseEntity<>(deleteTag, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        TagResponseDto tag = tagService.getById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<TagResponseDto>> getAll() {
        List<TagResponseDto> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<TagResponseDto> addTag(
            @ApiParam(value = "createRequest", required = true)
            @RequestBody @Valid TagRequestDto tagRequestDto) {
        TagResponseDto tagResponseDto = tagService.addTag(tagRequestDto);
        return new ResponseEntity<>(tagResponseDto, HttpStatus.CREATED);
    }

}
