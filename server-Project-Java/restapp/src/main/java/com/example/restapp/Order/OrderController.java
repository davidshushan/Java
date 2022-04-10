package com.example.restapp.Order;
import com.example.restapp.Product;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@RestController


public class OrderController {
    private OrderRepo orderDataBase;  // data member from linking layer
    private OrderEntityFactory orderFactory;


    public OrderController(OrderRepo orderDataBase, OrderEntityFactory orderFactory) {
        this.orderDataBase = orderDataBase;
        this.orderFactory = orderFactory;
    }

//(1)
    @GetMapping("/orders")  // all orders from the database
    public ResponseEntity<CollectionModel<EntityModel<Order>>> getAllOrders(){
        List<EntityModel<Order>> orders = orderDataBase.findAll()
                .stream().map(order -> orderFactory.toModel(order)).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of((orders)));
    }

    /*
    // --> not working
    // (5) --> change 1
    @GetMapping("/orders")  //all orders from the database
    public ResponseEntity<CollectionModel<EntityModel<Order>>> getAllOrders(@RequestParam Double price){
        List<EntityModel<Order>> orders = orderDataBase.findAll()
                .stream().filter(order -> order.getProductListByPrice(price,order.getProductList()))
                    .map(order -> orderFactory.toModel(order)).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of((orders)));
        /////////////////////////// to do
//        List<Product> temp;
//        List<Order> orders = (List<Order>) orderDataBase.findAll()
//                .stream().map(order -> order.filterByPrice(order.getProductList(),price));
//
////
////        List<EntityModel<Order>> orders2 = orders.map(order -> orderFactory.toModel(order)).collect(Collectors.toList());
////        return ResponseEntity.ok(CollectionModel.of((orders2)));

    }

*/

//(2)
    @GetMapping("/orders/{id}") // This method will retrieve the order by ID and return it wrapped in EntityModel
    public ResponseEntity<EntityModel<Order>> getSingleOrder(@PathVariable Long id){
        Order order = orderDataBase.findById(id)
                .orElseThrow(()-> new OrderNotFoundException(id));  //If there is no product then we will throw an error because there will be nothing to return
        return ResponseEntity.ok(orderFactory.toModel(order));

        // We already defined the status 404 in the class ProductAdvice. "@ResponseStatus(HttpStatus.NOT_FOUND)"
    }


// (3)
    @GetMapping("/orders/{id}/products")
    public ResponseEntity<CollectionModel<EntityModel<Product>>> productsFromOrder(@PathVariable long id){
        Order order = orderDataBase.findById(id)
                .orElseThrow(()-> new OrderNotFoundException(id));  // If I do not have the order then we will throw an error because there will be nothing to return

        // If there is an order we will take all the products from the same order
        List<Product> productsOrder = order.getProductList();

        // צריך להחזיר את הרשימה של המוצרים בטיפוס של CollectionModel וEntityModel לכן נעשה קאסטינג לרשימה
        List<EntityModel<Product>> EntityProductsFListFromOrder = productsOrder
                .stream().map(product -> orderFactory.toModelProductInOrder(order,product)).collect(Collectors.toList()); // Add links

        return ResponseEntity.ok(CollectionModel.of(EntityProductsFListFromOrder));
    }


// (3 in the second page)
    @PostMapping("/Orders")
    ResponseEntity<?> placeOrder(@RequestBody Order newOrder){
        EntityModel<Order> orderRepresentation = //Representation of the resource we will return
                orderFactory.toModel(orderDataBase.save(newOrder)); // returns what we have saved

        return ResponseEntity.created(orderRepresentation
                .getRequiredLink(IanaLinkRelations.SELF) //IanaLinkRelations – gives details on the product.
                        .toUri()).body(orderRepresentation);
    }


//  4) in the second page --> add product to order
    @PutMapping("/Order/{id}")
        public ResponseEntity addToOrder (@PathVariable long id , @RequestBody Product newP) {
            Order order = orderDataBase.findById(id)
                    .orElseThrow(()-> new OrderNotFoundException(id));  //If there is no order then we will throw an error because there will be nothing to return

            order.getProductList().add(newP);
            Order newOrder = orderDataBase.save(order);
            EntityModel<Order> orderLink = orderFactory.toModel(newOrder);
            return ResponseEntity.ok("The order updated");
        }

//  6) in the second page
    @GetMapping("/orders/{id}/sale")
    public ResponseEntity <CollectionModel<EntityModel<Product>>> sale(@PathVariable long id){
        Order order = orderDataBase.findById(id)
                .orElseThrow(()-> new OrderNotFoundException(id));  // If there is no order then we will throw an error because there will be nothing to return

        List <Product> productsOrder = order.getProductList() ;  //We have saved all the products that are in the order in a new list
        List <Product> temp =new Stack();


        int j= productsOrder.size();
        int i=0;

        while (j!=0 ){                                              //We want to run on our list and add the products to the cartridge
            temp.add(productsOrder.get(i));
            temp.get(i).setPrice( temp.get(i).getPrice()* 0.75);    // We got to our product and changed its price.
            i++;
            j--;

        }

        List<EntityModel<Product>> newPriceList = temp
                .stream().map(product -> orderFactory.toModelProductInOrder(order,product))
                .collect(Collectors.toList()); // Add links

        return ResponseEntity.ok(CollectionModel.of(newPriceList));




    }


















}
