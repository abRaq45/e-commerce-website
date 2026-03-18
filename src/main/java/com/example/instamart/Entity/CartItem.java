package com.example.instamart.Entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    private String productId;

    private String productName;   // optional but useful

    private int quantity;

    private double price;
    private double totalPrice;// price at time of adding

    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}