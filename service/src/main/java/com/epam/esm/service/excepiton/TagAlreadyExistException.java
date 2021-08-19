package com.epam.esm.service.excepiton;

public class TagAlreadyExistException extends CodeException {


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

    @Override
    public int getCode() {
        return 40402;
    }
}
