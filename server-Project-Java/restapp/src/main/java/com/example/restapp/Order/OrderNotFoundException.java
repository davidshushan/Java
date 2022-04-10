package com.example.restapp.Order;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(Long id){
        super("There is not an order corresponding to " + id + " id "); // There is no matching order for this ID.
    }



}
