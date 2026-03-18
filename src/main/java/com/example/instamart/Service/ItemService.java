package com.example.instamart.Service;
import com.example.instamart.Entity.ItemEntry;
import com.example.instamart.Repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemEntry> getAllItems() {
        return itemRepository.findAll();
    }

    public List<ItemEntry> getItemByCategory(String category) {
        return itemRepository.findByCategoryIgnoreCase(category);
    }

    public List<ItemEntry> getItemByName(String item) {
        return itemRepository.findByItemIgnoreCase(item);
    }

    public ItemEntry addItem(ItemEntry itemEntry) {
        return itemRepository.save(itemEntry);
    }

    public Optional<ItemEntry> getItemById(String id) {
        return itemRepository.findById(id);
    }

    public void deleteItemById(String id) {
        itemRepository.deleteById(id);
    }

    public ItemEntry updateItem(String id, ItemEntry updatedItem) {

        return itemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setItem(updatedItem.getItem());
                    existingItem.setPrice(updatedItem.getPrice());
                    existingItem.setCategory(updatedItem.getCategory());
                    existingItem.setItemPosterUrl(updatedItem.getItemPosterUrl());
                    return itemRepository.save(existingItem);
                })
                .orElseThrow(() ->
                        new RuntimeException("Item not found with id " + id));
    }

}
