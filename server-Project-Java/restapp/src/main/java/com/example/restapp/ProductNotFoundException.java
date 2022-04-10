package com.example.restapp;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("There is not a product corresponding to " + id + " id ");// There is no product that matches this ID.
    }

    public ProductNotFoundException(String title) {
        super("There is not a product corresponding to " + title + " title ");// There is no product that matches this title.
    }


}
