package com.epam.esm.service;

import com.epam.esm.entity.GiftTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NullableFieldsFinderTest {

    private final NullableFieldsFinder fieldsFinder = new NullableFieldsFinder();


    @Test
    void getNullPropertyNamesShouldReturnNullPropertyNames() {
        GiftTag objectForTest = new GiftTag();
        String[] nullPropertyNames = fieldsFinder.getNullPropertyNames(objectForTest);
        Assertions.assertArrayEquals(nullPropertyNames, new String[]{"name"});
    }
}