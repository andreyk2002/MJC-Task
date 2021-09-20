package com.epam.esm.controller;


import com.epam.esm.request.TagRequestDto;
import com.epam.esm.response.TagResponseDto;
import com.epam.esm.service.GiftTagService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
@Validated
public class TagController {

    private static final int MAX_PAGE = 100;
    private final GiftTagService tagService;
    private final OffsetCreator offsetCreator;


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
        deleteTag.add(
                linkTo(methodOn(TagController.class).getById(id)).withRel("getById"),
                linkTo(methodOn(TagController.class).deleteById(id)).withSelfRel(),
                linkTo(methodOn(TagController.class).findTopUserTopTag()).withRel("topUserTopTag")
        );
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
        tag.add(
                linkTo(methodOn(TagController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(TagController.class).deleteById(id)).withRel("deleteTag"),
                linkTo(methodOn(TagController.class).findTopUserTopTag()).withRel("topUserTopTag")
        );
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("")
    @ApiOperation(value = " Return a page of tags within specified range",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Page were successfully find"),
            @ApiResponse(code = 40021, message = "Page offset were negative"),
            @ApiResponse(code = 400221, message = "Page size were not positive"),
            @ApiResponse(code = 400222, message = "Page size exceeded maximal limit"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<CollectionModel<TagResponseDto>> getPage(
            @ApiParam("number of tag from which page starts") @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "40021") int offset,
            @ApiParam("maximal number of orders in one page") @RequestParam(defaultValue = "10")
            @Positive(message = "400221")
            @Max(value = MAX_PAGE, message = "400222") int size) {
        List<TagResponseDto> page = tagService.getPage(offset, size);
        page.forEach(tag -> tag.add(linkTo(methodOn(TagController.class).getById(tag.getId())).withRel("findTag")));
        int nextOffset = offset + size;
        int prevOffset = offsetCreator.createPreviousOffset(offset, size);
        List<Link> links = Arrays.asList(
                linkTo(methodOn(TagController.class).getPage(offset, size)).withSelfRel(),
                linkTo(methodOn(TagController.class).getPage(nextOffset, size)).withRel("nextPage"),
                linkTo(methodOn(TagController.class).getPage(prevOffset, size)).withRel("prevPage"),
                linkTo(methodOn(TagController.class).findTopUserTopTag()).withRel("topUserTopTag")
        );
        CollectionModel<TagResponseDto> tagsWithLinks = CollectionModel.of(page, links);
        return new ResponseEntity<>(tagsWithLinks, HttpStatus.OK);
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
        TagResponseDto tag = tagService.addTag(tagRequestDto);
        tag.add(
                linkTo(methodOn(TagController.class).addTag(tagRequestDto)).withSelfRel(),
                linkTo(methodOn(TagController.class).getById(tag.getId())).withRel("getById"),
                linkTo(methodOn(TagController.class).deleteById(tag.getId())).withRel("deleteById"),
                linkTo(methodOn(TagController.class).findTopUserTopTag()).withRel("topUserTopTag")
        );
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping(value = "/topUserTopTag")
    @ApiOperation(value = "Searches for the most widely used tag of a user with the highest cost of all orders",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Requested tag was successfully find"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<TagResponseDto> findTopUserTopTag() {
        TagResponseDto topUserTopTag = tagService.getTopUserTopTag();
        topUserTopTag.add(
                linkTo(methodOn(TagController.class).getById(topUserTopTag.getId())).withRel("getById"),
                linkTo(methodOn(TagController.class).deleteById(topUserTopTag.getId())).withRel("deleteById"),
                linkTo(methodOn(TagController.class).findTopUserTopTag()).withSelfRel()
        );
        return new ResponseEntity<>(topUserTopTag, HttpStatus.OK);
    }

}
