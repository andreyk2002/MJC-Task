package com.epam.esm.repository;

import lombok.Getter;
import lombok.Setter;

class RequestBuilderResult {
    @Getter
    private String[] params;

    @Getter
    @Setter
    private String query;

    void setParams(String... params) {
        this.params = params;
    }

}
