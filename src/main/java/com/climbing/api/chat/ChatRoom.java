package com.climbing.api.chat;

import com.climbing.domain.gym.Gym;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity(name = "chat_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String roomName; // 사용자 닉네임
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

    public static ChatRoom of(String roomName, Gym gym) {
        return ChatRoom.builder()
                .roomName(roomName)
                .gym(gym)
                .build();
    }
}
