package com.example.instamart.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "items")
public class ItemEntry {

    @Id
    private String id;

    private String item;
    private String category;
    private int price;
    private String itemPosterUrl;

    // Default constructor
    public ItemEntry() {}

    // Parameterized constructor
    public ItemEntry(String item, String category, int price, String itemPosterUrl) {
        this.item = item;
        this.category = category;
        this.price = price;
        this.itemPosterUrl = itemPosterUrl;
    }
}