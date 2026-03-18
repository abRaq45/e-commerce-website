package com.example.instamart.Repository;

import com.example.instamart.Entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByUserId(String userId);
}