package com.climbing.api.chat.service;

import com.climbing.api.chat.ChatRoom;

import java.util.List;

public interface ChatService {
    ChatRoom createChatRoom(String nickname, Long gymId, String createDate);

    boolean isRoomExistsByNicknameAndGymId(String nickname, Long gymId);

    ChatRoom findChatRoomById(Long id);

    List<ChatRoom> findAllChatRooms();
}
