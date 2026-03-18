package com.example.instamart.Controller;

import com.example.instamart.security.JwtUtil;
import com.example.instamart.Entity.UserEntry;
import com.example.instamart.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    //  SIGN UP
    @PostMapping("/signup")
    public ResponseEntity<UserEntry> signUp(@RequestBody UserEntry userEntry) {
        return ResponseEntity.ok(userService.signUp(userEntry));
    }

    //  LOGIN (Basic for now, JWT later)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        Optional<UserEntry> user =
                userService.login(username, password);

        if (user.isPresent()) {

            String token = jwtUtil.generateToken(username);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", username,
                    "userId", user.get().getId()   // 🔥 ADD THIS
            ));
        }

        return ResponseEntity.status(401)
                .body("Invalid credentials");
    }

    //  ADD TO WISHLIST
    @PostMapping("/{userId}/wishlist/{productId}")
    public ResponseEntity<UserEntry> addToWishlist(@PathVariable String userId,
                                                   @PathVariable String productId) {
        return ResponseEntity.ok(userService.addToWishlist(userId, productId));
    }
    @GetMapping("/{userId}/wishlist")
    public ResponseEntity<List<String>> getWishlist(
            @PathVariable String userId) {

        UserEntry user = userService.getUserById(userId);
        return ResponseEntity.ok(user.getWishlist());
    }

    //  REMOVE FROM WISHLIST
    @DeleteMapping("/{userId}/wishlist/{productId}")
    public ResponseEntity<UserEntry> removeFromWishlist(@PathVariable String userId,
                                                        @PathVariable String productId) {
        return ResponseEntity.ok(userService.removeFromWishlist(userId, productId));
    }
}