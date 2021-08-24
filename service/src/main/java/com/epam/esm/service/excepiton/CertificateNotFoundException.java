package com.epam.esm.service.excepiton;

public class CertificateNotFoundException extends NotFoundException {

    private static final int ERROR_CODE = 40411;

    public CertificateNotFoundException(Object... params) {
        super(ERROR_CODE, params);
    }

    public CertificateNotFoundException(String message, Object... params) {
        super(message, ERROR_CODE, params);
    }


}
