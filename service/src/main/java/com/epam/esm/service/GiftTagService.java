package com.epam.esm.service;

import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.CertificateTagJdbcRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.request.TagRequestDto;
import com.epam.esm.response.TagResponseDto;
import com.epam.esm.service.excepiton.TagAlreadyExistException;
import com.epam.esm.service.excepiton.TagNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Provides set of operations with {@link GiftTag} entities
 */


@Service
@AllArgsConstructor
public class GiftTagService {

    private final TagRepository tagRepo;

    private final CertificateTagJdbcRepository certificateTagRepo;

    private final TagMapper mapper;



    /**
     * Gets all available tags
     *
     * @return list of all tags, available in the repository
     */
    public List<TagResponseDto> getAllTags() {
        List<GiftTag> tags = tagRepo.getAll();
        return mapper.entitiesToResponses(tags);
    }


    /**
     * Removes tag by its id
     *
     * @param id ID of tag needed to be deleted
     * @return instance of {@link TagResponseDto} which is already removed from repository
     */
    @Transactional
    public TagResponseDto deleteById(long id) {
        TagResponseDto tagToDelete = getById(id);
        certificateTagRepo.deleteByTagId(id);
        tagRepo.deleteById(id);
        return tagToDelete;
    }

    /**
     * Get tag from repository by its id
     *
     * @param id - id of required tag
     * @return instance of required tag
     * @throws TagNotFoundException if tag not found in repository
     */
    public TagResponseDto getById(long id) {
        Optional<GiftTag> optionalGiftTag = tagRepo.getById(id);
        return optionalGiftTag.map(mapper::entityToResponse).orElseThrow(() -> new TagNotFoundException(id));
    }

    /**
     * Updates instance of specified tag. If no tag found new instance will be create
     *
     * @param tagRequestDto Contains updated state of tag instance
     * @return instance of {@link TagResponseDto} which is already updated in repository
     */
    TagResponseDto updateTag(TagRequestDto tagRequestDto) {
        GiftTag giftTag = mapper.requestToEntity(tagRequestDto);
        long id = tagRequestDto.getId();
        Optional<GiftTag> optional = tagRepo.getById(id);
        return optional.map(tag -> {
            tagRepo.updateTag(giftTag);
            return getById(id);
        }).orElseGet(() -> mapper.entityToResponse(tagRepo.addTag(giftTag)));
    }

    /**
     * Adds a requested instance of tag to repository
     *
     * @param tagRequestDto - instance needed to be added
     * @return instance of {@link TagResponseDto} which is already added to repository
     */
    public TagResponseDto addTag(TagRequestDto tagRequestDto) {
        long id = tagRequestDto.getId();
        Optional<GiftTag> optionalGiftTag = tagRepo.getById(id);
        optionalGiftTag.ifPresent(giftTag -> {
            throw new TagAlreadyExistException();
        });
        GiftTag giftTag = mapper.requestToEntity(tagRequestDto);
        GiftTag addedTag = tagRepo.addTag(giftTag);
        return mapper.entityToResponse(addedTag);
    }

    public TagResponseDto getTopUserTopTag() {
        GiftTag topUserTopTag = tagRepo.getTopUserTopTag();
        return mapper.entityToResponse(topUserTopTag);
    }

    public List<TagResponseDto> getPage(int offset, int size) {
        List<GiftTag> page = tagRepo.getPage(offset, size);
        return mapper.entitiesToResponses(page);
    }
}
