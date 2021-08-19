package com.epam.esm.service.excepiton;

public class TagNotFoundException extends CodeException {
    public TagNotFoundException() {
    }

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getCode() {
        return 40401;
    }

}
