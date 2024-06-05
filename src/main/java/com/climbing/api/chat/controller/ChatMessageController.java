package com.climbing.api.chat.controller;

import com.climbing.api.chat.request.ChatMessageRequest;
import com.climbing.api.chat.response.ChatMessageResponse;
import com.climbing.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public Mono<ResponseEntity<Void>> receiveAndSendMessage(@RequestBody ChatMessageRequest chatMessageRequest) {
        return chatService.saveChatMessages(chatMessageRequest).doOnNext(message -> {
            sendingOperations.convertAndSend("/queue/chat/room/" + chatMessageRequest.getRoomId(),
                    ChatMessageResponse.of(message));
        }).thenReturn(ResponseEntity.ok().build());
    }
}
