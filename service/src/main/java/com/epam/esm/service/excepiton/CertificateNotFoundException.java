package com.epam.esm.service.excepiton;

public class CertificateNotFoundException extends NotFoundException {

    private static final int ERROR_CODE = 40411;

    public CertificateNotFoundException(Object... messageParams) {
        super(ERROR_CODE, messageParams);
    }

    public CertificateNotFoundException(String message, Object... messageParams) {
        super(message, ERROR_CODE, messageParams);
    }


}
