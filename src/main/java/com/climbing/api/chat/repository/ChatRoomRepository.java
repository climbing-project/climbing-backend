package com.climbing.api.chat.repository;

import com.climbing.api.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomName(String roomName);

    Optional<ChatRoom> findByRoomNameAndGymId(String roomName, Long gymId);

    List<ChatRoom> findByGymId(Long gymId);
}
