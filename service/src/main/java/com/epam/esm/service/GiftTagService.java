package com.epam.esm.service;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagRequestMapper;
import com.epam.esm.mappers.TagResponseMapper;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.TagRepository;
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
    private final TagRequestMapper requestMapper;
    private final TagResponseMapper responseMapper;

    public List<TagResponseDto> getAllTags() {
        List<GiftTag> tags = tagRepo.getAll();
        return responseMapper.entitiesToRequests(tags);
    }


    @Transactional
    public Optional<TagResponseDto> deleteById(long id) {
        Optional<TagResponseDto> tagToDelete = getById(id);
        certificateTagRepo.deleteByTagId(id);
        tagRepo.deleteById(id);
        return tagToDelete;
    }

    public Optional<TagResponseDto> getById(long id) {
        Optional<GiftTag> optionalGiftTag = tagRepo.getById(id);
        if (optionalGiftTag.isEmpty()) {
            return Optional.empty();
        }
        GiftTag giftTag = optionalGiftTag.get();
        TagResponseDto tagResponseDto = responseMapper.entityToRequest(giftTag);
        return Optional.of(tagResponseDto);
    }

    public TagResponseDto addTag(TagRequestDto tagRequestDto) {
        GiftTag giftTag = requestMapper.requestToEntity(tagRequestDto);
        long insertId = tagRepo.addTag(giftTag);
        Optional<TagResponseDto> insertedTag = getById(insertId);
        return insertedTag.get();
    }
}
