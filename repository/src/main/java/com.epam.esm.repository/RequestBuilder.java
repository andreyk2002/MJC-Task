package com.epam.esm.repository;

import org.springframework.stereotype.Component;

@Component
class RequestBuilder {
    private static final String FIND_SORTED = "SELECT * FROM gift_certificate";
    private static final String FIND_BY_KEYWORD = "SELECT * FROM gift_certificate WHERE name LIKE " +
            "concat('%', ?, '%') OR description LIKE concat('%', ?, '%')";
    private static final String FIND_BY_TAG_NAME = "SELECT gc.id AS id, gc.name AS name, price, duration, create_date," +
            "description, last_update_date FROM gift_certificate gc JOIN certificate_tag ct ON " +
            "gc.id = ct.certificate_id JOIN tag t ON t.id = ct.tag_id WHERE t.name = ?";
    private static final String FIND_BY_TAG_NAME_KEYWORD = "SELECT gc.id AS id, gc.name AS name, price, duration," +
            "description, create_date, last_update_date FROM gift_certificate gc JOIN certificate_tag ct ON " +
            "gc.id = ct.certificate_id JOIN tag t ON t.id = ct.tag_id WHERE t.name = ? AND (gc.name LIKE " +
            "concat('%', ?, '%') OR gc.description LIKE concat('%', ?, '%'))";
    private static final String ORDER = " ORDER BY ";

    RequestBuilderResult buildSortRequest(String keyword, String tagName, String sortOrder, String field) {
        RequestBuilderResult requestBuilderResult = new RequestBuilderResult();
        StringBuilder query;
        if (keyword == null && tagName == null) {
            query = new StringBuilder(FIND_SORTED);
        } else if (keyword == null) {
            requestBuilderResult.setParams(tagName);
            query = new StringBuilder(FIND_BY_TAG_NAME);
        } else if (tagName == null) {
            requestBuilderResult.setParams(keyword, keyword);
            query = new StringBuilder(FIND_BY_KEYWORD);
        } else {
            query = new StringBuilder(FIND_BY_TAG_NAME_KEYWORD);
            requestBuilderResult.setParams(tagName, keyword, keyword);
        }
        query.append(ORDER);
        query.append(field);
        query.append(" ");
        query.append(sortOrder);
        requestBuilderResult.setQuery(query.toString());
        return requestBuilderResult;
    }
}
