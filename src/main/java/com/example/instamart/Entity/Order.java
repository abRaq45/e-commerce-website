package com.example.instamart.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String id;

    private String userId;

    private List<CartItem> items;

    private String address;
    private String city;
    private String pincode;

    private double subtotal;
    private double tax;
    private double deliveryFee;
    private double totalAmount;

    private String status; // PLACED, SHIPPED, DELIVERED

    private LocalDateTime orderDate;
}