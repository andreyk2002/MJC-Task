package com.epam.esm.service;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.excepiton.TagAlreadyExistException;
import com.epam.esm.service.excepiton.TagNotFoundException;
import com.epam.esm.validation.TagRequestDto;
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
    private final TagMapper mapper;

    public List<TagResponseDto> getAllTags() {
        List<GiftTag> tags = tagRepo.getAll();
        return mapper.entitiesToRequests(tags);
    }


    @Transactional
    public TagResponseDto deleteById(long id) {
        TagResponseDto tagToDelete = getById(id);
        certificateTagRepo.deleteByTagId(id);
        tagRepo.deleteById(id);
        return tagToDelete;
    }

    public TagResponseDto getById(long id) {
        Optional<GiftTag> optionalGiftTag = tagRepo.getById(id);
        return optionalGiftTag.map(mapper::entityToRequest).orElseThrow(() -> new TagNotFoundException(id));
    }

    TagResponseDto updateTag(TagRequestDto tagRequestDto) {
        GiftTag giftTag = mapper.requestToEntity(tagRequestDto);
        long id = tagRequestDto.getId();
        Optional<GiftTag> optional = tagRepo.getById(id);
        return optional.map(tag -> {
            tagRepo.updateTag(giftTag);
            return getById(id);
        }).orElse(getById(tagRepo.addTag(giftTag)));
    }

    public TagResponseDto addTag(TagRequestDto tagRequestDto) {
        long id = tagRequestDto.getId();
        Optional<GiftTag> optionalGiftTag = tagRepo.getById(id);
        optionalGiftTag.ifPresent(giftTag -> {
            throw new TagAlreadyExistException();
        });
        GiftTag giftTag = mapper.requestToEntity(tagRequestDto);
        long insertId = tagRepo.addTag(giftTag);
        return getById(insertId);
    }
}
