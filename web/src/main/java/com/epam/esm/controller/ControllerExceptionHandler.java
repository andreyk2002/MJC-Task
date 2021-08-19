package com.epam.esm.controller;

import com.epam.esm.service.excepiton.CertificateNotFoundException;
import com.epam.esm.service.excepiton.TagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(value = {CertificateNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage certificateNotFoundException(CertificateNotFoundException e, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                "Rabotaet",
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = {TagNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage tagNotFoundException(TagNotFoundException e, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                "Rabotaet i s tagami",
                request.getDescription(false)
        );
    }
}
