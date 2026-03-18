package com.example.instamart.Repository;

import com.example.instamart.Entity.UserEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntry,String> {
    Optional<UserEntry> findUserByUsername(String username);


    Optional<UserEntry> findByEmail(String email);
    boolean existsByUsername(String username);

    // Check if an email already exists
    boolean existsByEmail(String email);
}
