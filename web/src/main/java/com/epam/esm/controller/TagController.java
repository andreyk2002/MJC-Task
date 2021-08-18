package com.epam.esm.controller;


import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.service.GiftTagService;
import com.epam.esm.validation.TagRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
@Validated
public class TagController {

    private final GiftTagService tagService;


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        Optional<TagResponseDto> deleteTag = tagService.deleteById(id);
        if (!deleteTag.isPresent()) {
            return new ResponseEntity<>(String.format("Tag with id %d not found", id), HttpStatus.NOT_FOUND);
        }
        TagResponseDto deleted = deleteTag.get();
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Optional<TagResponseDto> tag = tagService.getById(id);
        if (!tag.isPresent()) {
            return new ResponseEntity<>(String.format("Tag with id %d not found", id), HttpStatus.NOT_FOUND);
        }
        TagResponseDto tagResponseDto = tag.get();
        return new ResponseEntity<>(tagResponseDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<TagResponseDto>> getAll() {
        List<TagResponseDto> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<TagResponseDto> addTag(@RequestBody @Valid TagRequestDto tagRequestDto) {
        TagResponseDto tagResponseDto = tagService.addTag(tagRequestDto);
        return new ResponseEntity<>(tagResponseDto, HttpStatus.CREATED);
    }

}
