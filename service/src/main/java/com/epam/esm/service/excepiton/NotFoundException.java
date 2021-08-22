package com.epam.esm.service.excepiton;

import lombok.Getter;


@Getter
public abstract class NotFoundException extends RuntimeException {

    private final Object[] params;

    private final int errorCode;

    public NotFoundException(int errorCode, Object... params) {
        this.errorCode = errorCode;
        this.params = params;
    }

    public NotFoundException(String message, int errorCode, Object... params) {
        super(message);
        this.errorCode = errorCode;
        this.params = params;
    }

    public NotFoundException(String message, Throwable cause, int errorCode, Object... params) {
        super(message, cause);
        this.errorCode = errorCode;
        this.params = params;
    }

    public NotFoundException(Throwable cause, int errorCode, Object... params) {
        super(cause);
        this.errorCode = errorCode;
        this.params = params;
    }


}
