package com.epam.esm.service.excepiton;

public class TagNotFoundException extends NotFoundException {

    public static final int ERROR_CODE = 40401;


    public TagNotFoundException(Object... params) {
        super(ERROR_CODE, params);
    }

    public TagNotFoundException(String message, Object... params) {
        super(message, ERROR_CODE, params);
    }
}
