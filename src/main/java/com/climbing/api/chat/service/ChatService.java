package com.climbing.api.chat.service;

import com.climbing.api.chat.ChatRoom;

import com.climbing.domain.gym.Gym;
import java.util.List;

public interface ChatService {
    ChatRoom createChatRoom(String nickname, Gym gym);

    boolean isRoomExistsByNicknameAndGymId(String nickname, Long gymId);

    ChatRoom findChatRoomById(Long id);

    List<ChatRoom> findAllChatRooms();
}
