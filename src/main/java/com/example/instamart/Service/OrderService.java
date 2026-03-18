package com.example.instamart.Service;
import com.example.instamart.Entity.Order;
import com.example.instamart.Entity.UserEntry;
import com.example.instamart.Repository.OrderRepository;
import com.example.instamart.Repository.UserRepository;
import com.example.instamart.Repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository item;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ItemRepository item) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.item = item;
    }
    public Order placeOrder(String userId,String address,String city,String pincode) {
        UserEntry userEntry = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found")
                );
        if (userEntry.getCartItems().isEmpty()) {
            throw new RuntimeException("cart is empty");
        }
        double subtotal = userEntry.getCartItems()
                .stream().mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        //add tax and delivery
        double tax = subtotal * 0.18;
        double deliveryFee = 50;
        double totalAmount = subtotal+tax+deliveryFee;

        Order order = Order.builder()
                .userId(userId)
                .items(userEntry.getCartItems())
                .address(address)
                .city(city)
                .pincode(pincode)
                .subtotal(subtotal)
                .tax(tax)
                .deliveryFee(deliveryFee)
                .totalAmount(totalAmount)
                .status("PLACED")
                .orderDate(LocalDateTime.now())
                .build();

        Order saveOrder = orderRepository.save(order);

        //CLEAR CART AFTER ORDER
        userEntry.getCartItems().clear();
        userRepository.save(userEntry);

        return  saveOrder;

        }
        public List<Order> getUserOrder(String userId){
        return orderRepository.findByUserId(userId);
        }
    }