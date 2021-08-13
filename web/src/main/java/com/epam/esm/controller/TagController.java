package com.epam.esm.controller;


import com.epam.esm.dto.TagDto;
import com.epam.esm.mapping.TagModelDtoMapper;
import com.epam.esm.service.GitTagService;
import com.epam.esm.validation.TagModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private final GitTagService tagService;
    private final TagModelDtoMapper mapper;


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        tagService.deleteById(id);
    }

    @GetMapping("/{id}")
    public TagDto getById(@PathVariable long id){
        return tagService.getById(id);
    }

    @GetMapping("")
    public List<TagDto> getAll(){
        return tagService.getAllTags();
    }

    @PostMapping("")
    public void addTag(@RequestBody TagModel tagModel){
        TagDto tagDto = mapper.tagModelToDto(tagModel);
        tagService.addTag(tagDto);
    }

}
