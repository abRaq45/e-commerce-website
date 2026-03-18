package com.example.instamart.Controller;

import com.example.instamart.Entity.Order;
import com.example.instamart.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //PLACE ORDER
    @PostMapping("/{userId}")
    public ResponseEntity<Order> placeOrder(@PathVariable String userId,
                                            @RequestBody Map<String,String> request)
    {
        String address = request.get("address");
        String city = request.get("city");
        String pincode = request.get("pincode");

        return ResponseEntity.ok(orderService.placeOrder(userId,address,city,pincode));
    }
    //  Get Order History
    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable String userId) {
        return ResponseEntity.ok(
                orderService.getUserOrder(userId)
        );
    }
}
