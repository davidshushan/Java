package com.example.restapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product,Long> {  // We will store in the database product and their ID will be of the Long type

      Optional<Product> findByTitle (String title);
}
