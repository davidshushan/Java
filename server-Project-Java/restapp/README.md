# PART A
## restApp

schema:

![schema](https://user-images.githubusercontent.com/93195038/162620615-aa6fc212-0960-4159-b226-80663f40d7eb.jpg)

## Product class:

## RESTController
1. @GetMapping("/products")
public ResponseEntity<CollectionModel<EntityModel< product>>> allProducts()

2. @GetMapping(“/products/{id}”)
public ResponseEntity<EntityModel< product>> singleProduct(@PathVariable long id)

3. @GetMapping("/orders/{id}/products")
public ResponseEntity<CollectionModel<EntityModel<products>>>
productsByOrder(@PathVariable long id)
  
  
  @GetMapping("/posts")
public Post singlePost(@RequestParam String title) {
return postRepo.findByTitle(title);
}
  
  OrderController
  
  @GetMapping("/Orders")
ResponseEntity<CollectionModel<EntityModel<Profile>>> allOrders()

2. @GetMapping("/Order/{id}")
ResponseEntity<EntityModel<Profile>> singleOrder(@PathVariable long id)
  
