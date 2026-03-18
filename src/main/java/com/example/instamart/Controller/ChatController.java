package com.example.instamart.Controller;

import com.example.instamart.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*") // allow frontend calls
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 🔥 MAIN CHAT ENDPOINT
    @PostMapping
    public ResponseEntity<?> chat(
            @RequestParam String userId,
            @RequestBody String message
    ) {

        String response = chatService.processMessage(userId, message);

        return ResponseEntity.ok(response);
    }
}