package com.example.instamart.Repository;

import com.example.instamart.Entity.ItemEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemRepository extends MongoRepository<ItemEntry, String> {

    List<ItemEntry> findByItemIgnoreCase(String item);

    List<ItemEntry> findByCategoryIgnoreCase(String category);

    List<ItemEntry> findByPriceBetween(int min, int max);

    List<ItemEntry> findByItemContainingIgnoreCase(String item);

}