package com.epam.esm.service.excepiton;

public class TagNotFoundException extends NotFoundException {

    private static final int ERROR_CODE = 40401;


    public TagNotFoundException(Object... messageParams) {
        super(ERROR_CODE, messageParams);
    }

    public TagNotFoundException(String message, Object... messageParams) {
        super(message, ERROR_CODE, messageParams);
    }
}
