package com.epam.esm.service.excepiton;

public class OrderNotFoundException extends NotFoundException {

    private static final int ERROR_CODE = 40431;

    public OrderNotFoundException(Object... params) {
        super(ERROR_CODE, params);
    }

    public OrderNotFoundException(String message, Object... params) {
        super(message, ERROR_CODE, params);
    }
}
