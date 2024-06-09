package com.climbing.api.chat.service;

import com.climbing.api.chat.ChatMessage;
import com.climbing.api.chat.ChatRoom;
import com.climbing.api.chat.exception.ChatRoomException;
import com.climbing.api.chat.exception.ChatRoomExceptionType;
import com.climbing.api.chat.repository.ChatMessageRepository;
import com.climbing.api.chat.repository.ChatRoomRepository;
import com.climbing.api.chat.request.ChatMessageRequest;
import com.climbing.api.chat.response.ChatMessageResponse;
import com.climbing.api.chat.response.ChatRoomResponse;
import com.climbing.api.chat.response.RoomExistResponse;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymException;
import com.climbing.domain.gym.GymExceptionType;
import com.climbing.domain.gym.GymRepository;
import com.climbing.global.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GymRepository gymRepository;

    @Override
    public ChatRoomResponse createChatRoom(String nickname, Long gymId) throws BaseException { //채팅방 생성
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new GymException(GymExceptionType.GYM_NOT_FOUND));
        if (chatRoomRepository.findByRoomNameAndGymId(nickname, gymId).isEmpty()) {
            ChatRoom chatRoom = ChatRoom.of(nickname, gym);
            return ChatRoomResponse.of(chatRoomRepository.save(chatRoom));
        }
        ChatRoom existChatRoom = chatRoomRepository.findByRoomNameAndGymId(nickname, gymId).orElseThrow(() -> new ChatRoomException(ChatRoomExceptionType.NOT_FOUND_CHATROOM));
        return ChatRoomResponse.of(existChatRoom);
    }

    @Override
    public RoomExistResponse isRoomExistsByNicknameAndGymId(String nickname, Long gymId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(nickname).orElse(null);
        boolean exist = false;
        Long roomId = null;
        if (chatRoom != null) {
            exist = chatRoom.getGym().getId().equals(gymId);
        }
        if (exist) {
            roomId = chatRoom.getId();
        }
        return RoomExistResponse.of(exist, roomId);
    }

    @Override
    public ChatRoomResponse findChatRoomById(Long id) throws BaseException {
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(() -> new ChatRoomException(ChatRoomExceptionType.NOT_FOUND_CHATROOM));
        return ChatRoomResponse.of(room);
    }

    @Override
    public List<ChatRoomResponse> findChatRoomByGymId(Long gymId) throws BaseException {
        List<ChatRoom> rooms = chatRoomRepository.findByGymId(gymId);
        return rooms.stream().map(ChatRoomResponse::of).collect(Collectors.toList());
    }

    @Override
    public List<ChatRoomResponse> findAllChatRooms() {
        List<ChatRoom> rooms = chatRoomRepository.findAll();
        return rooms.stream().map(ChatRoomResponse::of).collect(Collectors.toList());
    }

    @Override
    public Flux<ChatMessageResponse> findChatMessages(Long roomId) {
        Flux<ChatMessage> chatMessageFlux = chatMessageRepository.findAllByRoomId(roomId);
        return chatMessageFlux.map(ChatMessageResponse::of);
    }

    @Override
    public Mono<ChatMessage> saveChatMessages(ChatMessageRequest chat) {
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String localDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return chatMessageRepository.save(
                new ChatMessage(chat.getRoomId(), chat.getSender(), chat.getMessage(), localDateTime)
        );
    }
}
