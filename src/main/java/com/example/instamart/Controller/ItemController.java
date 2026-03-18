package com.example.instamart.Controller;
import com.example.instamart.Entity.ItemEntry;
import com.example.instamart.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController{

    @Autowired
    private ItemService itemService;

    //ADD ITEM;
    @PostMapping
    public ItemEntry addItems(@RequestBody ItemEntry itemEntry){
        return itemService.addItem(itemEntry);
    }

    //GET ALL ITEMS
    @GetMapping
    public List<ItemEntry> getAllItems(){
        return itemService.getAllItems();
    }

    //GET ITEM BY ID
    @GetMapping("/{id}")
    public ItemEntry getItemById(@PathVariable String id) {
        return itemService.getItemById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    //GET ITEM BY NAME
    @GetMapping("/item/{item}")
    public List<ItemEntry> getItemByItemName(@PathVariable String item)
    {
        return itemService.getItemByName(item);
    }

    //GET ITEM BY CATEGORY
    @GetMapping("/category/{category}")
    public List<ItemEntry> getItemByCategory(@PathVariable String category){
        return itemService.getItemByCategory(category);
    }

    //DELETE ITEM BY ID
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable String id){
        itemService.deleteItemById(id);
        return "Item with id" + id  + "Deleted successfully!";
    }

    //UPDATE ITEM
    @PutMapping("/{id}")
    public ItemEntry updateItem(@PathVariable String id,@RequestBody ItemEntry updateItem){
        return itemService.updateItem(id,updateItem);
    }
}
