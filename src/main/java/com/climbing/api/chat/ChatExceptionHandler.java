package com.climbing.api.chat;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
public class ChatExceptionHandler extends StompSubProtocolErrorHandler {

    public ChatExceptionHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        if (ex.getCause().getMessage().equals("JWT Error")) {
            return jwtException(clientMessage, ex);
        }

        if (ex.getCause().getMessage().equals("Chat Message Error")) {
            return chatMessageException(clientMessage, ex);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> chatMessageException(Message<byte[]> clientMessage, Throwable ex) {
        return errorMessage(ChatErrorCode.INVALID_MESSAGE);
    }
    
    private Message<byte[]> jwtException(Message<byte[]> clientMessage, Throwable ex) {
        return errorMessage(ChatErrorCode.INVALID_JWT);
    }

    private Message<byte[]> errorMessage(ChatErrorCode chatErrorCode) {
        String code = chatErrorCode.getMessage();

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(String.valueOf(chatErrorCode.getStatus()));
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(code.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
