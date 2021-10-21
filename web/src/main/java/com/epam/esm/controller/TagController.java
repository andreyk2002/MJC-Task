package com.epam.esm.controller;


import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.service.GiftTagService;
import com.epam.esm.validation.TagRequestDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation(value = "Removes tag by specified id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed specified tag"),
            @ApiResponse(code = 40401, message = "Requesting tag not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<TagResponseDto> deleteById(@ApiParam(value = "id of the specified tag", required = true)
                                                     @PathVariable long id) {
        TagResponseDto deleteTag = tagService.deleteById(id);
        return new ResponseEntity<>(deleteTag, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Search for tag by specified id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Requested tag was successfully found"),
            @ApiResponse(code = 40401, message = "Requesting tag not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<TagResponseDto> getById(@ApiParam(value = "id of the specified tag", required = true)
                                                  @PathVariable long id) {
        TagResponseDto tag = tagService.getById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("")
    @ApiOperation(value = "Returns list of all available tags", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Requested tag was successfully found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<List<TagResponseDto>> getAll() {
        List<TagResponseDto> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping(value = "")
    @ApiOperation(value = "Add specified tag", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Requested tag was successfully added"),
            @ApiResponse(code = 400, message = "Tag for adding was in incorrect format"),
            @ApiResponse(code = 40001, message = "Tag for adding was in incorrect format : tag name was be empty"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<TagResponseDto> addTag(
            @ApiParam(value = "tag for adding", required = true)
            @RequestBody @Valid TagRequestDto tagRequestDto) {
        TagResponseDto tagResponseDto = tagService.addTag(tagRequestDto);
        return new ResponseEntity<>(tagResponseDto, HttpStatus.CREATED);
    }

}
