package com.epam.esm.controller;


import com.epam.esm.dto.TagDto;
import com.epam.esm.mappers.TagModelDtoMapper;
import com.epam.esm.service.GiftTagService;
import com.epam.esm.validation.TagModel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final TagModelDtoMapper mapper;


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        tagService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Optional<TagDto> tag = tagService.getById(id);
        if (!tag.isPresent()) {
            return new ResponseEntity<>(String.format("Tag with id %d not found", id), HttpStatus.NOT_FOUND);
        }
        TagDto tagDto = tag.get();
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<TagDto>> getAll() {
        List<TagDto> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTag(@RequestBody @Valid TagModel tagModel) {
        TagDto tagDto = mapper.tagModelToDto(tagModel);
        tagService.addTag(tagDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
