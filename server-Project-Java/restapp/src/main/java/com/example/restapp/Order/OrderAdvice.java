package com.example.restapp.Order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice

public class OrderAdvice {
    @ResponseBody
    @ExceptionHandler(OrderNotFoundException.class) // which class handles with the error
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String OrderNotFoundHandler(OrderNotFoundException onf){    //- הודעה מובנת - מה נרצה להחזיר
        return onf.getMessage();
    }

}
