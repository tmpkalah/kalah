package com.backbase.kalah.controller;

import com.backbase.kalah.controller.dto.ErrorDto;
import com.backbase.kalah.domain.InvalidMoveException;
import com.backbase.kalah.service.GameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDto processGameNotFoundException(GameNotFoundException ex) {
        return new ErrorDto("Game not found");
    }

    @ExceptionHandler(InvalidMoveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto processInvalidMoveException(InvalidMoveException ex) {
        return new ErrorDto("Invalid move");
    }

}

