package com.example.restapp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProductAdvice {
    @ResponseBody
    @ExceptionHandler(ProductNotFoundException.class)              // which class handles with the error
    @ResponseStatus(HttpStatus.NOT_FOUND)                          // status 404
    String productNotFoundHandler(ProductNotFoundException pnf){   // מה נרצה להחזיר
        return pnf.getMessage();
    }




}
