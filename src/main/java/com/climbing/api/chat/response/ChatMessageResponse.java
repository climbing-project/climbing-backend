package com.climbing.api.chat.response;

import com.climbing.api.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {

    private Long roomId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;

    public static ChatMessageResponse of(ChatMessage message) {
        return new ChatMessageResponse(message.getRoomId(), message.getSender(), message.getMessage(), message.getCreatedAt());
    }
}
