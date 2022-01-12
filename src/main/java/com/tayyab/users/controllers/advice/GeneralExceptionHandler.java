package com.tayyab.users.controllers.advice;

import com.tayyab.users.dto.ErrorDto;
import com.tayyab.users.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GeneralExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    /**
     * Handle Application exception exception
     *
     * @param ex
     * @return ErrorDto
     */
    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<Object> applicationExpetion(ApplicationException ex, HttpServletRequest request) {
        logger.error("Application Exception------> " + ex.getMessage(), ex);
        ErrorDto error = new ErrorDto(ex.getCode(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
