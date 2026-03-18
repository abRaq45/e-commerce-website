package com.example.instamart.Service;

import com.example.instamart.Entity.ItemEntry;
import com.example.instamart.Entity.UserEntry;
import com.example.instamart.Repository.ItemRepository;
import  com.example.instamart.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ItemRepository itemRepository){
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder= new BCryptPasswordEncoder();
    }

    //signup
    public UserEntry signUp(UserEntry userEntry){
        if(userRepository.existsByUsername(userEntry.getUsername())){
            throw new RuntimeException("username already exist");
        }
        userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));

        if (userEntry.getRoles() == null || userEntry.getRoles().isEmpty()) {
            userEntry.setRoles(List.of("ROLE_USER")); // ⚡ Add ROLE_ prefix
        }
        return userRepository.save(userEntry);

    }

    //login
    public Optional<UserEntry>login(String username,String rawPassword){
        Optional<UserEntry> userEntryOptional = userRepository.findUserByUsername(username);
        if(userEntryOptional.isPresent()){
            UserEntry userEntry = userEntryOptional.get();
            if(passwordEncoder.matches(rawPassword,userEntry.getPassword())){
                return Optional.of(userEntry);
            }
        }
        return Optional.empty();

    }
    // ❤️ Add to Wishlist
    public UserEntry addToWishlist(String userId, String productId) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ItemEntry product = itemRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!user.getWishlist().contains(productId)) {
            user.getWishlist().add(productId);
        }

        return userRepository.save(user);
    }
    // ❌ Remove from Wishlist
    public UserEntry removeFromWishlist(String userId, String productId) {

        UserEntry user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getWishlist().remove(productId);

        return userRepository.save(user);
    }

    public UserEntry getUserById(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userId)
                );
    }
}
