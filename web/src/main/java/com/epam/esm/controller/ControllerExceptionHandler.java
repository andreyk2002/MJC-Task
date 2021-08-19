package com.epam.esm.controller;

import com.epam.esm.localization.Localizer;
import com.epam.esm.service.excepiton.CertificateNotFoundException;
import com.epam.esm.service.excepiton.CodeException;
import com.epam.esm.service.excepiton.TagAlreadyExistException;
import com.epam.esm.service.excepiton.TagNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@AllArgsConstructor
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ControllerExceptionHandler.class);

    private final Localizer localizer;


    @ExceptionHandler(value = {CertificateNotFoundException.class, TagNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage certificateNotFoundException(CodeException e, WebRequest request) {
        LOGGER.error(e.getMessage(), e);
        int errorCode = e.getCode();
        String localizedMessage = localizer.getLocalizedMessage(errorCode);
        return new ErrorMessage(
                errorCode,
                LocalDateTime.now(),
                localizedMessage,
                request.getDescription(false)
        );
    }


    @ExceptionHandler(value = {TagAlreadyExistException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage tagNotFoundException(TagAlreadyExistException e, WebRequest request) {
        LOGGER.error(e.getMessage(), e);
        int errorCode = e.getCode();
        String localizedMessage = localizer.getLocalizedMessage(errorCode);
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                localizedMessage,
                request.getDescription(false)
        );
    }
}
