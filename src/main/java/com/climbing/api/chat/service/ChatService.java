package com.climbing.api.chat.service;

import com.climbing.api.chat.ChatRoomResponse;

import java.util.List;

public interface ChatService {
    ChatRoomResponse createChatRoom(String nickname, Long gymId);

    boolean isRoomExistsByNicknameAndGymId(String nickname, Long gymId);

    ChatRoomResponse findChatRoomById(Long id);

    List<ChatRoomResponse> findAllChatRooms();
}
