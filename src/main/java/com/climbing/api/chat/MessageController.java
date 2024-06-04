package com.climbing.api.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessage message) {
        message.setTimestamp(String.valueOf(LocalDateTime.now()));
        sendingOperations.convertAndSend("/queue/chat/room/" + message.getRoomId(), message);
    }
}
