package com.example.instamart.Service;

import com.example.instamart.Entity.CartItem;
import com.example.instamart.Entity.ItemEntry;
import com.example.instamart.Entity.UserEntry;
import com.example.instamart.Repository.ItemRepository;
import com.example.instamart.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public CartService(UserRepository userRepository,
                       ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    // 🛒 Add item to cart
    public UserEntry addToCart(String userId, String productId, int quantity) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ItemEntry product = itemRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = user.getCartItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // If item already exists → increase quantity
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + quantity
            );
        } else {
            // Add new cart item
            CartItem cartItem = CartItem.builder()
                    .productId(product.getId())
                    .productName(product.getItem())
                    .quantity(quantity)
                    .price(product.getPrice())
                    .build();

            user.getCartItems().add(cartItem);
        }

        return userRepository.save(user);
    }

    // ❌ Remove item from cart
    public UserEntry removeFromCart(String userId, String productId) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getCartItems()
                .removeIf(item -> item.getProductId().equals(productId));

        return userRepository.save(user);
    }

    // 🔄 Update quantity
    public UserEntry updateQuantity(String userId,
                                    String productId,
                                    int quantity) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getCartItems().forEach(item -> {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
            }
        });

        return userRepository.save(user);
    }

    // 🧹 Clear cart
    public UserEntry clearCart(String userId) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getCartItems().clear();

        return userRepository.save(user);
    }

    // 👀 Get cart
    public UserEntry getCart(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 🔹 Add single item (used by AI)
    public UserEntry addItem(String userId, ItemEntry item, int qty) {
        return addToCart(userId, item.getId(), qty);
    }

    // 🔹 Add multiple items (used by AI)
    public UserEntry addAllItems(String userId, List<ItemEntry> items) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (ItemEntry item : items) {
            addToCart(userId, item.getId(), 1); // default qty = 1
        }

        return userRepository.findById(userId).get();
    }

    // 💰 Get total cart amount
    public int getTotalAmount(String userId) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getCartItems()
                .stream()
                .mapToInt(item -> (int) (item.getPrice() * item.getQuantity()))
                .sum();
    }

    // 📦 Get total number of items
    public int getTotalItems(String userId) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getCartItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    public List<String> getCartItemNames(String userId) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getCartItems()
                .stream()
                .map(CartItem::getProductName)
                .toList();
    }
}