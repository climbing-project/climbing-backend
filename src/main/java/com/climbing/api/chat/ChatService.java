package com.climbing.api.chat;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> rooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(rooms);

        return rooms;
    }

    public ChatRoom findById(String roomId) {
        return chatRoomMap.get(roomId);
    }

    public ChatRoom createChatRoom(String name) {
        log.info("채팅방이 생성되었습니다.");
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
