package com.epam.esm.repository;

import lombok.Getter;
import lombok.Setter;

public class RequestResult {
    @Getter
    private String[] params;

    @Getter
    @Setter
    private String query;

    public void setParams(String... params) {
        this.params = params;
    }

}
