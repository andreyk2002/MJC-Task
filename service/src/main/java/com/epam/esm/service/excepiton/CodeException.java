package com.epam.esm.service.excepiton;

public abstract class CodeException extends RuntimeException {
    public CodeException() {
    }

    public CodeException(String message) {
        super(message);
    }

    public CodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeException(Throwable cause) {
        super(cause);
    }

    public abstract int getCode();
}
