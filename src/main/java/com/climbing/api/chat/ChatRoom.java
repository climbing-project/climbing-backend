package com.climbing.api.chat;

import com.climbing.domain.gym.Gym;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity(name = "chat_room")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String roomName; // 사용자 닉네임
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;
    @CreatedDate
    private LocalDateTime createdAt;

    public static ChatRoom of(String roomName, Gym gym) {
        return ChatRoom.builder()
                .roomName(roomName)
                .gym(gym)
                .build();
    }

}
