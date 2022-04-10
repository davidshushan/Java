package com.example.restapp;

import com.example.restapp.Order.Order;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/*
    A domain object for our project.
    Represent a (naive) Post implementation in a social network.
     */
@Data     // longBock= annotation that gives Getters, Setters, equals, hashcode, toString

@Entity  // saved in dataBase
public class Product implements Comparable<Product> {
    private @Id @GeneratedValue  Long id; // primary key --> @Id , @GeneratedValue --> מאחתל לבד
    private  String productName;
    private  String category;
    private  Double price;
    private  String description;


    @ManyToOne
    private Order order;


    public Product(){}

    public Product(String productName, String category , Double price , String description){
        this.productName = productName;
        this.category=category;
        this.price = price;
        this.description = description;
      //  this.order = null;
    }


    @Override
    public int compareTo(Product other) {
        return Double.compare(this.getPrice(),other.getPrice());
    }
}
