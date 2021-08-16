package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagEntityDtoMapper;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GiftTagService {

    private final TagRepository tagRepo;
    private final CertificateTagRepository certificateTagRepo;
    private final TagEntityDtoMapper mapper;

    public List<TagDto> getAllTags() {
        List<GiftTag> tags = tagRepo.getAll();
        return mapper.tagsToTagsDto(tags);
    }


    @Transactional
    public void deleteById(long id) {
        certificateTagRepo.deleteByTagId(id);
        tagRepo.deleteById(id);
    }

    public Optional<TagDto> getById(long id) {
        Optional<GiftTag> optionalGiftTag = tagRepo.getById(id);
        if (optionalGiftTag.isEmpty()) {
            return Optional.empty();
        }
        GiftTag giftTag = optionalGiftTag.get();
        TagDto tagDto = mapper.tagToTagDto(giftTag);
        return Optional.of(tagDto);
    }

    public void addTag(TagDto tagDto) {
        GiftTag giftTag = mapper.tagDtoToTag(tagDto);
        tagRepo.saveTag(giftTag);
    }
}
