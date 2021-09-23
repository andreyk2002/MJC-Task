package com.epam.esm.paging;

import org.springframework.stereotype.Component;

@Component
public class OffsetCreator {

    public int createPreviousOffset(int offset, int size) {
        int prevOffset = offset - size;
        if (prevOffset < 0) {
            return 0;
        }
        return prevOffset;
    }
}
