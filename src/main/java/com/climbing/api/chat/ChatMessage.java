package com.climbing.api.chat;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_message")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

//    public enum MessageType {
//        ENTER, TALK, LEAVE
//    }

    @Id
    private String id;
    private Long roomId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;

    public ChatMessage(Long roomId, String sender, String message, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }
}
