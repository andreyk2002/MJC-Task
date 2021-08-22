package com.epam.esm.controller;

import com.epam.esm.localization.Localizer;
import com.epam.esm.service.excepiton.NotFoundException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ControllerExceptionHandler.class);
    public static final String PATH_VARIABLES = "org.springframework.web.servlet.View.pathVariables";

    private final Localizer localizer;


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public List<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        LOGGER.error("Exception: {}", allErrors, e);
        return allErrors.stream()
                .map(x -> {
                            int code = Integer.parseInt(x.getDefaultMessage());
                            String localizedMessage = localizer.getLocalizedMessage(code);
                            return new ErrorMessage(
                                    code, LocalDateTime.now(), localizedMessage, request.getDescription(false));
                        }
                ).collect(Collectors.toList());
    }


    @ExceptionHandler(value = {
            NotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage certificateNotFoundException(NotFoundException e, WebRequest request) {
        Object[] params = e.getParams();
        LOGGER.error(e.getMessage(), e);
        int errorCode = e.getErrorCode();
        String localizedMessage = localizer.getLocalizedMessage(errorCode, params);
        return new ErrorMessage(
                errorCode,
                LocalDateTime.now(),
                localizedMessage,
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage internalServerError(Exception e, WebRequest request) {
        LOGGER.error(e.getMessage(), e);
        int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String localizedMessage = localizer.getLocalizedMessage(errorCode);
        return new ErrorMessage(
                errorCode,
                LocalDateTime.now(),
                localizedMessage,
                request.getDescription(false)
        );
    }


}
