package com.example.restapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class LoadDataBase {
   private  static final Logger log = LoggerFactory.getLogger(LoadDataBase.class);
    @Bean

    CommandLineRunner initDataBase(ProductRepo myProducts){
            return  args -> {
                log.info("logging" +
                        myProducts.save(new Product("AirPods v3 20201", "HeadPhones", 699.0, "The best HeadPhones")));
                log.info("logging" +
                        myProducts.save(new Product("Iphone 13", "Cellular", 4000.0 , "The best iphone")));
                log.info("logging" +
                        myProducts.save(new Product("MacBook pro 2021", "laptops", 23000.0, "The best laptops")));
            };




    }



}


