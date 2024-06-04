package com.climbing.api.chat.service;

import com.climbing.api.chat.ChatMessage;
import com.climbing.api.chat.request.ChatMessageRequest;
import com.climbing.api.chat.response.ChatMessageResponse;
import com.climbing.api.chat.response.ChatRoomResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatService {
    ChatRoomResponse createChatRoom(String nickname, Long gymId);

    boolean isRoomExistsByNicknameAndGymId(String nickname, Long gymId);

    ChatRoomResponse findChatRoomById(Long id);

    List<ChatRoomResponse> findAllChatRooms();

    Flux<ChatMessageResponse> findChatMessages(Long roomId);

    Mono<ChatMessage> saveChatMessages(ChatMessageRequest chat);
}
