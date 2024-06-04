package com.climbing.api.chat.service;

import com.climbing.api.chat.ChatRoom;
import com.climbing.api.chat.repository.ChatRoomRepository;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymException;
import com.climbing.domain.gym.GymExceptionType;
import com.climbing.domain.gym.GymRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final GymRepository gymRepository;



    @Override
    public ChatRoom createChatRoom(String nickname, Long gymId) { //채팅방 생성
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new GymException(GymExceptionType.GYM_NOT_FOUND));
        ChatRoom chatRoom = ChatRoom.of(nickname, gym);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    @Override
    public boolean isRoomExistsByNicknameAndGymId(String nickname, Long gymId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(nickname).orElse(null);
        return chatRoom != null && chatRoom.getGym().getId().equals(gymId);
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
