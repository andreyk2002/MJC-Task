package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagEntityDtoMapper;
import com.epam.esm.repository.CertificateTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CertificateTagService {
    private final CertificateTagRepository repository;
    private final TagEntityDtoMapper mapper;

    public List<TagDto> getTagsByCertificateId(long certificateId){
        List<GiftTag> tags = repository.getTagByCertificateId(certificateId);
        return mapper.tagsToTagsDto(tags);
    }
}
