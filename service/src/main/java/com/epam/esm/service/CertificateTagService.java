package com.epam.esm.service;

import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import com.epam.esm.mappers.TagMapper;
import com.epam.esm.repository.CertificateTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides set of operations with {@link GiftCertificate} and {@link GiftTag} connections
 */

@Service
@AllArgsConstructor
public class CertificateTagService {
    private final CertificateTagRepository repository;
    private final TagMapper mapper;

    /**
     * Gets all certificate tags from repository
     *
     * @param certificateId - id of specified tag
     * @return {@link List} of all tags, which belong to specified certificate
     */
    public List<TagResponseDto> getTagsByCertificateId(long certificateId) {
        List<GiftTag> tags = repository.getTagByCertificateId(certificateId);
        return mapper.entitiesToRequests(tags);
    }
}
