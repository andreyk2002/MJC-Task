package com.epam.esm.service.excepiton;

public class CertificateNotFoundException extends CodeException {
    public CertificateNotFoundException() {
    }

    public CertificateNotFoundException(String message) {
        super(message);
    }

    public CertificateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getCode() {
        return 40411;
    }

}
