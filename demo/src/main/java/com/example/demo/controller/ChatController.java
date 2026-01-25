package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.model.ChatMessage;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {"http://localhost:5173", "https://car-app-ch3s.onrender.com"})
public class ChatController {

    @Autowired
    private ChatRepository chatRepo;

    @Autowired
    private CarRepository carRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private String safe(String email) {
    return email.replace(".", "_");
}


    /* ---------------- LOAD CHAT FOR A CAR ---------------- */
    @GetMapping("/{carId}")
    public List<ChatMessage> loadChat(@PathVariable String carId) {
        return chatRepo.findByCarIdOrderByTimestampAsc(carId);
    }

    /* ---------------- WEBSOCKET SEND MESSAGE ---------------- */
    @MessageMapping("/chat/{carId}")
public void sendMessage(
        @DestinationVariable String carId,
        @Payload ChatMessage message
) {
    message.setCarId(carId);
    message.setTimestamp(LocalDateTime.now());

    if (message.getBuyerEmail() == null || message.getSellerEmail() == null) {
        System.out.println("❌ Missing buyer/seller email");
        return;
    }

    chatRepo.save(message);

messagingTemplate.convertAndSend(
    "/topic/chat/" + carId + "/" + safe(message.getSellerEmail()),
    message
);

messagingTemplate.convertAndSend(
    "/topic/chat/" + carId + "/" + safe(message.getBuyerEmail()),
    message
);


}


    

    

    /* ---------------- SELLER INBOX ---------------- */
   @GetMapping("/seller/{sellerEmail}")
public Map<String, Set<String>> sellerInbox(@PathVariable String sellerEmail) {

    Map<String, Set<String>> inbox = new HashMap<>();

    List<ChatMessage> msgs =
            chatRepo.findAll(); // simple & safe

    for (ChatMessage m : msgs) {
        if (sellerEmail.equals(m.getSellerEmail())) {
            inbox
              .computeIfAbsent(m.getCarId(), k -> new HashSet<>())
              .add(m.getBuyerEmail());
        }
    }
    return inbox;
}
@GetMapping("/buyer")
public List<ChatMessage> loadBuyerChat(
    @RequestParam String carId,
    @RequestParam String sellerEmail,
    @RequestParam String buyerEmail
) {
    return chatRepo
      .findByCarIdAndSellerEmailAndBuyerEmailOrderByTimestampAsc(
          carId, sellerEmail, buyerEmail
      );
}

  

}