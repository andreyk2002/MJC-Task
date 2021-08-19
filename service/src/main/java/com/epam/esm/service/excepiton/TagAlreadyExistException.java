package com.epam.esm.service.excepiton;

public class TagAlreadyExistException extends RuntimeException {

    public TagAlreadyExistException() {
    }

    public TagAlreadyExistException(String message) {
        super(message);
    }

    public TagAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public TagAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
