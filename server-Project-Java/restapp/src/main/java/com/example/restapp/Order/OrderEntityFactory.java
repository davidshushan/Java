package com.example.restapp.Order;


import com.example.restapp.Product;

import com.example.restapp.ProductController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class OrderEntityFactory implements RepresentationModelAssembler<Order , EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order order) {
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getSingleOrder(order.getId()))
                        .withSelfRel(),  // self
                linkTo(methodOn(OrderController.class).getAllOrders())
                        .withRel("All Orders"));
    }



    @Override
    public CollectionModel<EntityModel<Order>> toCollectionModel(Iterable<? extends Order> entities) {
        return null;
    }


    public EntityModel<Product> toModelProductInOrder(Order order, Product product) {
        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class).getSingleProduct(product.getId())).
                        withSelfRel(),
                linkTo(methodOn(OrderController.class).getSingleOrder(order.getId()))
                        .withRel("Return to order"));
    }


}


