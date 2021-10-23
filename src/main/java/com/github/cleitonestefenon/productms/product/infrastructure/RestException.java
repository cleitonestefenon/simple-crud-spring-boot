package com.github.cleitonestefenon.productms.product.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class RestException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ArrayList<ErrorObject> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        ArrayList<ErrorObject> errors = new ArrayList<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            Integer status_code = HttpStatus.BAD_REQUEST.value();
            String message = error.getDefaultMessage();

            errors.add(new ErrorObject(status_code, message));
        }
        return errors;
    }
}
