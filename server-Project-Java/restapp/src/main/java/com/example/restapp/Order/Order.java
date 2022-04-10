package com.example.restapp.Order;

import com.example.restapp.Product;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
public class Order {
    private Long id;
    private LocalDate purchaseDate;
    private String title;
    private Double price;
    private List<Product> productList ;



    public Order(LocalDate purchaseDate, String title, Double price, List<Product> productList) {
//        this.purchaseDate = purchaseDate;
        this.title = title;
        this.price = price;
        this.productList = productList;
    }

    public Order() {}


    public Boolean getProductListByPrice(Double price, List<Product> productList) { //    public Boolean getProductListByPrice(Double price, List<Product> productList) { //

        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getPrice() < price){
                return true;
            }
        }
        return false;
    }

    public List<Product> filterByPrice ( List<Product> list, Double price) {  // Returns a sorted list with products that are smaller than the price
        List<Product> listResult = new ArrayList<Product>();
        for (Product element: list) {
            if (element.getPrice()<price) {
                listResult.add(element);
            }
        }
        return listResult;
    }

}
