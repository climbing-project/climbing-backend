package com.climbing.api.chat;

import com.climbing.domain.gym.Gym;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String roomName;
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;
    @CreatedDate
    private LocalDateTime createdAt;

    public static ChatRoom create(String nickname, Gym gym) {
        ChatRoom room = new ChatRoom();
        room.id = UUID.randomUUID().toString();
        room.roomName = nickname;
        room.gym = gym;
        return room;
    }
}
