package com.epam.esm.paging;

import org.springframework.stereotype.Component;

@Component
public class OffsetCreator {

    public int createPreviousOffset(int page) {
        int prevPage = page - 1;
        return Math.max(prevPage, 0);
    }
}
