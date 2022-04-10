package com.example.restapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.restapp.ProductAdvice;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;


@RestController

//indicates that the data from our method will be injected to the response payload(body)

public class ProductController {
    private ProductRepo productDataBase;            // Layer links to a database
    private ProductEntityFactory productFactory;    // product --> product with links


    public ProductController(ProductRepo aDatabase ,ProductEntityFactory  aProductFactory){
        this.productDataBase = aDatabase;
        this.productFactory = aProductFactory;
    }

//    // home route - נתיב הבית
//    @GetMapping("/products") // הנתיב שאם אליו יגשו הם יקבלו את הרשימה של המוצרים - המטודה שנמאת בשורה הבאה
//    List<Product> getAllProducts(){
//       return productDataBase.findAll();
//    }
//
//    // איך צריך באמת לכתוב:
//    @GetMapping("/products")
//    CollectionModel<EntityModel<Product>> getAllProducts(){
//        List<EntityModel<Product>> products = productDataBase.findAll()
//                .stream().map(product -> productFactory.toModel(product)).collect(Collectors.toList());
//                //.stream().map(productFactory::toModel()).collect(Collectors.toList()); // הנקודתיים זה רפרנס למטודה
//
//        //container of container of product
//        return CollectionModel.of(products,
//                linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
//    }


//    //route - נתיב נוסף של פוסט , יצירה של מוצר
//    @PostMapping("/products")
//        Product createProduct(@RequestBody Product aProduct){
//        return productDataBase.save(aProduct);
//    }

/*
    ResponseEntity is  a special container for Spring MVC that enable addition of
    response codes + data

     */


//    @GetMapping("/products/{id}")
//    Product SingleProduct(@PathVariable Long id){ // פה חילצנו את המשתנה שלנו שהוא ID מסוג LONG
//        return productDataBase.findById(id)
//                .orElseThrow(()->new ProductAdvice.ProductNotFoundException(id));
//    }
//



//   (1)
     @GetMapping("/products")
     public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts(){
        List<EntityModel<Product>> products = productDataBase.findAll()
                .stream().map(productFactory::toModel).collect(Collectors.toList());  // toModel --> change the product to <EntityModel>
                //.stream().map(product -> productFactory.toModel(product)).collect(Collectors.toList());  anther way to right this.

        return ResponseEntity.ok(CollectionModel.of((products)));  // CollectionModel is container to EntityModel of some object

    }


    // (2)
    @GetMapping("/products")
    public ResponseEntity<EntityModel<Product>> findByName (@RequestParam  String title){
        Product product = productDataBase.findByTitle(title)
                .orElseThrow(()-> new ProductNotFoundException(title));  // אם לא קיים לי המוצר אז נזרוק שגיאה כי לא יהיה מה להחזיר
        return ResponseEntity.ok(productFactory.toModel(product));

        // We already defined the status 404 in the class ProductAdvice. "@ResponseStatus(HttpStatus.NOT_FOUND)"
    }

    
    
    
 // (2)
 // במטודה זו נחזיר את המוצר שהID מתאים לו, אם קיים מוצר כזה נקבל סטטוס 200 ואם לא קיים אז נקבל סטטוס 404
    @GetMapping("/products/{id}")
     public ResponseEntity<EntityModel<Product>> getSingleProduct(@PathVariable Long id){  // @PathVariable --> מספר שמייצג לי תז
        Product product = productDataBase.findById(id)
                .orElseThrow(()-> new ProductNotFoundException(id));  // אם לא קיים לי המוצר אז נזרוק שגיאה כי לא יהיה מה להחזיר
        return ResponseEntity.ok(productFactory.toModel(product));

        // We already defined the status 404 in the class ProductAdvice. "@ResponseStatus(HttpStatus.NOT_FOUND)"
    }


//    @GetMapping("/products")
//    CollectionModel<EntityModel<Product>> getAllProducts(){
//        List<EntityModel<Product>> products = productDataBase.findAll()
//                .stream().map(productFactory::toModel).collect(Collectors.toList());
////                .stream().map(product -> productFactory.toModel(product)).collect(Collectors.toList());
//        return CollectionModel.of(products,
//                linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
//    }
//

//    @PutMapping("/products/{id}")
//    Product updateProduct(@PathVariable Long id,@RequestBody Product aProduct){ // שמאל מהפסיק איפה לעדכן ,ימין מקבל ג'ייסון חדש עם כל הפרטים
//        // אם המוצר נמצא
//        return productDataBase.findById(id).map( productToUpdate ->{
//            productToUpdate.setPrice(aProduct.getPrice());
//            productToUpdate.setCategory(aProduct.getCategory());
//            productToUpdate.setProductName(aProduct.getProductName());
//            return productDataBase.save(productToUpdate);
//        })
//        //  אם המוצר לא נמצא אפשר או לזרוק שגיאה (המחלקה שיצרנו) או לבנות מוצר חדש
//        .orElseGet(()->{
//            createProduct(aProduct).setId(id); // חייב לעשות SetId כי אם לא אז הGeneratedValue יביא לי סתם Id
//            return productDataBase.save(aProduct);
//                });
//
//    }

// ResponseEntity --> is container of EntityModel
    @PostMapping("/products")
    ResponseEntity<?> createProduct(@RequestBody Product newProduct){
        EntityModel<Product> productRepresentation =
                productFactory.toModel(productDataBase.save(newProduct)); // מחזיר את מה ששמרנו

        return ResponseEntity
                .created(productRepresentation.getRequiredLink(IanaLinkRelations.SELF)
                        .toUri()).body(productRepresentation);
    }




    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable Long id){
        productDataBase.deleteById(id);
    }

}
