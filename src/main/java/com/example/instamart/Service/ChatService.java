package com.example.instamart.Service;

import com.example.instamart.Entity.ItemEntry;
import com.example.instamart.Repository.ItemRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final String baseUrl = "https://api.groq.com/openai/v1/chat/completions";

    private final ItemRepository itemRepository;
    private final CartService cartService;

    private final Map<String, List<ItemEntry>> pendingCart = new HashMap<>();

    public ChatService(ItemRepository itemRepository, CartService cartService) {
        this.itemRepository = itemRepository;
        this.cartService = cartService;
    }

    // 🔥 MAIN METHOD
    public String processMessage(String userId, String userMessage) {

        if (userMessage.equalsIgnoreCase("yes") ||
                userMessage.equalsIgnoreCase("no")) {
            return handleCartLogic(userId, userMessage);
        }

        try {
            String aiResponse = callGroq(userMessage);
            String json = extractContent(aiResponse);

            return handleCartLogic(userId, json);

        } catch (Exception e) {
            System.out.println("❌ ERROR IN CHAT SERVICE:");
            e.printStackTrace();
            return "Something went wrong. Please try again.";
        }
    }

    // 🔹 STEP 1: CALL GROQ API
    private String callGroq(String userMessage) {

        RestTemplate restTemplate = new RestTemplate();

        String systemPrompt = """
        You are an AI assistant for an e-commerce application.

        Convert user input into structured JSON.

        Rules:
        - Return ONLY valid JSON
        - No explanation
        - If category mentioned → include "category"
        - If budget mentioned → include "budget"
        - If items mentioned → include "items"
        - Each item must have:
          - name
          - quantity (default 1)

        If unclear:
        { "error": "invalid request" }
        """;

        JSONObject body = new JSONObject();

        // ✅ GROQ MODEL
        body.put("model", "llama3-8b-8192");
        body.put("temperature", 0.2);
        body.put("max_tokens", 200);

        JSONArray messages = new JSONArray();

        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);

        messages.put(systemMsg);
        messages.put(userMsg);

        body.put("messages", messages);

        // ✅ HEADERS
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(baseUrl, entity, String.class);

            System.out.println("✅ GROQ RESPONSE = " + response.getBody());

            return response.getBody();

        } catch (Exception e) {
            System.out.println("❌ GROQ CALL FAILED:");
            e.printStackTrace();
            throw e;
        }
    }

    // 🔹 STEP 2: EXTRACT JSON
    private String extractContent(String responseBody) {

        JSONObject json = new JSONObject(responseBody);

        String content = json
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");

        return content
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .replaceAll("Here is.*?:", "")
                .trim();
    }

    // 🔹 STEP 3: HANDLE CART LOGIC
    private String handleCartLogic(String userId, String input) {

        // 🟢 YES / NO handling
        if (pendingCart.containsKey(userId)) {

            String msg = input.toLowerCase();

            if (msg.contains("yes")) {

                List<ItemEntry> items = pendingCart.get(userId);

                cartService.addAllItems(userId, items);

                pendingCart.remove(userId);

                int totalAmount = cartService.getTotalAmount(userId);
                int totalItems = cartService.getTotalItems(userId);

                return "✅ Items added to cart!" +
                        "\nTotal Items: " + totalItems +
                        "\nTotal Price: ₹" + totalAmount;
            }

            if (msg.contains("no")) {
                pendingCart.remove(userId);
                return "❌ Okay, items were not added.";
            }
        }

        JSONObject data;

        try {
            data = new JSONObject(input);
        } catch (Exception e) {
            return "Invalid request. Try again.";
        }

        if (data.has("error")) {
            return "Sorry, I didn’t understand your request.";
        }

        // 🔹 CATEGORY CASE
        if (data.has("category")) {

            String category = data.getString("category");
            int budget = data.optInt("budget", 1000);

            List<ItemEntry> items =
                    itemRepository.findByCategoryIgnoreCase(category);

            List<ItemEntry> filtered = items.stream()
                    .filter(item -> item.getPrice() <= budget)
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                return "No items found under ₹" + budget;
            }

            pendingCart.put(userId, filtered);

            List<String> itemNames = filtered.stream()
                    .map(ItemEntry::getItem)
                    .collect(Collectors.toList());

            return "🛒 I found these items:\n" + itemNames +
                    "\n\nDo you want to add them to cart? (yes/no)";
        }

        // 🔹 ITEMS CASE
        if (data.has("items")) {

            JSONArray itemsArray = data.getJSONArray("items");

            List<ItemEntry> tempList = new ArrayList<>();
            List<String> itemNames = new ArrayList<>();

            for (int i = 0; i < itemsArray.length(); i++) {

                JSONObject itemObj = itemsArray.getJSONObject(i);

                String name = itemObj.getString("name");

                List<ItemEntry> foundItems =
                        itemRepository.findByItemContainingIgnoreCase(name);

                if (!foundItems.isEmpty()) {
                    ItemEntry item = foundItems.get(0);
                    tempList.add(item);
                    itemNames.add(item.getItem());
                }
            }

            if (tempList.isEmpty()) {
                return "No matching items found.";
            }

            pendingCart.put(userId, tempList);

            return "🛒 Found these items:\n" + itemNames +
                    "\n\nAdd them to cart? (yes/no)";
        }

        return "Could not process request.";
    }
}
