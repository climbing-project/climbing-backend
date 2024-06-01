package com.climbing.api.chat.service;

import com.climbing.api.chat.ChatRoom;
import com.climbing.api.chat.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom createChatRoom(String nickname, Long gymId, String createDate) { //채팅방 생성
        ChatRoom chatRoom = ChatRoom.of(nickname, gymId, createDate);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    @Override
    public boolean isRoomExistsByNicknameAndGymId(String nickname, Long gymId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(nickname).orElse(null);
        return chatRoom != null && chatRoom.getGymId().equals(gymId);
    }

    @Override
    public ChatRoom findChatRoomById(Long id) {
        return chatRoomRepository.findById(id).orElse(null);
    }

    @Override
    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findAll();
    }
}
