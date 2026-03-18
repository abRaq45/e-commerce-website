
package com.example.instamart.Entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntry {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;

    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Builder.Default
    private List<String> wishlist = new ArrayList<>();
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();
}
