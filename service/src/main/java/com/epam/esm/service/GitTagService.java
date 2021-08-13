package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagEntityDtoMapper;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GitTagService {

    private final TagRepository tagRepo;
    private final CertificateTagRepository certificateTagRepo;
    private final TagEntityDtoMapper mapper;

    public List<TagDto> getAllTags() {
        List<GiftTag> tags = tagRepo.getAll();
        return mapper.tagsToTagsDto(tags);
    }


    public void deleteById(long id) {
        certificateTagRepo.deleteByTagId(id);
        tagRepo.deleteById(id);
    }

    public TagDto getById(long id) {
        GiftTag giftTag = tagRepo.getById(id);
        return mapper.tagToTagDto(giftTag);
    }

    public void addTag(TagDto tagDto) {
        GiftTag giftTag = mapper.tagDtoToTag(tagDto);
        tagRepo.saveTag(giftTag);
    }
}
