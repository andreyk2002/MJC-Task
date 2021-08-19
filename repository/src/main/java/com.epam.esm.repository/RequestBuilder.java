package com.epam.esm.repository;

import org.springframework.stereotype.Component;

@Component
public class RequestBuilder {
    private static final String FIND_SORTED = "SELECT * FROM gift_certificate";
    private static final String FIND_BY_KEYWORD = "SELECT * FROM gift_certificate WHERE name LIKE " +
            "concat('%', ?, '%') OR description LIKE concat('%', ?, '%')";
    private static final String FIND_BY_TAG_NAME = "SELECT * FROM gift_certificate gc JOIN certificate_tag ct ON " +
            "gc.id = ct.id JOIN tag t ON t.id = ct.tag_id WHERE t.name = ?";
    private static final String FIND_BY_TAG_NAME_KEYWORD = "SELECT * FROM gift_certificate gc JOIN certificate_tag ct ON " +
            "gc.id = ct.certificate_id JOIN tag t ON t.id = ct.tag_id WHERE t.name = ? AND (gc.name LIKE " +
            "concat('%', ?, '%') OR gc.description LIKE concat('%', ?, '%'))";
    public static final String ORDER = " ORDER BY ";

    public String buildSortRequest(String keyword, String tagName, String sortOrder, String field) {
        StringBuilder result;
        if (keyword == null && tagName == null) {
            result = new StringBuilder(FIND_SORTED);
        } else if (keyword == null) {
            result = new StringBuilder(FIND_BY_TAG_NAME);
        } else if (tagName == null) {
            result = new StringBuilder(FIND_BY_KEYWORD);
        } else {
            result = new StringBuilder(FIND_BY_TAG_NAME_KEYWORD);
        }
        result.append(ORDER);
        result.append(field);
        result.append(" ");
        result.append(sortOrder);
        return result.toString();
    }
}
