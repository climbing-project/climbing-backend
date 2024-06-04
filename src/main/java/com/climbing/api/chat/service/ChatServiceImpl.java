package com.climbing.api.chat.service;

import com.climbing.api.chat.ChatRoom;
import com.climbing.api.chat.ChatRoomResponse;
import com.climbing.api.chat.exception.ChatRoomException;
import com.climbing.api.chat.exception.ChatRoomExceptionType;
import com.climbing.api.chat.repository.ChatRoomRepository;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymException;
import com.climbing.domain.gym.GymExceptionType;
import com.climbing.domain.gym.GymRepository;
import com.climbing.global.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final GymRepository gymRepository;

    @Override
    public ChatRoomResponse createChatRoom(String nickname, Long gymId) { //채팅방 생성
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new GymException(GymExceptionType.GYM_NOT_FOUND));
        ChatRoom chatRoom = ChatRoom.of(nickname, gym);
        return ChatRoomResponse.of(chatRoomRepository.save(chatRoom));
    }

    @Override
    public boolean isRoomExistsByNicknameAndGymId(String nickname, Long gymId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(nickname).orElseThrow(() -> new ChatRoomException(ChatRoomExceptionType.NOT_FOUND_CHATROOM));
        return chatRoom != null && chatRoom.getGym().getId().equals(gymId);
    }

    @Override
    public ChatRoomResponse findChatRoomById(Long id) throws BaseException {
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(() -> new ChatRoomException(ChatRoomExceptionType.NOT_FOUND_CHATROOM));
        return ChatRoomResponse.of(room);
    }

    @Override
    public List<ChatRoomResponse> findAllChatRooms() {
        List<ChatRoom> rooms = chatRoomRepository.findAll();
        return rooms.stream().map(ChatRoomResponse::of).collect(Collectors.toList());
    }
}
