package com.climbing.api.chat.response;

import com.climbing.api.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private Long roomId;
    private String sender;
    private String message;

    public static ChatMessageResponse of(ChatMessage message) {
        return new ChatMessageResponse(message.getRoomId(), message.getSender(), message.getMessage());
    }
}
