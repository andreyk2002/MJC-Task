package com.epam.esm.repository;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CertificateFilter {

    private final String sortOrder;
    private final String sortString;
    private final String keyword;
    private final String tagName;
    private final int pageSize;
    private final int offset;
}
