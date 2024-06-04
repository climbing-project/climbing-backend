package com.climbing.api.chat.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageRequest {
    
    private Long roomId;
    private String sender;
    private String message;

}
