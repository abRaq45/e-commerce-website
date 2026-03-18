package com.example.instamart.Controller;
import com.example.instamart.Entity.CartItem;
import com.example.instamart.Entity.UserEntry;
import com.example.instamart.Service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //ADD TO CART
    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<UserEntry> addToCart(@PathVariable String userId, @PathVariable String productId,
                                               @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    //DELETE TO CART
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<UserEntry> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    //UPDATE CART
    @PutMapping("/{userId}/update/{productId}")
    public ResponseEntity<UserEntry> updateQuantity(@PathVariable String userId,@PathVariable String productId,
                                                    @RequestParam int quantity){
        return  ResponseEntity.ok(cartService.updateQuantity(userId,productId,quantity));
    }

    //GET CART
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable String userId){
        return ResponseEntity.ok(cartService.getCart(userId).getCartItems());
    }

    //CLEAR CART
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<UserEntry> clearCart(@PathVariable String userId){
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}

